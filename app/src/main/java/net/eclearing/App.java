package net.eclearing;

import io.undertow.server.RoutingHandler;

class App {
    public static void main(String[] args) {
        TodoDao todoDao = new TodoDao();
        
        RoutingHandler routes = new RoutingHandler()
            .get("/todos", new GetTodosHandler(todoDao))
            .post("todos", new AddTaskHandler(todoDao))
            .delete("/todos", new DeleteTaskHandler(todoDao))
            .put("/todos", new CompleteTaskHandler(todoDao));
        
        
        LocalServer server = new LocalServer(8080, "localhost", routes);
        server.start();
    }
}
