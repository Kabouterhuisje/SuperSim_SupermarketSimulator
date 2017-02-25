package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

abstract public class ViewCollectorController {
	
    protected Parent root;

    public ViewCollectorController() 
    {
    	// View laden
        try 
        {
            this.root = FXMLLoader.load(getClass().getResource("/views/View.fxml"));
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public Parent getView() 
    {
        return this.root;
    }
}
