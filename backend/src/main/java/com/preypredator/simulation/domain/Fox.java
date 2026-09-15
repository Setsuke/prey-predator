package com.preypredator.simulation.domain;

public class Fox extends Animal {

    public Fox(Position position, int age, double food, int speed, int lastBreed) {
        super(position, age, food, speed, lastBreed);
    }

    @Override
    public Species species() {
        return Species.FOX;
    }
}
