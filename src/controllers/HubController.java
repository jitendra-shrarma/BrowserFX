package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** @author God-Hand */
public class HubController implements Initializable {

    @FXML private Tab history;
    @FXML private Tab bookmark;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            history.setContent(FXMLLoader.load(getClass().getResource("../resources/fxml/History.fxml")));
            bookmark.setContent(FXMLLoader.load(getClass().getResource("../resources/fxml/Bookmarks.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}