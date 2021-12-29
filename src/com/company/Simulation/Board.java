package com.company.Simulation;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import com.company.Fuzzy.LightController;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;
import com.company.Program;
public class Board extends JComponent implements MouseInputListener, ComponentListener {
    private static final long serialVersionUID = 1L;
    private final String fileName = "fuzzy.fcl";
    private final int roadsNum = 4;
    private RoadSection[] roads = new RoadSection[roadsNum];
    private final int sectionLength = 8;
    private final int sectionWidth = 4;
    public static double fog = 0;
    public double fogDuration = 1;
    public int editType = 0;
    double p = 0.2; // probability of acceleration
    public static double spawn_prob = 0.2;
    int squareSize = 20;
    int arriving = 0;
    int queuing = 0;
    public Board(int length, int height) {
        addMouseListener(this);
        addComponentListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        setOpaque(true);
    }
    public void iteration() {

        // see if we need a new light
        if (roads[0].getLight().readForNextLight())
        {
            this.queuing = getQueuing();
            this.arriving = getArriving();
            System.out.println("QUEUING: " + this.queuing);
            System.out.println("ARRIVING: " + this.arriving);
            int newGreen = (int) LightController.getNewLight(fileName, this.arriving, this.queuing, (int)(fog * 100));
            if (newGreen <= 5)
                newGreen = 6;
            for (RoadSection road : roads)
            {
                road.getLight().setNewLights(newGreen);
            }
        }
        for (int i = 0; i < roadsNum; i++)
        {
            // change the lights
            roads[i].getLight().nextLight();
            // if old's fog has ended, make a new one
            if (this.fogDuration <= 0)
                this.newFog();
            else
                fogDuration--;
            // for every cell
            Car[][] points = roads[i].section;
            for (int x = 0; x <  sectionWidth ; ++x) {
                for (int y = 0; y < sectionLength ; ++y)
                {
                    if(points[x][y].getState())
                    {
                        // a vehicle exit's the crossing
                        if (points[x][y].getType() == RoadType.EXIT)
                            points[x][y].exit();
                        // default move of a car
                        else
                            points[x][y].normal_move(p);
                    }
                    // spawn new vehicles
                    else if (points[x][y].getType() == RoadType.ENTRY)
                        points[x][y].spawn(spawn_prob);
                }
            }
        }
        for (int i = 0; i < roadsNum; i++)
        {
            Car[][] points = roads[i].section;
            for (int x = 0; x <  sectionWidth ; ++x) {
                for (int y = 0; y <  sectionLength ; ++y)
                {
                    points[x][y].changeState();
                }
            }
        }

        this.repaint();
    }

    public void clear() {

        for (int i = 0; i < roadsNum; i++)
        {
            Car[][] points = roads[i].section;
            for (int x = 0; x <  sectionWidth ; ++x) {
                for (int y = 0; y <  sectionLength ; ++y) {
                    points[x][y].clear();
                }
            }
        }
        this.repaint();
    }

