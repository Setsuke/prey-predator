package com.preypredator.simulation.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SimulationState {

    private final UUID id;
    private final SimulationParameters parameters;
    private final int[][] grass;
    private final List<Rabbit> rabbits;
    private final List<Fox> foxes;
    private final Random random;
    private int step;

    public SimulationState(UUID id, SimulationParameters parameters, int[][] grass, List<Rabbit> rabbits, List<Fox> foxes, Random random) {
        this.id = id;
        this.parameters = parameters;
        this.grass = grass;
        this.rabbits = new ArrayList<>(rabbits);
        this.foxes = new ArrayList<>(foxes);
        this.random = random;
    }

    public UUID getId() {
        return id;
    }

    public SimulationParameters getParameters() {
        return parameters;
    }

    public int[][] getGrass() {
        return grass;
    }

    public List<Rabbit> getRabbits() {
        return rabbits;
    }

    public List<Fox> getFoxes() {
        return foxes;
    }

    public Random getRandom() {
        return random;
    }

    public int getStep() {
        return step;
    }

    public void advanceStep() {
        step++;
    }
}
