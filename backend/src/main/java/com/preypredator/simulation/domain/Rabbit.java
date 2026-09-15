package com.preypredator.simulation.domain;

public class Rabbit extends Animal {

    private boolean eaten;

    public Rabbit(Position position, int age, double food, int speed, int lastBreed) {
        super(position, age, food, speed, lastBreed);
    }

    public boolean isEaten() {
        return eaten;
    }

    public void markEaten() {
        eaten = true;
    }

    @Override
    public Species species() {
        return Species.RABBIT;
    }
}
