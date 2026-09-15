# Prey-Predator

A small full-stack simulation based on the MSc Advanced Computer Science course **Modelling and Simulation of Natural Systems**.

The project reworks a course lab on agent-based modelling into a simple Spring Boot API and Angular UI. It is not intended to be a complex ecological model; it is a clear implementation of the basic rabbit, fox and grass rules used in the lab.

## What the simulation is based on

The model is based on a grid environment with three main parts:

- **Grass** grows on grid cells up to a maximum value.
- **Rabbits** move around the grid, eat grass, reproduce when they have enough food, and die from hunger, age or being eaten.
- **Foxes** move around the grid, look for nearby rabbits, hunt with a distance-based probability, reproduce when they have enough food, and die from hunger or age.

The default values follow the lab setup:

- Grid size: `60 x 60`
- Rabbits: `200`
- Foxes: `15`
- Rabbit speed: `1`
- Fox speed: `3`
- Grass growth rate: `60`
- Maximum grass per cell: `5`
- Starting grass per cell: `1`

## Backend

The backend contains the simulation rules and exposes a small REST API.

Main responsibilities:

- Create a simulation with default or custom parameters.
- Advance the simulation one step at a time.
- Track rabbit, fox and grass totals.
- Return grass cells and animal positions for the frontend.
- Keep the code separated into API, service, engine and domain layers.

## Frontend

The frontend is a simple dashboard for running and observing the simulation.

It includes:

- Controls for the main parameters.
- Run, pause, reset and step actions.
- A grid view showing grass levels and animal positions.
- Counters for rabbits, foxes and grass.
- A trend chart for comparing population changes over time.

## Tech Stack

- Java 17
- Spring Boot
- Maven
- JUnit 5
- Angular
- TypeScript

## Run Backend

```bash
cd backend
mvn spring-boot:run
```

## Run Frontend

```bash
cd frontend
npm install
npm start
```

If PowerShell blocks `npm`, use:

```bash
npm.cmd start
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
