package controllers;

import java.util.Random;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.paint.Color;
import enums.State;
import utils.CustomersGenerator;
import models.CashRegister;

import javax.swing.*;
import java.util.ArrayList;

public class MainController extends ViewCollectorController {
	
    private static int simulationSpeed = 60;
    private static int animationSpeed = 5;
    private Button btnStart;
    private Button btnStop;
    private Button btnReset;
    private Label lblSpeed;
    private Label lblAnimation;
    private Label lblRegisters;
    private Label lblCustomerGrace;
    private Label lblCustomerTime;
    private Slider slSpeed;
    private Slider slAnimationSpeed;
    private Slider slRegisters;
    private static Slider slTimeBetweenCustomers;
    private Slider slCustomerTime;
    private Canvas canvas;
    private Timer drawTimer;
    private State state;
    private CustomersGenerator customersGenerator;
    private ArrayList<CashRegister> cashRegisters;

    public MainController() 
    {
        this.state = State.STOPPED;
        this.customersGenerator = new CustomersGenerator(0, () -> this.addCart()); 
        this.cashRegisters = new ArrayList<>();
        this.drawTimer = new Timer(16, event -> this.draw());

        // Ophalen van het linker panel
        ObservableList<Node> panes = ((SplitPane)this.getView().lookup("#pane")).getItems();
        Node controlsPane = panes.get(0);

        // Ophalen van alle controls
        this.btnStart = (Button) controlsPane.lookup("#btnStart");
        this.btnStop = (Button) controlsPane.lookup("#btnStop");
        this.btnReset = (Button) controlsPane.lookup("#btnReset");
        this.lblSpeed = (Label) controlsPane.lookup("#lblSpeed");
        this.lblAnimation = (Label) controlsPane.lookup("#lblAnimation");
        this.lblRegisters = (Label) controlsPane.lookup("#lblRegisters");
        this.lblCustomerGrace = (Label) controlsPane.lookup("#lblCustomerGrace");
        this.lblCustomerTime = (Label) controlsPane.lookup("#lblCustomerTime");
        this.slSpeed = (Slider) controlsPane.lookup("#sldrSpeed");
        this.slAnimationSpeed = (Slider) controlsPane.lookup("#sldrAnimation");
        this.slRegisters = (Slider) controlsPane.lookup("#sldrRegisters");
        this.slTimeBetweenCustomers = (Slider) controlsPane.lookup("#sldrCustomerGrace");
        this.slCustomerTime  = (Slider) controlsPane.lookup("#sldrCustomerTime");
        this.canvas = (Canvas) panes.get(1).lookup("#cashRegistersPanel");

        // Methodes aan controls koppelen
        this.btnStart.setOnAction(event -> this.startSimulation());
        this.btnStop.setOnAction(event -> this.stopSimulation());
        this.btnReset.setOnAction(event -> this.resetSimulation());
        this.slSpeed.valueProperty().addListener(change -> this.changeSimulationSpeed());
        this.slAnimationSpeed.valueProperty().addListener(change -> this.changeAnimationSpeed());
        this.slRegisters.valueProperty().addListener(change -> this.changeAmountOfRegisters());
        this.slTimeBetweenCustomers.valueProperty().addListener(change -> this.changeTimeBetweenCustomers());
        this.slCustomerTime.valueProperty().addListener(change -> this.changeCustomerTime());

        this.canvas.getGraphicsContext2D().setFill(Color.rgb(0, 0, 0));
    }
    
    public static int getAnimationSpeed() 
    {
        return animationSpeed;
    }

    public static int getSimulationSpeed() 
    {
        return simulationSpeed;
    }
    
    public static int getTimeBetweenCustomers()
    {
    	return (int) slTimeBetweenCustomers.getValue();
    }

    private void draw() 
    {
        // canvas legen
        this.canvas.getGraphicsContext2D().clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());

