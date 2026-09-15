package com.preypredator.simulation.api;

import com.preypredator.simulation.domain.Animal;
import com.preypredator.simulation.domain.Species;

import java.util.UUID;

public record AnimalResponse(
        UUID id,
        Species species,
        int row,
        int column,
        double food
) {

    public static AnimalResponse from(Animal animal) {
        return new AnimalResponse(
                animal.getId(),
                animal.species(),
                animal.getPosition().row(),
                animal.getPosition().column(),
                animal.getFood()
        );
    }
}
