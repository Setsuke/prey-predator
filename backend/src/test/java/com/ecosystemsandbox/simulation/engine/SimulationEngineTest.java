package com.ecosystemsandbox.simulation.engine;

import com.ecosystemsandbox.simulation.domain.SimulationParameters;
import com.ecosystemsandbox.simulation.domain.SimulationState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimulationEngineTest {

    private final SimulationEngine simulationEngine = new SimulationEngine();

    @Test
    void createsSimulationWithInitialPopulation() {
        SimulationParameters parameters = SimulationParameters.defaults();

        SimulationState state = simulationEngine.create(parameters);

        assertThat(state.getStep()).isZero();
        assertThat(state.getPrey()).hasSize(parameters.initialPrey());
        assertThat(state.getPredators()).hasSize(parameters.initialPredators());
    }

    @Test
    void advancesSimulationByOneStep() {
        SimulationState state = simulationEngine.create(SimulationParameters.defaults());

        simulationEngine.step(state);

        assertThat(state.getStep()).isEqualTo(1);
    }
}
