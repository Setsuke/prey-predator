package com.ecosystemsandbox.simulation.engine;

import com.ecosystemsandbox.simulation.domain.CellType;
import com.ecosystemsandbox.simulation.domain.Position;
import com.ecosystemsandbox.simulation.domain.Predator;
import com.ecosystemsandbox.simulation.domain.Prey;
import com.ecosystemsandbox.simulation.domain.SimulationParameters;
import com.ecosystemsandbox.simulation.domain.SimulationState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Component
public class SimulationEngine {

    public SimulationState create(SimulationParameters parameters) {
        validateParameters(parameters);

        Random random = new Random(parameters.seed());
        CellType[][] grid = createGrid(parameters, random);
        List<Prey> prey = createPrey(parameters, random);
        List<Predator> predators = createPredators(parameters, random);

        return new SimulationState(UUID.randomUUID(), parameters, grid, prey, predators);
    }

    public SimulationState step(SimulationState state) {
        Random random = new Random(state.getParameters().seed() + state.getStep() + 1);

        movePrey(state, random);
        movePredators(state, random);
        removeDeadAnimals(state);
        growFood(state, random);
        state.advanceStep();

        return state;
    }

    private CellType[][] createGrid(SimulationParameters parameters, Random random) {
        CellType[][] grid = new CellType[parameters.rows()][parameters.columns()];

        for (int row = 0; row < parameters.rows(); row++) {
            for (int column = 0; column < parameters.columns(); column++) {
                grid[row][column] = CellType.EMPTY;
            }
        }

        placeCells(grid, CellType.FOOD, parameters.initialFood(), random);
        placeCells(grid, CellType.REFUGE, parameters.initialRefuges(), random);
        return grid;
    }

    private void validateParameters(SimulationParameters parameters) {
        int cells = parameters.rows() * parameters.columns();
        int terrainCells = parameters.initialFood() + parameters.initialRefuges();

        if (terrainCells > cells) {
            throw new IllegalArgumentException("Food and refuge cells cannot exceed the grid size.");
        }
    }

    private void placeCells(CellType[][] grid, CellType type, int count, Random random) {
        int placed = 0;

        while (placed < count) {
            Position position = randomPosition(grid, random);

            if (cellAt(grid, position) == CellType.EMPTY) {
                grid[position.row()][position.column()] = type;
                placed++;
            }
        }
    }

    private List<Prey> createPrey(SimulationParameters parameters, Random random) {
        List<Prey> prey = new ArrayList<>();

        for (int index = 0; index < parameters.initialPrey(); index++) {
            prey.add(new Prey(randomPosition(parameters, random), parameters.preyInitialEnergy()));
        }

        return prey;
    }

    private List<Predator> createPredators(SimulationParameters parameters, Random random) {
        List<Predator> predators = new ArrayList<>();

        for (int index = 0; index < parameters.initialPredators(); index++) {
            predators.add(new Predator(randomPosition(parameters, random), parameters.predatorInitialEnergy()));
        }

        return predators;
    }

    private void movePrey(SimulationState state, Random random) {
        List<Prey> newborns = new ArrayList<>();

        for (Prey prey : state.getPrey()) {
            Position nextPosition = randomNeighbor(prey.getPosition(), state.getParameters(), random);
            prey.moveTo(nextPosition);
            prey.spendEnergy(state.getParameters().preyMoveCost());

            if (cellAt(state.getGrid(), nextPosition) == CellType.FOOD) {
                prey.gainEnergy(state.getParameters().foodEnergy());
                state.getGrid()[nextPosition.row()][nextPosition.column()] = CellType.EMPTY;
            }

            if (prey.getEnergy() >= state.getParameters().preyReproductionEnergy()) {
                prey.spendEnergy(state.getParameters().preyInitialEnergy());
                newborns.add(new Prey(prey.getPosition(), state.getParameters().preyInitialEnergy()));
            }
        }

        state.getPrey().addAll(newborns);
    }

    private void movePredators(SimulationState state, Random random) {
        List<Predator> newborns = new ArrayList<>();

        for (Predator predator : state.getPredators()) {
            Position nextPosition = findNearestPrey(predator.getPosition(), state.getPrey())
                    .map(preyPosition -> moveTowards(predator.getPosition(), preyPosition, state.getParameters()))
                    .orElseGet(() -> randomNeighbor(predator.getPosition(), state.getParameters(), random));

            if (cellAt(state.getGrid(), nextPosition) != CellType.REFUGE) {
                predator.moveTo(nextPosition);
            }

            predator.spendEnergy(state.getParameters().predatorMoveCost());
            huntPreyAt(predator, state.getPrey(), state.getParameters());

            if (predator.getEnergy() >= state.getParameters().predatorReproductionEnergy()) {
                predator.spendEnergy(state.getParameters().predatorInitialEnergy());
                newborns.add(new Predator(predator.getPosition(), state.getParameters().predatorInitialEnergy()));
            }
        }

        state.getPredators().addAll(newborns);
    }

    private Optional<Position> findNearestPrey(Position predatorPosition, List<Prey> prey) {
        return prey.stream()
                .map(Prey::getPosition)
                .filter(position -> distance(predatorPosition, position) <= 2)
                .min((first, second) -> Integer.compare(distance(predatorPosition, first), distance(predatorPosition, second)));
    }

    private void huntPreyAt(Predator predator, List<Prey> prey, SimulationParameters parameters) {
        Iterator<Prey> iterator = prey.iterator();

        while (iterator.hasNext()) {
            Prey target = iterator.next();

            if (target.getPosition().equals(predator.getPosition())) {
                iterator.remove();
                predator.gainEnergy(parameters.preyInitialEnergy());
                return;
            }
        }
    }

    private void removeDeadAnimals(SimulationState state) {
        state.getPrey().removeIf(prey -> !prey.isAlive());
        state.getPredators().removeIf(predator -> !predator.isAlive());
    }

    private void growFood(SimulationState state, Random random) {
        if (random.nextDouble() < 0.25) {
            Position position = randomPosition(state.getParameters(), random);

            if (cellAt(state.getGrid(), position) == CellType.EMPTY) {
                state.getGrid()[position.row()][position.column()] = CellType.FOOD;
            }
        }
    }

    private Position randomNeighbor(Position origin, SimulationParameters parameters, Random random) {
        int rowDelta = random.nextInt(3) - 1;
        int columnDelta = random.nextInt(3) - 1;
        Position next = origin.moveBy(rowDelta, columnDelta);

        return clamp(next, parameters);
    }

    private Position moveTowards(Position origin, Position target, SimulationParameters parameters) {
        int rowDelta = Integer.compare(target.row(), origin.row());
        int columnDelta = Integer.compare(target.column(), origin.column());
        return clamp(origin.moveBy(rowDelta, columnDelta), parameters);
    }

    private Position clamp(Position position, SimulationParameters parameters) {
        int row = Math.max(0, Math.min(parameters.rows() - 1, position.row()));
        int column = Math.max(0, Math.min(parameters.columns() - 1, position.column()));
        return new Position(row, column);
    }

    private Position randomPosition(SimulationParameters parameters, Random random) {
        return new Position(random.nextInt(parameters.rows()), random.nextInt(parameters.columns()));
    }

    private Position randomPosition(CellType[][] grid, Random random) {
        return new Position(random.nextInt(grid.length), random.nextInt(grid[0].length));
    }

    private CellType cellAt(CellType[][] grid, Position position) {
        return grid[position.row()][position.column()];
    }

    private int distance(Position first, Position second) {
        return Math.abs(first.row() - second.row()) + Math.abs(first.column() - second.column());
    }
}
