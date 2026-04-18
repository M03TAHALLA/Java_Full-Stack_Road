# Task Tracker - Spring Boot (Beginner Practice)

## Submit Solution URL
Public GitHub repository URL:

**https://github.com/M03TAHALLA/Java_Full-Stack_Road/tree/main/Beginner/task_tracker**

## Project Overview
This project is a beginner-friendly Task Tracker application built with Spring Boot and Java.
It runs in the terminal (CLI) and lets you manage tasks with statuses:

- `todo`
- `in-progress`
- `done`

The app demonstrates core Spring Boot concepts:

- `@SpringBootApplication` bootstrapping
- Dependency Injection (constructor injection)
- `@Component` and `@Service` usage
- Clean separation between controller and business logic

## Features
- Add a task
- Update a task description
- Delete a task
- Mark task as in-progress
- Mark task as done
- List tasks by filter (`all`, `todo`, `in-progress`, `done`)
- Input validation and friendly error handling

## Tech Stack
- Java 17
- Spring Boot 4.0.5
- Maven

## Project Structure

```text
task_tracker/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/task_tracer/task_tracer/
    │   │   ├── TaskTracerApplication.java
    │   │   ├── TaskController.java
    │   │   └── TaskService.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/task_tracer/task_tracer/
            └── TaskTracerApplicationTests.java
```

## How to Run
From the project root (`Beginner/task_tracker`):

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

## Menu Operations
When the app starts, you will see:

- `1` Add task
- `2` Update task
- `3` Delete task
- `4` Mark task as in-progress
- `5` Mark task as done
- `6` List tasks
- `0` Exit

## Example Flow
1. Add a task with option `1`
2. List all tasks with option `6` and filter `all`
3. Mark task in-progress using option `4`
4. Mark task done using option `5`

## Notes
- Data is stored in memory using `ArrayList`, so tasks are reset when the app restarts.
- This is a practice project focused on Spring Boot fundamentals.

## Author
Mohammed Tahalla
