package com.preypredator.simulation.domain;

import java.util.UUID;

public abstract class Animal {

    private final UUID id;
    private Position position;
    private int age;
    private double food;
    private final int speed;
    private int lastBreed;

    protected Animal(Position position, int age, double food, int speed, int lastBreed) {
        this.id = UUID.randomUUID();
        this.position = position;
        this.age = age;
        this.food = food;
        this.speed = speed;
        this.lastBreed = lastBreed;
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

    public int getAge() {
        return age;
    }

    public void increaseAge() {
        age++;
    }

    public double getFood() {
        return food;
    }

    public void addFood(double amount) {
        food += amount;
    }

    public void reduceFood(double amount) {
        food -= amount;
    }

    public void halveFood() {
        food = food / 2.0;
    }

    public int getSpeed() {
        return speed;
    }

    public int getLastBreed() {
        return lastBreed;
    }

    public void setLastBreed(int lastBreed) {
        this.lastBreed = lastBreed;
    }

    public void increaseLastBreed() {
        lastBreed++;
    }

    public abstract Species species();
}
