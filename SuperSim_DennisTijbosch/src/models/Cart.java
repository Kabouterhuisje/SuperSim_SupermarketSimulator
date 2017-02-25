package models;

import javafx.scene.canvas.Canvas;

public class Cart extends ImageCollectorModel {
    private int y;
    private int timeNeeded;

    public Cart(int row, double d) 
    {
        super("cart");

        this.y = row * 50;
        this.timeNeeded = (int) d;
    }

    public int getTimeRemaining() 
    {
        return this.timeNeeded;
    }

    public void drawOnCanvas(Canvas canvas, int col, int gap) 
    {
        int x = (col * 50) + gap;
        
        super.drawOnCanvas(canvas, x, this.y);
    }

    public void secondsRemaining(int simulationSeconds) 
    {
        this.timeNeeded += simulationSeconds;
    }
}