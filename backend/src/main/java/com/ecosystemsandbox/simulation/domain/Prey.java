package com.ecosystemsandbox.simulation.domain;

public class Prey extends Animal {

    public Prey(Position position, int energy) {
        super(position, energy);
    }

    @Override
    public Species species() {
        return Species.PREY;
    }
}
