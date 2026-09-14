package com.ecosystemsandbox.simulation.domain;

import java.util.UUID;

public abstract class Animal {

    private final UUID id;
    private Position position;
    private int energy;

    protected Animal(Position position, int energy) {
        this.id = UUID.randomUUID();
        this.position = position;
        this.energy = energy;
    }

    public UUID getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public void moveTo(Position position) {
        this.position = position;
    }

    public int getEnergy() {
        return energy;
    }

    public void gainEnergy(int amount) {
        energy += amount;
    }

    public void spendEnergy(int amount) {
        energy -= amount;
    }

    public boolean isAlive() {
        return energy > 0;
    }

    public abstract Species species();
}
