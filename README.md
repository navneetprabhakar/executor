# Executor

A Spring Boot demo that processes CSV uploads using Java's `ExecutorService` and Spring's `ThreadPoolTaskExecutor`, exposing REST endpoints that run the same workload single-threaded, multi-threaded (parallel), and asynchronously with task-status polling.

## Features

- Single-threaded (synchronous) CSV processing endpoint
- Multi-threaded parallel CSV processing endpoint
- Asynchronous CSV processing with a task id returned immediately
- Task-status polling endpoint backed by a database table
- Configurable `ThreadPoolTaskExecutor` (core/max pool size, queue capacity)
- CSV parsing via univocity-parsers and persistence via Spring Data JPA

## Tech Stack

- Java 17
- Spring Boot 2.7.18 (`spring-boot-starter-web`, `spring-boot-starter-data-jpa`)
- univocity-parsers 2.9.1 (CSV parsing)
- MySQL (mysql-connector-j) with HikariCP
- Lombok
- Gradle

## Getting Started

### Prerequisites

- JDK 17
- A running MySQL instance with a schema matching the configured datasource URL

### Build and Run

```bash
./gradlew bootRun
```

The application starts on port `8080` with context path `/executor`, so endpoints are served under `http://localhost:8080/executor`.

To build a jar:

```bash
./gradlew build
```

## Configuration

Configuration lives in `src/main/resources/application.properties`:

```properties
server.port=8080
server.servlet.context-path=/executor

spring.datasource.url=jdbc:mysql://<DB_HOST>:<DB_PORT>/<DB_NAME>?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=<DB_USERNAME>
spring.datasource.password=<DB_PASSWORD>
spring.jpa.hibernate.ddl-auto=update

## Thread pool executor configs
thread.executor.core.pool.size=4
thread.executor.max.pool.size=6
thread.executor.queue.capacity=20
```

## Usage

All upload endpoints accept a JSON body wrapping an `UploadRequest` that carries the CSV file `path`:

```json
{
  "data": {
    "path": "/absolute/path/to/file.csv"
  }
}
```

### Endpoints

- `POST /executor/v1/task/upload/single` — process the CSV in a single thread (synchronous)
- `POST /executor/v1/task/upload/multi` — process the CSV across multiple parallel threads (synchronous)
- `POST /executor/v1/task/upload/async` — process the CSV asynchronously; returns a task id
- `GET /executor/v1/task/status?task_id=<id>` — poll the status of an asynchronous task
