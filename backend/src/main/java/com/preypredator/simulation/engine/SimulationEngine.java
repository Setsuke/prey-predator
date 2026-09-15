package com.preypredator.simulation.engine;

import com.preypredator.simulation.domain.Fox;
import com.preypredator.simulation.domain.Position;
import com.preypredator.simulation.domain.Rabbit;
import com.preypredator.simulation.domain.SimulationParameters;
import com.preypredator.simulation.domain.SimulationState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Component
public class SimulationEngine {

    public SimulationState create(SimulationParameters parameters) {
        validateParameters(parameters);

        Random random = new Random(parameters.seed());
        int[][] grass = createGrass(parameters);
        List<Rabbit> rabbits = createRabbits(parameters, random);
        List<Fox> foxes = createFoxes(parameters, random);

        return new SimulationState(UUID.randomUUID(), parameters, grass, rabbits, foxes, random);
    }

    public SimulationState step(SimulationState state) {
        Random random = state.getRandom();

        applyRabbitRules(state, random);
        applyFoxRules(state, random);
        removeDeadAnimals(state);
        growGrass(state, random);
        state.advanceStep();

        return state;
    }

    private void validateParameters(SimulationParameters parameters) {
        if (parameters.startGrass() > parameters.maxGrass()) {
            throw new IllegalArgumentException("Start grass cannot be greater than max grass.");
        }
    }

    private int[][] createGrass(SimulationParameters parameters) {
        int[][] grass = new int[parameters.rows()][parameters.columns()];

        for (int row = 0; row < parameters.rows(); row++) {
            for (int column = 0; column < parameters.columns(); column++) {
                grass[row][column] = parameters.startGrass();
            }
        }

        return grass;
    }

    private List<Rabbit> createRabbits(SimulationParameters parameters, Random random) {
        List<Rabbit> rabbits = new ArrayList<>();

        for (int index = 0; index < parameters.initialRabbits(); index++) {
            rabbits.add(new Rabbit(
                    randomPosition(parameters, random),
                    random.nextInt(parameters.rabbitMaxAge()),
                    parameters.rabbitInitialFood(),
                    parameters.rabbitSpeed(),
                    0
            ));
        }

        return rabbits;
    }

    private List<Fox> createFoxes(SimulationParameters parameters, Random random) {
        List<Fox> foxes = new ArrayList<>();

        for (int index = 0; index < parameters.initialFoxes(); index++) {
            foxes.add(new Fox(
                    randomPosition(parameters, random),
                    random.nextInt(parameters.foxMaxAge()),
                    parameters.foxInitialFood(),
                    parameters.foxSpeed(),
                    0
            ));
        }

        return foxes;
    }

    private void applyRabbitRules(SimulationState state, Random random) {
        List<Rabbit> newborns = new ArrayList<>();

        for (Rabbit rabbit : state.getRabbits()) {
            moveRabbit(rabbit, state, random);
            eatGrass(rabbit, state);
            breedRabbit(rabbit, state.getParameters()).ifPresent(newborns::add);
        }

        state.getRabbits().addAll(newborns);
    }

    private void applyFoxRules(SimulationState state, Random random) {
        List<Fox> newborns = new ArrayList<>();

        for (Fox fox : state.getFoxes()) {
            moveFox(fox, state.getParameters(), random);
            eatRabbit(fox, state, random);
            breedFox(fox, state.getParameters()).ifPresent(newborns::add);
        }

        state.getFoxes().addAll(newborns);
    }

    private void moveRabbit(Rabbit rabbit, SimulationState state, Random random) {
        if (grassAt(rabbit.getPosition(), state) > 0) {
            return;
        }

        Optional<Position> grassPosition = findGrassPosition(rabbit.getPosition(), state, random);

        if (grassPosition.isPresent()) {
            rabbit.moveTo(moveTowards(rabbit.getPosition(), grassPosition.get(), rabbit.getSpeed(), state.getParameters()));
            return;
        }

        rabbit.moveTo(randomDirectionalMove(rabbit.getPosition(), rabbit.getSpeed(), state.getParameters(), random));
    }

