# Prey-Predator Backend

Simple Spring Boot backend for an interactive rabbit-fox-grass simulation.

The simulation follows a lab-style agent model: rabbits move through a grid and eat grass, foxes move faster and hunt rabbits, and grass regrows up to a maximum level.

## Stack

- Java 17
- Spring Boot
- Maven
- JUnit 5

## API

Create a simulation with default parameters:

```http
POST /api/simulations
```

Example:

```bash
curl -X POST http://localhost:8080/api/simulations
```

Get a simulation:

```http
GET /api/simulations/{id}
```

Advance a simulation by one step:

```http
POST /api/simulations/{id}/steps
```

Example:

```bash
curl -X POST http://localhost:8080/api/simulations/{id}/steps
```

## Structure

```text
com.preypredator
├── common
│   └── api
└── simulation
    ├── api
    ├── domain
    ├── engine
    └── service
```

## Run

```bash
mvn spring-boot:run
```

## Test

```bash
mvn test
```
