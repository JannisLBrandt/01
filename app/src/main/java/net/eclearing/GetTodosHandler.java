package net.eclearing;

import com.alibaba.fastjson2.JSON;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import io.undertow.util.Headers;

import java.util.List;

public class GetTodosHandler implements HttpHandler {
    private final TodoDao todoDao;
    
    public GetTodosHandler(TodoDao todoDao) {
        this.todoDao = todoDao;
    }
    
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        try {

            String limitParam = exchange.getQueryParameters().containsKey("limit") ? exchange.getQueryParameters().get("limit").getFirst() : null;
            String offsetParam = exchange.getQueryParameters().containsKey("offset") ? exchange.getQueryParameters().get("offset").getFirst() : null;

            // default to 10 rows
            int limit = (limitParam != null) ? Integer.parseInt(limitParam) : 10;
            // default to page 1
            int offset = (offsetParam != null) ? Integer.parseInt(offsetParam) : 0;
            
            List<Task> todos = todoDao.display(limit,offset);
            String json = JSON.toJSONString(todos);
        
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send(json);
        } catch (Exception e) {
            e.printStackTrace();
            exchange.setStatusCode(500);
            exchange.getResponseSender().send("Error: " + e.getMessage());
        }
    }
}
