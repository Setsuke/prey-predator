package com.ecosystemsandbox.simulation.api;

import com.ecosystemsandbox.simulation.domain.CellType;
import com.ecosystemsandbox.simulation.domain.SimulationState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record SimulationResponse(
        UUID id,
        int step,
        int rows,
        int columns,
        int preyCount,
        int predatorCount,
        List<CellResponse> cells,
        List<AnimalResponse> animals
) {

    public static SimulationResponse from(SimulationState state) {
        List<CellResponse> cells = new ArrayList<>();
        CellType[][] grid = state.getGrid();

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column] != CellType.EMPTY) {
                    cells.add(new CellResponse(row, column, grid[row][column]));
                }
            }
        }

        List<AnimalResponse> animals = new ArrayList<>();
        state.getPrey().stream().map(AnimalResponse::from).forEach(animals::add);
        state.getPredators().stream().map(AnimalResponse::from).forEach(animals::add);

        return new SimulationResponse(
                state.getId(),
                state.getStep(),
                state.getParameters().rows(),
                state.getParameters().columns(),
                state.getPrey().size(),
                state.getPredators().size(),
                cells,
                animals
        );
    }
}
