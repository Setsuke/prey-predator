package com.preypredator.simulation.service;

import com.preypredator.simulation.domain.SimulationParameters;
import com.preypredator.simulation.domain.SimulationState;
import com.preypredator.simulation.engine.SimulationEngine;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SimulationServiceTest {

    private final SimulationService simulationService = new SimulationService(new SimulationEngine());

    @Test
    void storesCreatedSimulation() {
        SimulationState created = simulationService.createSimulation(SimulationParameters.defaults());

        SimulationState found = simulationService.getSimulation(created.getId());

        assertThat(found).isSameAs(created);
    }

    @Test
    void throwsWhenSimulationDoesNotExist() {
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> simulationService.getSimulation(id))
                .isInstanceOf(SimulationNotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}
