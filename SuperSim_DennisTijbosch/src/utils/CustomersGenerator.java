package utils;

import javax.swing.*;

import controllers.MainController;

public class CustomersGenerator extends Timer {
	
    public CustomersGenerator(int delay, Runnable callback) 
    {
        super(delay, event -> {
            callback.run();
        });

        // timer herhaald
        super.setRepeats(true);
    }

    @Override
    public void setDelay(int delay) 
    {
        super.setDelay(delay);
    }
}
