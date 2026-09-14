package com.ecosystemsandbox.simulation.service;

import java.util.UUID;

public class SimulationNotFoundException extends RuntimeException {

    public SimulationNotFoundException(UUID id) {
        super("Simulation not found: " + id);
    }
}
