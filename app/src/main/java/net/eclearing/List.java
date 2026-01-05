package net.eclearing;

class List {
    // max amount of tasks per list
    final int MAX_TASKS = 10;
    Task[] tasks = null;

    List() {
	this.tasks = new Task[MAX_TASKS];
    }
}
