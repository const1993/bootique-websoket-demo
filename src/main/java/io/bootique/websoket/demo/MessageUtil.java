package io.bootique.websoket.demo;

import com.google.gson.Gson;

import java.util.*;

public class MessageUtil {

    public static final String SYMBOL = "symbol";
    public static final String SCHEMA = "schema";
    public static final String MAX_FREQUENCY = "maxFrequency";
    public static final String COMMAND = "command";

    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";
    public static final String ACK = "ack";
    public static final String LOGIN = "login";
    public static final String USER = "user";

    public static Map<String, Object> parseJson(String message) {
       Map<String, Object> result = new Gson().fromJson(message, HashMap.class);
       result.putIfAbsent(MAX_FREQUENCY, Double.MAX_VALUE);
       result.putIfAbsent(SCHEMA, new ArrayList<>());
       return result;
    }

    public static String toJson(Map<String, String> message) {
        Map<String, String> toSend = new HashMap<>();
        // Beautifying JSON
        message.forEach((k, v) -> {
            toSend.put(k.toLowerCase().replace("_", ""), v);
        });

        return new Gson().toJson(toSend);
    }

    public static Map<String, Object> parsePosition(String message) {
        Map<String, Object> result = new HashMap<>();

        String[] parts = message.split("\\|");
        switch (parts[0]) {
            case "S":
                result.put(COMMAND, SUBSCRIBE);
                break;
            case "U":
                result.put(COMMAND, UNSUBSCRIBE);
                break;
            case "A":
                result.put(COMMAND, ACK);
                break;
            case "L":
                result.put(COMMAND, LOGIN);
                result.put(USER, parts[1]);
                break;
        }

        if (!LOGIN.equals(result.get(COMMAND))) {
            result.put(SYMBOL, parts[1]);

            if (parts.length > 2) {
                result.put(SCHEMA, Arrays.asList(parts[2].split(",")));
            } else {
                result.put(SCHEMA, new ArrayList<>());
            }

            if (parts.length > 3) {
                result.put(MAX_FREQUENCY, Double.parseDouble(parts[3]));
            } else {
                result.put(MAX_FREQUENCY, Double.MAX_VALUE);
            }
        }

        return result;
    }

    public static String toPositionBased(Map<String, String> message, List<String> schema) {
        StringBuilder result = new StringBuilder();
        schema.forEach(field -> {
            String value = message.get(field) != null ? message.get(field) : "";
            result.append(value).append("|");
        });

        return result.toString();
    }

    public static void toLowerCase(Map<String, String> data) {
        Map<String, String> result = new HashMap<>();
        data.forEach((k, v) -> result.put(k.toLowerCase().replace("_", ""), v));
        data.clear();
        data.putAll(result);
    }
}