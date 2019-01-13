package hub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** @author God-Hand */
public class Hub{

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

    public Hub(){
        try {
            stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../resources/fxml/Hub.fxml"));
            Scene scene = new Scene(root,1000,600);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle("Hub");
            setStage(stage);
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}