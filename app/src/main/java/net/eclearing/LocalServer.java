package net.eclearing;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;

public class LocalServer {

    private final Undertow undertow;
    private final int port;
    private final String host;

    public LocalServer(int port, String host, HttpHandler handler) {

        this.port = port;
        this.host = host;
        this.undertow = Undertow.builder()
            .addHttpListener(port, host)
            .setHandler(handler)
            .build();
    }

    public void start() {
        undertow.start();
        System.out.printf("Server started at %s:%d\n", this.host, this.port);
    }

    public void stop() {
        undertow.stop();
        System.out.println("Server stopped!");
    }
}