    private void initialize() {
        for (int i = 0; i < roadsNum; i++)
        {
            roads[i] = new RoadSection(this.sectionLength, this.sectionWidth,i);
        }

        // connect opposite roadsections
        for (int i = 0; i < this.roadsNum; i++){
            for (int j = 0; j < 2; j++)
            {
                roads[i].section[j][0].addNeighbour(roads[(i + 2) % this.roadsNum].section[3 - j][0]);
            }
        }

    }
    public void newFog(){
        int min = 5;
        int max = 20;
        fog = Math.random();
        this.fogDuration = (int) ((Math.random() * (max - min)) + min);
    }
    public int getArriving(){
        int arriving = 0;
        for (RoadSection road: roads){
            for (int i = 0; i < 2; i ++)
            {
                for (int j = 0; j < 2; j ++)
                {
                    if (road.section[i][j].isOccupied)
                        arriving ++;
                }
            }
        }
        return arriving;
    }
    public int getQueuing(){
        int queuing = 0;
        for (RoadSection road: roads){
            for (int i = 2; i < sectionWidth; i ++)
            {
                for (int j = 2; j < sectionLength; j ++)
                {
                    if (road.section[i][j].isOccupied)
                        queuing ++;
                }
            }
        }
        return queuing;
    }
    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        g.setColor(Color.GRAY);
        Graphics2D g2D = (Graphics2D) g;
        this.paintMainRoad(g2D);
        //drawNetting(g, size);
    }

   /* private void drawNetting(Graphics g, int gridSpace) {
        Insets insets = getInsets();
        int firstX = insets.left;
        int firstY = insets.top;
        int lastX = this.getWidth() - insets.right;
        int lastY = this.getHeight() - insets.bottom;

        int x = firstX;
        while (x < lastX) {
            g.drawLine(x, firstY, x, lastY);
            x += gridSpace;
        }

        int y = firstY;
        while (y < lastY) {
            g.drawLine(firstX, y, lastX, y);
            y += gridSpace;
        }
        Car[][] points = roads[i].section;
        for (x = 0; x < points.length; ++x) {
            for (y = 0; y < points[x].length; ++y) {
                float a = 0f;
                if (points[x][y].getState())
                {
                    g.setColor(new Color(a, a, a, 0.7f));
                    g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
                }

            }
        }

    }*/
    private void paintMainRoad(Graphics2D g2D)
    {
        int[] xDir = {1,1,0,0};
        int[] yDir = {0,-1,-1,0};
        int halfWidth = Program.width / 2;
        int halfHeight = Program.height / 2;
        for (int i = 0; i < roadsNum; i++) {
            int startX = sectionWidth * squareSize * xDir[i]  + halfWidth;
            int startY = squareSize * sectionWidth * yDir[i] + halfHeight;;
            double angle = Math.toRadians( i  * 90);
            AffineTransform current = g2D.getTransform();
            AffineTransform at = new AffineTransform();

            at.rotate(angle, halfWidth, halfHeight);
            g2D.setTransform(at);
            Car [][] points = roads[i].section;
            int transparency;
            // if the fog is too thick we make it smaller to preserve visibility
            if (this.fog >= 0.2)
                transparency = (int) (255 * (1 - fog + 0.2));
            else
                transparency = (int) (255 * (1 - fog));
            for (int x = 0; x < sectionLength; x++) {
                for (int y = 0;y < sectionWidth; y++)
                {
                    //Coloring cells
                    if (points[y][x].getState())
                    {
                            g2D.setPaint(new Color(0,0,255,transparency));
                            //System.out.println(y + " " +x);
                    }
                    else if (points[y][x].getType() == RoadType.ENTRY)
                    {
                        g2D.setPaint(new Color(0,255,0,transparency));
                    }
                    else if (points[y][x].getType() == RoadType.EXIT)
                    {
                        g2D.setPaint(new Color(255,0,0,transparency));
                    }
                    else
                        g2D.setPaint(new Color(255,255,255,transparency));

                    int startSquareX = startX + x * squareSize;
                    int startSquareY = startY + y * squareSize;
                    g2D.fillRect(startSquareX, startSquareY, squareSize, squareSize);
                    g2D.setPaint(new Color(0, 0, 0));
                    g2D.drawRect(startSquareX, startSquareY, squareSize, squareSize);
                }
            }
            if (points[0][0].hasLight)
            {

                startY -= squareSize;
                g2D.setPaint(points[0][0].getLight().getColor());
                g2D.fillRect(startX, startY, squareSize, squareSize);
                g2D.setPaint(new Color(0, 0, 0));
                g2D.drawRect(startX, startY, squareSize, squareSize);
            }
            g2D.setTransform(current);

        }
    }
    public void mouseClicked(MouseEvent e) {
      /*  int x = e.getX();
        int y = e.getY();
        if (x > com.company.Program.width)
            x = x % com.company.Program.width;
        else
            x = x - com.company.Program.width;
        System.out.println("X: " + x + "Y: "+ y);
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
            if (editType == 0) {
                points[x][y].clicked();
                System.out.println("X: " + x + "Y: "+ y);
            }
            this.repaint();
        }*/
    }

    public void componentResized(ComponentEvent e) {
       // int dlugosc = (this.getWidth() / size) + 1;
       // int wysokosc = (this.getHeight() / size) + 1;
       // initialize(dlugosc, wysokosc);
        initialize();
    }

    public void mouseDragged(MouseEvent e) {
     /*   int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
            if (editType == 0) {
                points[x][y].clicked();
            }
            this.repaint();
        }*/
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

}
