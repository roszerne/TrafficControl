package com.company.Simulation;
public class RoadSection {

    public Car[][] section;
    private Light light;
    public RoadSection(int length, int width, int index)
    {
        section = new Car[width][length];

        for (int i=0;i<width;i++)
        {
            for (int j=0;j<length;j++)
            {
                section[i][j] = new Car();
            }
        }
        setNeighbors();
        if (index % 2 == 0)
            setLight(new Light(true));
        else
            setLight(new Light(false));
        section[0][0].setLight(this.light);
        section[1][0].setLight(this.light);
    }
    //this function sets neighbors within road section
    //after this function edge neighbors are NOT set
    private void setNeighbors() {
        for (int i = 0; i < 2; i++) {
            for (int j = section[i].length - 1; j > 0; j--)
                section[i][j].addNeighbour(section[i][j - 1]);
            section[i][section[i].length - 1].setType(RoadType.ENTRY);
            section[i][section[i].length - 1].setState(true);

        }
        for (int i = 2; i < 4; i++) {
            for (int j = 0; j < section[i].length - 1; j++)
                section[i][j].addNeighbour(section[i][j + 1]);
            section[i][section[i].length - 1].setType(RoadType.EXIT);
        }
       // section[0][0].addNeighbour(section[0][section[0].length - 1]);
       // section[1][0].addNeighbour(section[1][section[1].length - 1]);
    }
    public void setLight(Light light){
        this.light = light;
    }
    public Light getLight()
    {
        return this.light;
    }
    }