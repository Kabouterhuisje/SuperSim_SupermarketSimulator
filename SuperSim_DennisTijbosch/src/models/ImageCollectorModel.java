package models;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

abstract public class ImageCollectorModel {
	
    private String imagePath;
    protected Image image;

    // Juiste image ophalen (kassa of winkelwagen)
    public ImageCollectorModel(String imagePath) 
    {
        this.imagePath = imagePath;
        this.image = new Image(getClass().getResourceAsStream("/Images/" + this.imagePath + ".png"));
    }

    public void drawOnCanvas(Canvas canvas, int x, int y) 
    {
        canvas.getGraphicsContext2D().drawImage(this.image, x, y);
    }
}