    private Optional<Position> findGrassPosition(Position origin, SimulationState state, Random random) {
        List<Position> candidates = new ArrayList<>();
        int bestGrass = 0;
        int vision = state.getParameters().rabbitVision();

        for (int row = origin.row() - vision; row <= origin.row() + vision; row++) {
            for (int column = origin.column() - vision; column <= origin.column() + vision; column++) {
                if (!isInside(row, column, state.getParameters())) {
                    continue;
                }

                int rowDelta = row - origin.row();
                int columnDelta = column - origin.column();

                if (rowDelta * rowDelta + columnDelta * columnDelta > vision * vision) {
                    continue;
                }

                int grassLevel = state.getGrass()[row][column];

                if (grassLevel > bestGrass) {
                    bestGrass = grassLevel;
                    candidates.clear();
                    candidates.add(new Position(row, column));
                } else if (grassLevel == bestGrass && grassLevel > 0) {
                    candidates.add(new Position(row, column));
                }
            }
        }

        if (candidates.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(candidates.get(random.nextInt(candidates.size())));
    }

    private void eatGrass(Rabbit rabbit, SimulationState state) {
        Position position = rabbit.getPosition();

        if (grassAt(position, state) > 0) {
            state.getGrass()[position.row()][position.column()]--;
            rabbit.addFood(1);
        } else {
            rabbit.reduceFood(1);
        }
    }

    private void moveFox(Fox fox, SimulationParameters parameters, Random random) {
        fox.moveTo(randomDirectionalMove(fox.getPosition(), fox.getSpeed(), parameters, random));
    }

    private void eatRabbit(Fox fox, SimulationState state, Random random) {
        Optional<Rabbit> target = nearestRabbit(fox.getPosition(), state);

        if (target.isEmpty()) {
            return;
        }

        double distance = distance(fox.getPosition(), target.get().getPosition());

        if (distance < fox.getSpeed()) {
            double killProbability = 1 - (distance / fox.getSpeed());

            if (killProbability > random.nextDouble()) {
                fox.moveTo(target.get().getPosition());
                target.get().markEaten();
                fox.addFood(2);
            }
        }
    }

    private Optional<Rabbit> nearestRabbit(Position position, SimulationState state) {
        int vision = state.getParameters().foxVision();

        return state.getRabbits().stream()
                .filter(rabbit -> !rabbit.isEaten())
                .filter(rabbit -> distanceSquared(position, rabbit.getPosition()) < vision * vision)
                .min(Comparator.comparingDouble(rabbit -> distanceSquared(position, rabbit.getPosition())));
    }

    private Optional<Rabbit> breedRabbit(Rabbit rabbit, SimulationParameters parameters) {
        Optional<Rabbit> newborn = Optional.empty();

        if (rabbit.getLastBreed() > parameters.rabbitBreedFrequency()
                && rabbit.getFood() > parameters.rabbitBreedFood()) {
            rabbit.setLastBreed(-1);
            double sharedFood = rabbit.getFood() / 2.0;
            rabbit.halveFood();
            newborn = Optional.of(new Rabbit(rabbit.getPosition(), 0, sharedFood, rabbit.getSpeed(), 10));
        }

        rabbit.increaseAge();
        rabbit.increaseLastBreed();
        return newborn;
    }

    private Optional<Fox> breedFox(Fox fox, SimulationParameters parameters) {
        Optional<Fox> newborn = Optional.empty();

        if (fox.getLastBreed() > parameters.foxBreedFrequency()
                && fox.getFood() > parameters.foxBreedFood()) {
            fox.setLastBreed(-1);
            double sharedFood = fox.getFood() / 2.0;
            fox.halveFood();
            newborn = Optional.of(new Fox(fox.getPosition(), 0, sharedFood, fox.getSpeed(), 10));
        }

        fox.increaseAge();
        fox.increaseLastBreed();
        return newborn;
    }

    private Position randomDirectionalMove(Position origin, int speed, SimulationParameters parameters, Random random) {
        double direction = random.nextDouble() * 2 * Math.PI;
        int rowDelta = (int) Math.round(Math.cos(direction) * speed);
        int columnDelta = (int) Math.round(Math.sin(direction) * speed);
        return checkedPosition(origin.moveBy(rowDelta, columnDelta), parameters);
    }

    private Position moveTowards(Position origin, Position target, int speed, SimulationParameters parameters) {
        int rowDelta = target.row() - origin.row();
        int columnDelta = target.column() - origin.column();
        double distance = Math.sqrt(rowDelta * rowDelta + columnDelta * columnDelta);

        if (distance < speed) {
            return checkedPosition(target, parameters);
        }

        int nextRow = (int) Math.round(origin.row() + (rowDelta / distance) * speed);
        int nextColumn = (int) Math.round(origin.column() + (columnDelta / distance) * speed);
        return checkedPosition(new Position(nextRow, nextColumn), parameters);
    }

    private void growGrass(SimulationState state, Random random) {
        SimulationParameters parameters = state.getParameters();

        for (int index = 0; index < parameters.grassGrowthRate(); index++) {
            Position location = randomPosition(parameters, random);

            if (state.getGrass()[location.row()][location.column()] < parameters.maxGrass()) {
                state.getGrass()[location.row()][location.column()]++;
            }
        }
    }

    private void removeDeadAnimals(SimulationState state) {
        SimulationParameters parameters = state.getParameters();
        state.getRabbits().removeIf(rabbit ->
                rabbit.getFood() <= 0 || rabbit.getAge() > parameters.rabbitMaxAge() || rabbit.isEaten()
        );
        state.getFoxes().removeIf(fox ->
                fox.getFood() <= 0 || fox.getAge() > parameters.foxMaxAge()
        );
    }

    private int grassAt(Position position, SimulationState state) {
        return state.getGrass()[position.row()][position.column()];
    }

    private boolean isInside(int row, int column, SimulationParameters parameters) {
        return row >= 0 && column >= 0 && row < parameters.rows() && column < parameters.columns();
    }

    private Position checkedPosition(Position position, SimulationParameters parameters) {
        int row = Math.max(0, Math.min(parameters.rows() - 1, position.row()));
        int column = Math.max(0, Math.min(parameters.columns() - 1, position.column()));
        return new Position(row, column);
    }

    private double distance(Position first, Position second) {
        return Math.sqrt(distanceSquared(first, second));
    }

    private int distanceSquared(Position first, Position second) {
        int rowDelta = first.row() - second.row();
        int columnDelta = first.column() - second.column();
        return rowDelta * rowDelta + columnDelta * columnDelta;
    }

    private Position randomPosition(SimulationParameters parameters, Random random) {
        return new Position(random.nextInt(parameters.rows()), random.nextInt(parameters.columns()));
    }
}
