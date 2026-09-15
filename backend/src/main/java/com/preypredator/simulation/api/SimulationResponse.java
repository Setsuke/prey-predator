package com.preypredator.simulation.api;

import com.preypredator.simulation.domain.SimulationState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record SimulationResponse(
        UUID id,
        int step,
        int rows,
        int columns,
        int rabbitCount,
        int foxCount,
        int grassTotal,
        List<CellResponse> cells,
        List<AnimalResponse> animals
) {

    public static SimulationResponse from(SimulationState state) {
        List<CellResponse> cells = new ArrayList<>();
        int[][] grass = state.getGrass();
        int grassTotal = 0;

        for (int row = 0; row < grass.length; row++) {
            for (int column = 0; column < grass[row].length; column++) {
                grassTotal += grass[row][column];

                if (grass[row][column] > 0) {
                    cells.add(new CellResponse(row, column, grass[row][column]));
                }
            }
        }

        List<AnimalResponse> animals = new ArrayList<>();
        state.getRabbits().stream().map(AnimalResponse::from).forEach(animals::add);
        state.getFoxes().stream().map(AnimalResponse::from).forEach(animals::add);

        return new SimulationResponse(
                state.getId(),
                state.getStep(),
                state.getParameters().rows(),
                state.getParameters().columns(),
                state.getRabbits().size(),
                state.getFoxes().size(),
                grassTotal,
                cells,
                animals
        );
    }
}
