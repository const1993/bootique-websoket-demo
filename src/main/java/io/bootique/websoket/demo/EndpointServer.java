package io.bootique.websoket.demo;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/ws/simple")
public class EndpointServer {

    @OnOpen
    public void onWebSocketConnect(Session sess)
    {
        System.out.println("Socket Connected: " + sess);
    }

    @OnMessage
    public void onWebSocketText(String message)
    {
        System.out.println("Received TEXT message: " + message);
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
}
