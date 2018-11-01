package io.bootique.websoket.demo;


import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.bootique.websoket.demo.MessageUtil.*;

@ServerEndpoint("/ws/basic")
public class StreamingEndpoint {

    private RandomPriceGenerator gen;
    private Map<String, SubscriptionInfo> subscriptions = new ConcurrentHashMap<>();

    public StreamingEndpoint() {
        this.gen = new RandomPriceGenerator(10);
        gen.addListener((symbol, data) -> {
            System.out.println("SEND "+ symbol + " " + data);
            toLowerCase(data);

            SubscriptionInfo sub = subscriptions.get(symbol);
            if (sub != null) {
                synchronized (sub) {
                    sub.sessions.keySet().forEach(s -> send(s, symbol, data));
                }
            }
        });
        gen.start();
    }

    private ExecutorService exec = Executors.newCachedThreadPool();
    public static class SubscriptionInfo {
        Map<Session, SessionInfo> sessions = new HashMap<>();
    }

    public static class SessionInfo {
        // Nothing to store now
    }

    @OnOpen
    public void onWebSocketConnect(Session sess) {
        System.out.println("Socket Connected: " + sess);
    }

    @OnMessage
    public void onWebSocketText(Session s, String message)
    {

        System.out.println("Received TEXT message: " + message);

        Map<String, Object> request = parseJson(message);
        String symbol = request.get(SYMBOL).toString();
        String command = request.get(COMMAND).toString();

        if (SUBSCRIBE.equals(command)) {
            subscribe(symbol, s);
        }

        if (UNSUBSCRIBE.equals(command)) {
            unsubscribe(symbol, s);
        }
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        System.out.println("Socket Closed: " + reason);
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
    }


    private void send(Session s, String symbol, Map<String, String> data) {
        exec.submit(() -> {
            try {
                data.put(SYMBOL, symbol);
                synchronized (s) {
                    s.getBasicRemote().sendText(toJson(data));
                }
            } catch (Exception e) {
                System.out.println("Failed to send data "+  e);
            }
        });
    }

    private void subscribe(String symbol, Session s) {
        subscriptions.putIfAbsent(symbol, new SubscriptionInfo());

        SubscriptionInfo sub = subscriptions.get(symbol);
        synchronized (sub) {
            SessionInfo info = new SessionInfo();
            sub.sessions.put(s, info);
            if (sub.sessions.size() == 1) {
                gen.subscribe(symbol);
            }
        }
    }

    private void unsubscribe(String symbol, Session s) {
        SubscriptionInfo sub = subscriptions.get(symbol);
        if (sub != null) {
            synchronized (sub) {
                sub.sessions.remove(s);
                if (sub.sessions.size() == 0) {
                    subscriptions.remove(symbol);
                    gen.unsubscribe(symbol);
                }
            }
        }
    }

    public static void toLowerCase(Map<String, String> data) {
        Map<String, String> result = new HashMap<>();
        data.forEach((k, v) -> result.put(k.toLowerCase().replace("_", ""), v));
        data.clear();
        data.putAll(result);
    }
}
