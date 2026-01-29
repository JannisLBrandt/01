package net.eclearing;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class UpdateTitleHandler implements HttpHandler {
  private final TodoDao todoDao;

  public UpdateTitleHandler(TodoDao todoDao) {
    this.todoDao = todoDao;
  }

  @Override
  public void handleRequest(HttpServerExchange exchange) throws Exception {
    try {
            String idP = exchange.getQueryParameters().get("id").getFirst();
            long id = Long.parseLong(idP);
            exchange.getRequestReceiver().receiveFullString((ex, title) -> {
        try {
          todoDao.updateTitle(title, id);
          ex.setStatusCode(201);
          ex.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
          ex.getResponseSender().send("{\"message\":\"Task title updated\"}");
        } catch (Exception e) {
          e.printStackTrace();
          ex.setStatusCode(500);
          ex.getResponseSender().send("{\"error\":\"Task title not updated\"}");
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      exchange.setStatusCode(500);
      exchange.getResponseSender().send("{\"error\":\"Task title not updated\"}");
    }
  }
}
