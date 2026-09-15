package com.preypredator.simulation.domain;

public record SimulationParameters(
        int rows,
        int columns,
        int initialRabbits,
        int initialFoxes,
        int rabbitSpeed,
        int foxSpeed,
        int grassGrowthRate,
        int maxGrass,
        int startGrass,
        int rabbitInitialFood,
        int foxInitialFood,
        int rabbitVision,
        int foxVision,
        int rabbitBreedFrequency,
        int foxBreedFrequency,
        int rabbitBreedFood,
        int foxBreedFood,
        int rabbitMaxAge,
        int foxMaxAge,
        long seed
) {

    public static SimulationParameters defaults() {
        return new SimulationParameters(
                60,
                60,
                200,
                15,
                1,
                3,
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
    }
}
