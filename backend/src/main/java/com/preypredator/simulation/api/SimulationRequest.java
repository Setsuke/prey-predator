package com.preypredator.simulation.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record SimulationRequest(
        @Min(5) @Max(80) int rows,
        @Min(5) @Max(80) int columns,
        @Min(1) @Max(1000) int initialRabbits,
        @Min(1) @Max(200) int initialFoxes,
        @Min(1) @Max(10) int rabbitSpeed,
        @Min(1) @Max(10) int foxSpeed,
        @Min(1) @Max(500) int grassGrowthRate,
        @Min(1) @Max(20) int maxGrass,
        @Min(0) @Max(20) int startGrass,
        @Min(1) int rabbitInitialFood,
        @Min(1) int foxInitialFood,
        @Min(1) int rabbitVision,
        @Min(1) int foxVision,
        @Min(0) int rabbitBreedFrequency,
        @Min(0) int foxBreedFrequency,
        @Min(1) int rabbitBreedFood,
        @Min(1) int foxBreedFood,
        @Min(1) int rabbitMaxAge,
        @Min(1) int foxMaxAge,
        long seed
) {
}
