package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class BookmarkPopOverController implements Initializable{

    @FXML private JFXTextField name;
    @FXML private JFXComboBox<?> folderBox;
    @FXML private JFXButton newFolder;
    @FXML private JFXButton save;
    @FXML private JFXButton cancel;

    BookmarkPopOverController(){
        // TODO Auto-generated constructor stub
    }

    @FXML
    void cancelOrRemove(MouseEvent event) {

    }

    @FXML
    void createFolder(MouseEvent event) {

    }

    @FXML
    void save(MouseEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}