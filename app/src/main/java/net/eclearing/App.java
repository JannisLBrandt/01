package net.eclearing;

class App {
    public static void main(String[] args) {
	System.out.println("App main entry point!");

	Task task1 = new Task("Task1", "This is the body of task 1.");
	Task task2 = new Task("Task2", "This is the body of task 2.");

	//	System.out.printf("Task1's ID is %d\n", task1.id);
	//	System.out.printf("Task2's ID is %d\n", task2.id);

	List toDos = new List();
	
	toDos.tasks[0] = task1;

	System.out.printf("Task 1 %s %s\n", toDos.tasks[0].header, toDos.tasks[0].content);
    }
}
