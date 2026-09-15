package com.preypredator.simulation.engine;

import com.preypredator.simulation.domain.SimulationParameters;
import com.preypredator.simulation.domain.SimulationState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimulationEngineTest {

    private final SimulationEngine simulationEngine = new SimulationEngine();

    @Test
    void createsSimulationWithInitialPopulation() {
        SimulationParameters parameters = SimulationParameters.defaults();

        SimulationState state = simulationEngine.create(parameters);

        assertThat(state.getStep()).isZero();
        assertThat(state.getRabbits()).hasSize(parameters.initialRabbits());
        assertThat(state.getFoxes()).hasSize(parameters.initialFoxes());
    }

    @Test
    void advancesSimulationByOneStep() {
        SimulationState state = simulationEngine.create(SimulationParameters.defaults());

        simulationEngine.step(state);

        assertThat(state.getStep()).isEqualTo(1);
    }

    @Test
    void defaultParametersKeepBothPopulationsAliveDuringShortDemoWindow() {
        SimulationState state = simulationEngine.create(SimulationParameters.defaults());

        for (int step = 0; step < 60; step++) {
            simulationEngine.step(state);
        }

        assertThat(state.getRabbits()).isNotEmpty();
        assertThat(state.getFoxes()).isNotEmpty();
    }

    @Test
    void defaultParametersKeepBothPopulationsAliveDuringCourseWindow() {
        SimulationParameters parameters = SimulationParameters.defaults();
        SimulationState state = simulationEngine.create(parameters);
        int peakFoxes = state.getFoxes().size();

        for (int step = 0; step < 1000; step++) {
            simulationEngine.step(state);
            peakFoxes = Math.max(peakFoxes, state.getFoxes().size());
        }

        assertThat(peakFoxes).isGreaterThan(parameters.initialFoxes());
        assertThat(state.getFoxes()).isNotEmpty();
        assertThat(state.getRabbits()).isNotEmpty();
    }

    @Test
    void higherFoxSpeedCanCollapseTheSystemByOverhunting() {
        SimulationParameters parameters = new SimulationParameters(
                60,
                60,
                200,
                15,
                1,
                9,
                60,
                5,
                1,
                10,
                10,
                5,
                7,
                10,
                30,
                10,
                20,
                40,
                80,
                42L
        );
        SimulationState state = simulationEngine.create(parameters);
        int peakFoxes = state.getFoxes().size();

        for (int step = 0; step < 240; step++) {
            simulationEngine.step(state);
            peakFoxes = Math.max(peakFoxes, state.getFoxes().size());
        }

        assertThat(peakFoxes).isGreaterThan(parameters.initialFoxes());
        assertThat(state.getFoxes()).isEmpty();
        assertThat(state.getRabbits()).isEmpty();
    }
}
