package io.bootique.websoket.demo;

import java.util.Map;

public interface IPricingListener {

    void onData(String symbol, Map<String, String> data);
}