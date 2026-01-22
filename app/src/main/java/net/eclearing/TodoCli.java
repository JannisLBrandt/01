package net.eclearing;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Callable;
import com.alibaba.fastjson2.JSON;
import de.vandermeer.asciitable.AsciiTable;
import java.util.List;

@Command(name = "todo", mixinStandardHelpOptions = true, version = "1.0",
    description = "CLI for managing todos")
public class TodoCli implements Callable<Integer> {
    
    @Option(names = {"--display"}, description = "Display todos with defaults to limit and offset")
    private boolean display;
    
    @Option(names = {"--limit"}, description = "Number of items to display", defaultValue = "10")
    private int limit;
    
    @Option(names = {"--offset"}, description = "Offset for pagination", defaultValue = "0")
    private int offset;

    @Option(names = {"--add"}, description = "Add a new task")
    private String addTask;

    @Option(names = {"--delete"}, description = "Mark a task as delete by ID")
    private Long deleteId;

    @Option(names = {"--complete"}, description = "Mark a task completed by ID")
    private Long completeId;
    
    @Override
    public Integer call() throws Exception {
        if (display) {
            displayTodos();
        } else if (addTask != null) {
            addNewTask(addTask);
        } else if (deleteId != null) {
            deleteTask(deleteId);
        } else if (completeId != null) {
            completeTask(completeId);
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
    
        // Parse JSON to List<Task>
        List<Task> tasks = JSON.parseArray(response.body(), Task.class);
    
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
    
        // Create ASCII table
        AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow("ID", "Title", "Created At", "Modified At", "Completed At");
        table.addRule();
    
        for (Task task : tasks) {
            table.addRow(
                task.id(),
                task.title(),
                task.createdAt() != null ? task.createdAt().toString() : "",
                task.modifiedAt() != null ? task.modifiedAt().toString() : "",
                task.completedAt() != null ? task.completedAt().toString() : ""
            );
            table.addRule();
        }
    
        System.out.println(table.render());
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

    private void deleteTask(long id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
    
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/todos?id=" + id))
            .DELETE()
            .build();
    
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void completeTask(long id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/todos?id=" + id))
            .PUT(HttpRequest.BodyPublishers.noBody())
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
    
    public static void main(String[] args) {
        int exitCode = new CommandLine(new TodoCli()).execute(args);
        System.exit(exitCode);
    }
}
