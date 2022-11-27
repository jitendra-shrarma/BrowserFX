package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import resources.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HubController implements Initializable {

    @FXML private Tab history;
    @FXML private Tab bookmark;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            history.setContent(FXMLLoader.load(getClass().getResource(Resources.FXML+"History.fxml")));
            bookmark.setContent(FXMLLoader.load(getClass().getResource(Resources.FXML+"Bookmarks.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
