package net.eclearing;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Callable;

@Command(name = "todo", mixinStandardHelpOptions = true, version = "1.0",
         description = "CLI for managing todos")
public class TodoCli implements Callable<Integer> {
    
    @Option(names = {"--display-all"}, description = "Display all todos")
    private boolean displayAll;
    
    @Option(names = {"--limit"}, description = "Number of items to display", defaultValue = "10")
    private int limit;
    
    @Option(names = {"--offset"}, description = "Offset for pagination", defaultValue = "0")
    private int offset;

    @Option(names = {"--add"}, description = "Add a new task")
    private String addTask;
    
    @Override
    public Integer call() throws Exception {
        if (displayAll) {
            displayTodos();
        } else if (addTask != null) {
            addNewTask(addTask);
        }
        return 0;
    }
    
    private void displayTodos() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String url = String.format("http://localhost:8080/todos?limit=%d&offset=%d", limit, offset);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void addNewTask(String title) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
    
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/todos"))
            .header("Content-Type", "text/plain")
            .POST(HttpRequest.BodyPublishers.ofString(title))
            .build();
    
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
    
    public static void main(String[] args) {
        int exitCode = new CommandLine(new TodoCli()).execute(args);
        System.exit(exitCode);
    }
}
