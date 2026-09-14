package com.ecosystemsandbox.simulation.api;

import com.ecosystemsandbox.simulation.domain.CellType;

public record CellResponse(int row, int column, CellType type) {
}
