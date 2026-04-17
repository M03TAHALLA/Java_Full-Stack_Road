package com.task_tracer.task_tracer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class TaskController implements CommandLineRunner {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void run(String... args) {
        startInteractiveMode();
    }

    public void startInteractiveMode() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            System.out.print("Choose option: ");
            if (!scanner.hasNextLine()) {
                System.out.println("Goodbye.");
                return;
            }
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> addTask(scanner);
                    case "2" -> updateTask(scanner);
                    case "3" -> deleteTask(scanner);
                    case "4" -> markInProgress(scanner);
                    case "5" -> markDone(scanner);
                    case "6" -> listTasks(scanner);
                    case "0" -> {
                        System.out.println("Goodbye.");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please choose from 0 to 6.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Error: " + ex.getMessage());
            }

            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("===== Task Tracker =====");
        System.out.println("1) Add task");
        System.out.println("2) Update task");
        System.out.println("3) Delete task");
        System.out.println("4) Mark task as in-progress");
        System.out.println("5) Mark task as done");
        System.out.println("6) List tasks");
        System.out.println("0) Exit");
    }

    private void addTask(Scanner scanner) {
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        long id = taskService.addTask(description);
        System.out.println("Task added successfully (ID: " + id + ")");
    }

    private void updateTask(Scanner scanner) {
        long id = askTaskId(scanner);
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();
        taskService.updateTask(id, description);

        System.out.println("Task updated successfully.");
    }

    private void deleteTask(Scanner scanner) {
        long id = askTaskId(scanner);
        taskService.deleteTask(id);
        System.out.println("Task deleted successfully.");
    }

    private void markInProgress(Scanner scanner) {
        long id = askTaskId(scanner);
        taskService.markInProgress(id);
        System.out.println("Task marked as in-progress.");
    }

    private void markDone(Scanner scanner) {
        long id = askTaskId(scanner);
        taskService.markDone(id);
        System.out.println("Task marked as done.");
    }

    private void listTasks(Scanner scanner) {
        System.out.print("Filter (all/todo/in-progress/done): ");
        String filter = scanner.nextLine();

        List<TaskService.TaskItem> tasks = taskService.listTasks(filter);
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.printf("%-4s %-12s %-30s %-22s %-22s%n", "ID", "STATUS", "DESCRIPTION", "CREATED AT", "UPDATED AT");
        for (TaskService.TaskItem task : tasks) {
            System.out.printf(
                    "%-4d %-12s %-30s %-22s %-22s%n",
                    task.getId(),
                    task.getStatus(),
                    task.getDescription(),
                    task.getCreatedAt(),
                    task.getUpdatedAt()
            );
        }
    }

    private long askTaskId(Scanner scanner) {
        System.out.print("Enter task ID: ");
        String value = scanner.nextLine().trim();

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Task ID must be a number.");
        }
    }
}
