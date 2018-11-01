package io.bootique.websoket.demo;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import io.bootique.Bootique;
import io.bootique.jetty.JettyModule;
import io.bootique.jetty.websocket.JettyWebSocketModule;

public class Application implements Module {

    public static void main(String[] args) throws Exception {
        Bootique.app(args)
                .module(Application.class).autoLoadModules().exec().exit();
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(StreamingEndpoint.class).in(Singleton.class);
        binder.bind(EndpointServer.class).in(Singleton.class);

        JettyWebSocketModule.extend(binder)
                .addEndpoint(StreamingEndpoint.class)
                .addEndpoint(EndpointServer.class);
        JettyModule.extend(binder)
        .addStaticServlet("content", "/content/*");
    }
}