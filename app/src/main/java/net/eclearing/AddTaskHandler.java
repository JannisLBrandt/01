package net.eclearing;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class AddTaskHandler implements HttpHandler {
    private final TodoDao todoDao;

    public AddTaskHandler(TodoDao todoDao) {
        this.todoDao = todoDao;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        try {
            exchange.getRequestReceiver().receiveFullString((ex, title) -> {
                try {
                    todoDao.addTask(title);
                    ex.setStatusCode(201);
                    ex.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                    ex.getResponseSender().send("{\"message\":\"Task created\"}");
                } catch (Exception e) {
                    e.printStackTrace();
                    ex.setStatusCode(500);
                    ex.getResponseSender().send("{\"error\":\"Task created\"}");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            exchange.setStatusCode(500);
            exchange.getResponseSender().send("{\"error\":\"Task created\"}");
        }
    }
}
