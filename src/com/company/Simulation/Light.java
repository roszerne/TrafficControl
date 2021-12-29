package com.company.Simulation;
import java.awt.*;

public class Light
{
    private  int green = 10;
    private  int red = 20;
    private  int yellow = 4;
    private  int period;
    private  int currentState;
    public boolean horizontal;
    public Light(boolean horizontal)
    {
        // we set this value so in the first iteration new light will be calculated
        this.currentState = 0;
        this.period = -1;
        this.horizontal = horizontal;
    }
    public boolean sendLight()
    {
        return currentState < green;
    }
    public void nextLight()
    {
        currentState = currentState + 1;

    }
    public Color getColor()
    {
        int status;
        if (horizontal)
        {
            status = this.period - this.currentState;
            if (status < green)
                return Color.GREEN;
            if (status < green + red)
                return Color.RED;
            else
                return Color.YELLOW;
        }

        else
        {
            status = this.currentState;

            if (status < green)
                return Color.GREEN;
            if (status < green + yellow)
                return Color.YELLOW;
            else
                return Color.RED;
        }

    }
    public boolean readForNextLight()
    {
        return this.currentState > this.period;
    }
    public void setNewLights(int g){
        this.green = g;
        this.red = g;
        this.period = this.green + this.red + this.yellow;
        this.currentState = 0;
    }

}

