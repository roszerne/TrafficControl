package com.company.Simulation;
import java.awt.*;
import java.util.ArrayList;

public class Car {

    int velocity;
    boolean isOccupied;
    boolean nextState;
    public boolean hasLight;
    private Light light;
    private RoadType type;
    private final ArrayList<Car> neighbors;

    public Car() {
        this.velocity = 0;
        this.isOccupied = false;
        this.nextState = false;
        this.neighbors = new ArrayList<Car>();
        this.type = RoadType.NORMAL;
    }

    public Car(RoadType type) {
        this();
        this.type = type;
    }

    public void clear() {
        this.velocity = 0;
        this.isOccupied = false;
        this.nextState = false;
    }

    public void changeState() {
        this.setState(this.nextState);
    }

    public void accelerate() {
        if (this.getVelocity() < 1)
            this.setVelocity((this.velocity + 1));

    }

    public void randomize(double p) {
        if (this.velocity > 0) {
            if (Math.random() <= p) {
                setVelocity((this.velocity - 1));
            }
        }
    }
    public void slowDown() {

        int newVel = 0;
        for (int i = 0; i < this.neighbors.size(); i++) {
            if (this.neighbors.get(i).getState())
                break;

            newVel++;
        }
        if (this.getVelocity() > newVel)
            this.setVelocity(newVel);
    }

    public void move() {
        if (this.getVelocity() > 0) {
            this.neighbors.get(this.getVelocity() - 1).setNextState(true);
            this.neighbors.get(this.getVelocity() - 1).setVelocity(this.getVelocity());
            this.setVelocity(0);
            this.setNextState(false);
        } else {
            this.setNextState(true);
        }
    }
    public void normal_move(double p)
    {

        if (this.hasLight && this.light.getColor() != Color.GREEN)
            this.setVelocity(0);
        else
        {
            this.accelerate();
            this.slowDown();
            this.randomize(p);
        }
        this.move();
    }
    public void exit() {
        if (this.isOccupied) {
            this.setVelocity(0);
            this.setNextState(false);
        }
    }
    public void spawn(double p){
        if (!this.isOccupied){
            if (Math.random() < p)
            {
                this.isOccupied = true;
                this.nextState = true;
                this.velocity = 1;
            }
        }
    }
    public void setState(boolean s) {
        this.isOccupied = s;
    }

    public boolean getState() {
        return this.isOccupied;
    }

    public void setNextState(boolean s) {
        this.nextState = s;
    }

    public void setVelocity(int v) {
        this.velocity = v;
    }

    public void setType(RoadType type) {
        this.type = type;
    }
    public void setLight(Light light){
        this.light = light;
        this.hasLight = true;
    }
    public Light getLight()
    {
        return this.light;
    }
    public int getVelocity() {
        return this.velocity;
    }

    public RoadType getType() {
        return type;
    }

    public void addNeighbour(Car nei) {
        this.neighbors.add(nei);

    }


}