        // teken kassas
        for (CashRegister register : this.cashRegisters) 
        {
            register.drawOnCanvas(this.canvas);
        }
    }

    private void resetSimulation() 
    {
        this.slSpeed.setValue(60);
        this.slAnimationSpeed.setValue(5);
        this.slRegisters.setValue(6);
        this.slTimeBetweenCustomers.setValue(30);
        this.slCustomerTime.setValue(180);
        simulationSpeed = 60;
    }

    private void startSimulation() {

        switch (this.state) {
            case STOPPED:
                // teken kassas
                for (int i = 0; i < this.slRegisters.getValue(); i++) 
                {
                    this.cashRegisters.add(new CashRegister(i));
                }

                for (int i = 0; i < this.cashRegisters.size(); i++) 
                {
                    this.cashRegisters.get(i).drawOnCanvas(this.canvas);
                }

                this.slRegisters.setDisable(true);
                this.btnStop.setDisable(false);
                this.slAnimationSpeed.setDisable(true);
                this.slCustomerTime.setDisable(true);
                this.slSpeed.setDisable(true);
                this.slTimeBetweenCustomers.setDisable(true);
                this.btnReset.setDisable(true);
                this.state = State.RUNNING;
            case PAUSED:
                this.customersGenerator.setDelay((int)Math.round(this.slTimeBetweenCustomers.getValue() * 1000) / getSimulationSpeed());
                this.customersGenerator.start();
                
                // start timers
                for (CashRegister register : this.cashRegisters) 
                {
                    register.startTimers();
                }

                // start rendering
                this.drawTimer.start();
                this.state = State.RUNNING;
                this.btnStart.setText("Pause");
                
                this.slRegisters.setDisable(true);
                this.btnStop.setDisable(false);
                this.slAnimationSpeed.setDisable(true);
                this.slCustomerTime.setDisable(true);
                this.slSpeed.setDisable(true);
                this.slTimeBetweenCustomers.setDisable(true);
                this.btnReset.setDisable(true);
                break;
            case RUNNING:
                // stop timers
                for (CashRegister register : this.cashRegisters) 
                {
                    register.stopTimers();
                }
                
                this.slRegisters.setDisable(false);
                this.btnStop.setDisable(false);
                this.slAnimationSpeed.setDisable(false);
                this.slCustomerTime.setDisable(false);
                this.slSpeed.setDisable(false);
                this.slTimeBetweenCustomers.setDisable(false);
                this.btnReset.setDisable(false);
                
                this.state = State.PAUSED;
                this.btnStart.setText("Start");
                this.customersGenerator.stop();
                this.drawTimer.stop();
                break;
        }
    }

    private void stopSimulation() 
    {
        // stop timer
        this.customersGenerator.stop();
        this.drawTimer.stop();
        this.cashRegisters.clear();

        // canvas legen
        this.canvas.getGraphicsContext2D().clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        this.slRegisters.setDisable(false);
        this.btnStop.setDisable(true);
        this.state = State.STOPPED;
        this.btnStart.setText("Start");
        
        this.slRegisters.setDisable(false);
        this.btnStop.setDisable(false);
        this.btnStart.setDisable(false);
        this.slAnimationSpeed.setDisable(false);
        this.slCustomerTime.setDisable(false);
        this.slSpeed.setDisable(false);
        this.slTimeBetweenCustomers.setDisable(false);
        this.btnReset.setDisable(false);
    }

    private void changeSimulationSpeed() 
    {
        simulationSpeed = (int) Math.round(this.slSpeed.getValue());
        this.lblSpeed.setText(Long.toString(Math.round(this.slSpeed.getValue())));
    }

    private void changeAnimationSpeed() 
    {
        animationSpeed = (int) Math.round(this.slAnimationSpeed.getValue());
        this.lblAnimation.setText(Long.toString(Math.round(this.slAnimationSpeed.getValue())));

        if (this.slAnimationSpeed.getValue() == 0.0) 
        {
            for (CashRegister register : this.cashRegisters) 
            {
                register.getGapTimer().stop();
                register.setGap(0);
            }
            return;
        }

        for (CashRegister register : this.cashRegisters) 
        {
            // restart timer
            if (!register.getGapTimer().isRunning()) 
            {
                register.getGapTimer().start();
            }
        }
    }

    private void changeAmountOfRegisters() 
    {
        this.lblRegisters.setText(Long.toString(Math.round(this.slRegisters.getValue())));
    }

    private void changeTimeBetweenCustomers() 
    {
        this.customersGenerator.setDelay((int)Math.round(slTimeBetweenCustomers.getValue() * 1000) / getSimulationSpeed());
        this.lblCustomerGrace.setText(Long.toString(Math.round(slTimeBetweenCustomers.getValue())));
    }

    private void changeCustomerTime() 
    {
        this.lblCustomerTime.setText(Long.toString(Math.round(this.slCustomerTime.getValue())));
    }
    
    // Tijd per klant gebaseerd op gemiddelde 
    private double randomTimePerCustomer() 
    {	
    	Random rand = new Random();
    	
    	int max = (int)(2 * this.slCustomerTime.getValue() - 30) + 1;
    	int min = 30;
    	int random = rand.nextInt(max - min) + min;
    	
    	return random;
    }
    
    private void addCart() {

        CashRegister shortest = this.cashRegisters.get(0);

        // Kijken welke rij de kortste is
        for (CashRegister register : this.cashRegisters) 
        {
            if (register.getWaitingCount() < shortest.getWaitingCount()) 
            {
                shortest = register;
            }
        }

        // nieuwe klant toevoegen
        shortest.addCart(this.randomTimePerCustomer());
    }
}

