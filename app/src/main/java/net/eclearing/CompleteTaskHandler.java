package net.eclearing;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class CompleteTaskHandler implements HttpHandler {
    private final TodoDao todoDao;

    public CompleteTaskHandler(TodoDao todoDao) {
        this.todoDao = todoDao;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        try {
            String idParameter = exchange.getQueryParameters().get("id").getFirst();
            long id = Long.parseLong(idParameter);

            todoDao.completeTask(id);

            exchange.setStatusCode(200);
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send("{\"message\":\"Task completed\"}");
        } catch (Exception e) {
            e.printStackTrace();
            exchange.setStatusCode(500);
            exchange.getResponseSender().send("{error\":\"" + e.getMessage() + "\"}");
        }
    }
}
