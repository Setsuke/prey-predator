package com.ecosystemsandbox.simulation.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record SimulationRequest(
        @Min(5) @Max(80) int rows,
        @Min(5) @Max(80) int columns,
        @Min(1) @Max(500) int initialPrey,
        @Min(1) @Max(100) int initialPredators,
        @Min(0) @Max(1000) int initialFood,
        @Min(0) @Max(1000) int initialRefuges,
        @Min(1) int preyInitialEnergy,
        @Min(1) int predatorInitialEnergy,
        @Min(2) int preyReproductionEnergy,
        @Min(2) int predatorReproductionEnergy,
        @Min(1) int foodEnergy,
        @Min(1) int preyMoveCost,
        @Min(1) int predatorMoveCost,
        long seed
) {
}
