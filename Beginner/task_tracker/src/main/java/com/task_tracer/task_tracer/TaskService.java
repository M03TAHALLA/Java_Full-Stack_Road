package com.task_tracer.task_tracer;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private final List<TaskItem> tasks = new ArrayList<>();
    private long nextId = 1;

    public long addTask(String description) {
        validateDescription(description);

        LocalDateTime now = LocalDateTime.now();
        TaskItem task = new TaskItem(nextId++, description, "todo", now, now);
        tasks.add(task);

        return task.id;
    }

    public void updateTask(long id, String description) {
        validateDescription(description);

        TaskItem task = findById(id);
        task.description = description;
        task.updatedAt = LocalDateTime.now();
    }

    public void deleteTask(long id) {
        TaskItem task = findById(id);
        tasks.remove(task);
    }

    public void markInProgress(long id) {
        TaskItem task = findById(id);
        task.status = "in-progress";
        task.updatedAt = LocalDateTime.now();
    }

    public void markDone(long id) {
        TaskItem task = findById(id);
        task.status = "done";
        task.updatedAt = LocalDateTime.now();
    }

    public List<TaskItem> listTasks(String filter) {
        String normalizedFilter = normalizeFilter(filter);
        if (normalizedFilter.equals("all")) {
            return new ArrayList<>(tasks);
        }

        List<TaskItem> result = new ArrayList<>();
        for (TaskItem task : tasks) {
            if (task.status.equals(normalizedFilter)) {
                result.add(task);
            }
        }

        return result;
    }

    private String normalizeFilter(String filter) {
        String value = filter == null ? "all" : filter.trim().toLowerCase();
        if (value.isEmpty()) {
            value = "all";
        }

        if (!value.equals("all") && !value.equals("todo") && !value.equals("in-progress") && !value.equals("done")) {
            throw new IllegalArgumentException("Invalid filter.");
        }
        return value;
    }

    private TaskItem findById(long id) {
        for (TaskItem task : tasks) {
            if (task.id == id) {
                return task;
            }
        }

        throw new IllegalArgumentException("Task with ID " + id + " not found.");
    }

    private void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
    }

    public static class TaskItem {
        private final long id;
        private String description;
        private String status;
        private final LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private TaskItem(long id, String description, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.description = description;
            this.status = status;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public long getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String getStatus() {
            return status;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }
}
