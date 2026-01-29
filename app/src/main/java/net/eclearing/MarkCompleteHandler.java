package net.eclearing;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Heads;

public class MarkCompleteHandler implements HttpHandler {
  private final TodoDao todoDao;

  public MarkCompleteHandler(TodoDao todoDao) {
    this.todoDao = todoDao;
  }

  @Override
  public void handleRequest(HttpServerExchange exchange) throws Exception {
    try {
      // get String id parameter
      String idParameter = exchange.getQueryParameters().get("id").getFirst();
      long id = Long.parseLong(idParameter);

      todoDao.completeTask(id);

      excahnge.setStatusCode(200);
      exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
      exchange.getResponseSender().send("{\"message\":\"Task marked as completed\"}");
    } catch (Exception e) {
      e.printStackTrace();
      exchange.setStatusCode(500);
      exchange.getResponseSender().send("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }
}
