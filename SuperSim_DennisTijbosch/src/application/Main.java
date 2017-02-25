package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import application.Main;
import controllers.MainController;
import javafx.scene.Scene;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
    	
        MainController mc = new MainController();

        primaryStage.setTitle("SuperSim");
        primaryStage.setScene(new Scene(mc.getView(), 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
