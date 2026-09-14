# Ecosystem Sandbox

Interactive predator-prey simulation project built as a portfolio-friendly full-stack application.

The current implementation starts with a small, organised Spring Boot backend focused on the simulation engine and REST API.

## Backend

The backend models a simple ecosystem with prey, predators, food and refuge cells.

Current features:

- Create a new simulation with default or custom parameters
- Advance a simulation one step at a time
- Track prey and predator populations
- Return grid cells and animal positions for a future frontend
- Keep the code separated into API, service, engine and domain layers

## Tech Stack

- Java 17
- Spring Boot
- Maven
- JUnit 5

## Run

```bash
cd backend
mvn spring-boot:run
```

## Test

```bash
cd backend
mvn test
```

## API

```http
POST /api/simulations
GET /api/simulations/{id}
POST /api/simulations/{id}/steps
```

## Project Status

Backend MVP in progress. The next major step is to add an Angular frontend that visualises the grid and population changes over time.
