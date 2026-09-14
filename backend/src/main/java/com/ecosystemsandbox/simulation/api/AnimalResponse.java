package com.ecosystemsandbox.simulation.api;

import com.ecosystemsandbox.simulation.domain.Animal;
import com.ecosystemsandbox.simulation.domain.Species;

import java.util.UUID;

public record AnimalResponse(
        UUID id,
        Species species,
        int row,
        int column,
        int energy
) {

    public static AnimalResponse from(Animal animal) {
        return new AnimalResponse(
                animal.getId(),
                animal.species(),
                animal.getPosition().row(),
                animal.getPosition().column(),
                animal.getEnergy()
        );
    }
}
