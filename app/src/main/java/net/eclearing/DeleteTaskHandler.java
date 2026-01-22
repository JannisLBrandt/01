package net.eclearing;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class DeleteTaskHandler implements HttpHandler {
    private final TodoDao todoDao;

    public DeleteTaskHandler(TodoDao todoDao) {
        this.todoDao = todoDao;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        try {
            // get String id parameter
            String idP = exchange.getQueryParameters().get("id").getFirst();
            long id = Long.parseLong(idP);

            todoDao.deleteTask(id);

            // 
            exchange.setStatusCode(200);
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send("{\"message\":\"Task deleted\"}");
        } catch (Exception e) {
            e.printStackTrace();
            exchange.setStatusCode(500);
            exchange.getResponseSender().send("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
}
