package com.ecosystemsandbox.simulation.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimulationState {

    private final UUID id;
    private final SimulationParameters parameters;
    private final CellType[][] grid;
    private final List<Prey> prey;
    private final List<Predator> predators;
    private int step;

    public SimulationState(UUID id, SimulationParameters parameters, CellType[][] grid, List<Prey> prey, List<Predator> predators) {
        this.id = id;
        this.parameters = parameters;
        this.grid = grid;
        this.prey = new ArrayList<>(prey);
        this.predators = new ArrayList<>(predators);
    }

    public UUID getId() {
        return id;
    }

    public SimulationParameters getParameters() {
        return parameters;
    }

    public CellType[][] getGrid() {
        return grid;
    }

    public List<Prey> getPrey() {
        return prey;
    }

    public List<Predator> getPredators() {
        return predators;
    }

    public int getStep() {
        return step;
    }

    public void advanceStep() {
        step++;
    }
}
