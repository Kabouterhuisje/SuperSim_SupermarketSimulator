package models;

import javafx.scene.canvas.Canvas;
import application.Main;
import controllers.MainController;

import javax.swing.*;
import java.util.ArrayList;

public class CashRegister extends ImageCollectorModel {
    private int row;
    private ArrayList<Cart> carts;
    private Timer updateTimer;
    private Timer gapTimer;
    private int gap;
    private MainController controller;

    public CashRegister(int row) 
    {
        super("cashregister");

        this.carts = new ArrayList<>();
        this.row = row;
        this.updateTimer = new Timer(1000, event -> this.check());
        this.gapTimer = new Timer(1, event -> this.updateGap());
        this.gap = 0;

        this.updateTimer.setRepeats(true);
        this.gapTimer.setRepeats(true);

        // start timers
        this.startTimers();
    }

    public Timer getGapTimer() 
    {
        return this.gapTimer;
    }

    public void setGap(int gap) 
    {
        this.gap = gap;
    }

    public void startTimers() 
    {
        this.updateTimer.start();
        this.gapTimer.start();
    }

    public void stopTimers() 
    {
        this.updateTimer.stop();
        this.gapTimer.stop();
    }

    public void addCart(double d) 
    {
        this.carts.add(new Cart(this.row, d));
    }

    public int getWaitingCount() 
    {
        return this.carts.size();
    }

    public void drawOnCanvas(Canvas canvas) 
    {
        // teken kassa
        super.drawOnCanvas(canvas, 0, row * 50);

        // teken aantal winkelwagens (nummer)
        canvas.getGraphicsContext2D().fillText(String.valueOf(this.carts.size()), 550, (row * 50) + 20);

        // bereken hoeveel winkelwagens er getekend moeten worden
        int count = 10;

        if (this.carts.size() < 10) 
        {
            count = this.carts.size();
        }

        // teken wachtende klanten
        for (int i = 0; i < count; i++) 
        {
            this.carts.get(i).drawOnCanvas(canvas, (i + 1), this.gap);
        }
    }

    private void check() 
    {
        if (this.carts.size() < 1) 
        {
            return;
        }
        
        this.gapTimer.setDelay(controller.getAnimationSpeed());
        
        Cart cart = this.carts.get(0);

        // verklein tijd
        if (cart.getTimeRemaining() > 0) 
        {
            cart.secondsRemaining((-1 * controller.getSimulationSpeed()));
            return;
        }
        
        this.carts.remove(0);

        if (controller.getAnimationSpeed() > 0) 
        {
            this.gap += 50;
        }
    }

    private void updateGap() 
    {
        if (this.gap > 0) 
        {
            this.gap--;
        }
    }
}

