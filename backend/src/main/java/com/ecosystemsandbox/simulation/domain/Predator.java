package com.ecosystemsandbox.simulation.domain;

public class Predator extends Animal {

    public Predator(Position position, int energy) {
        super(position, energy);
    }

    @Override
    public Species species() {
        return Species.PREDATOR;
    }
}
