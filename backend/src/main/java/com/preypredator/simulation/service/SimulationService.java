package com.preypredator.simulation.service;

import com.preypredator.simulation.domain.SimulationParameters;
import com.preypredator.simulation.domain.SimulationState;
import com.preypredator.simulation.engine.SimulationEngine;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimulationService {

    private final SimulationEngine simulationEngine;
    private final Map<UUID, SimulationState> simulations = new ConcurrentHashMap<>();

    public SimulationService(SimulationEngine simulationEngine) {
        this.simulationEngine = simulationEngine;
    }

    public SimulationState createSimulation(SimulationParameters parameters) {
        SimulationState simulation = simulationEngine.create(parameters);
        simulations.put(simulation.getId(), simulation);
        return simulation;
    }

    public SimulationState getSimulation(UUID id) {
        SimulationState simulation = simulations.get(id);

        if (simulation == null) {
            throw new SimulationNotFoundException(id);
        }

        return simulation;
    }

    public SimulationState stepSimulation(UUID id) {
        SimulationState simulation = getSimulation(id);
        return simulationEngine.step(simulation);
    }
}
