package hub;

import controllers.HistoryController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import resources.Resources;
import java.io.IOException;

public class Hub extends Application{

    private Stage stage;
    public HistoryController historyController = new HistoryController();
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void show(){
        if(stage.isIconified()){
            stage.setIconified(false);
        }
        stage.show();

    }

    @Override
    public void start(Stage primaryStage){
        try {
            Parent root = FXMLLoader.load(getClass().getResource(Resources.FXML + "Hub.fxml"));
            Scene scene = new Scene(root,1000,600);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Hub");
            setStage(primaryStage);
        }catch (IOException e){
            System.out.println("Hub error");
        }
        //remove it after completion of hub
        //primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
