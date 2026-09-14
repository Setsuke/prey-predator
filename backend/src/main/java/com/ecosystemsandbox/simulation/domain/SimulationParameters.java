package com.ecosystemsandbox.simulation.domain;

public record SimulationParameters(
        int rows,
        int columns,
        int initialPrey,
        int initialPredators,
        int initialFood,
        int initialRefuges,
        int preyInitialEnergy,
        int predatorInitialEnergy,
        int preyReproductionEnergy,
        int predatorReproductionEnergy,
        int foodEnergy,
        int preyMoveCost,
        int predatorMoveCost,
        long seed
) {

    public static SimulationParameters defaults() {
        return new SimulationParameters(
                20,
                20,
                60,
                10,
                70,
                20,
                8,
                14,
                14,
                24,
                5,
                1,
                2,
                42L
        );
    }
}
