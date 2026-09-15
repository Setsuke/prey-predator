package com.preypredator.simulation.domain;

public record Position(int row, int column) {

    public Position moveBy(int rowDelta, int columnDelta) {
        return new Position(row + rowDelta, column + columnDelta);
    }
}
