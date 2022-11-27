package controllers;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import database.BookMarksDataBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class BookmarkController implements Initializable{

    @FXML private TreeView<?> treeView;
    @FXML private JFXTextField search;
    @FXML private JFXTreeTableView<HistoryStoreView> table;

    private JFXTreeTableColumn<URLdetails, String> nameCol =  new JFXTreeTableColumn<>("name");
    private JFXTreeTableColumn<URLdetails, String> timeCol =  new JFXTreeTableColumn<>("time");
    private JFXTreeTableColumn<URLdetails, String> dateCol =  new JFXTreeTableColumn<>("date");
    private JFXTreeTableColumn<URLdetails, String> locationCol =  new JFXTreeTableColumn<>("location");

    public static ObservableList<URLdetails> list = FXCollections.observableArrayList();
    TreeItem parentFolder = new TreeItem<>("All Bookmarks");
    private ObservableList<String> folders = BookMarksDataBase.folders(1);

    public BookmarkController() {
        // TODO Auto-generated constructor stub
    }

    @FXML void deleteAll(MouseEvent event) {

    }

    @FXML void deleteSelected(MouseEvent event) {

    }

    @FXML void SearchDataInTable(KeyEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        folders.sorted();
        for(int i=0 ; i< folders.size();i++){
            System.out.println(i);
            parentFolder.getChildren().add(new TreeItem<>(folders.get(i)));
        }
        treeView.setRoot(parentFolder);
    }
}


class URLdetails {
    private	StringProperty name;
    private	StringProperty time;
    private	StringProperty date;
    private	StringProperty location;
    public URLdetails(String name , String time,String date, String location){
        this.name = new SimpleStringProperty(name);
        this.time = new SimpleStringProperty(time);
        this.date = new SimpleStringProperty(date);
        this.location = new SimpleStringProperty(location);
    }
    public String getName() {
        return name.get();
    }
    public String getDate() {
        return date.get();
    }
    public String getLocation() {
        return location.get();
    }
    public String getTime() {
        return time.get();
    }
    public void setName(String name){
        this.name = new SimpleStringProperty(name);
    }
    public void setTime(String time){
        this.time = new SimpleStringProperty(time);
    }
    public void setDate(String date){
        this.date = new SimpleStringProperty(date);
    }
    public void setLocation(String location){
        this.location =new SimpleStringProperty(location);
    }
    @Override
    public String toString(){
        return this.location.toString();
    }
}