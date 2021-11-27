package hub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** @author God-Hand */
public class Hub extends Application{

    private Stage stage;
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
            Parent root = FXMLLoader.load(getClass().getResource("../resources/fxml/Hub.fxml"));
            Scene scene = new Scene(root,1000,600);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Hub");
            setStage(primaryStage);
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}