package io.bootique.websoket.demo;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import io.bootique.Bootique;
import io.bootique.jetty.websocket.JettyWebSocketModule;

public class Application implements Module {

    public static void main(String[] args) throws Exception {
        Bootique.app(args)
                .args("-s", "--config=classpath:bootique.yml")
                .module(Application.class).autoLoadModules().exec().exit();
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(EndpointServer.class).in(Singleton.class);

        JettyWebSocketModule.extend(binder)
                .addEndpoint(EndpointServer.class);

    }
}