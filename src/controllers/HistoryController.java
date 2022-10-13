package controllers;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import database.HistoryManagement;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HistoryController implements Initializable{

    @FXML private TreeView<?> treeView;
    @FXML private JFXTextField search;
    @FXML private JFXTreeTableView<HistoryStoreView> table;

    private JFXTreeTableColumn<HistoryStoreView, String> linkCol = new JFXTreeTableColumn<HistoryStoreView, String>("Links");
    private JFXTreeTableColumn<HistoryStoreView, String> titleCol = new JFXTreeTableColumn<HistoryStoreView, String>("Domain Name");
    private JFXTreeTableColumn<HistoryStoreView, String> timeCol = new JFXTreeTableColumn<HistoryStoreView, String>("Time");
    private JFXTreeTableColumn<HistoryStoreView, String> dateCol = new JFXTreeTableColumn<HistoryStoreView, String>("Date");

    private ObservableList<HistoryStoreView> list = FXCollections.observableArrayList();
    private ResultSet resultSet = null;
    private TreeItem rootItem;

    public HistoryController() {
        // TODO Auto-generated constructor stub
    }

    @FXML
    void deleteSelectedRow(MouseEvent event) {
        try {
            TreeItem<HistoryStoreView> selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.getParent().getChildren().remove(selectedItem);
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void addListInTable(ObservableList list) {
        titleCol.setPrefWidth(200);
        titleCol.setCellValueFactory(
                new Callback<CellDataFeatures<HistoryStoreView, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<HistoryStoreView, String> param) {
                        return param.getValue().getValue().title;
                    }
                });
        linkCol.setPrefWidth(300);
        linkCol.setCellValueFactory(
                new Callback<CellDataFeatures<HistoryStoreView, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<HistoryStoreView, String> param) {
                        return param.getValue().getValue().link;
                    }
                });
        timeCol.setPrefWidth(100);
        timeCol.setCellValueFactory(
                new Callback<CellDataFeatures<HistoryStoreView, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<HistoryStoreView, String> param) {
                        return param.getValue().getValue().time;
                    }
                });
        dateCol.setPrefWidth(100);
        dateCol.setCellValueFactory(
                new Callback<CellDataFeatures<HistoryStoreView, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<HistoryStoreView, String> param) {
                        return param.getValue().getValue().date;
                    }
                });


        final TreeItem<HistoryStoreView> root = new RecursiveTreeItem<HistoryStoreView>(list,
                RecursiveTreeObject::getChildren);

        table.getColumns().setAll(titleCol, linkCol, timeCol, dateCol);
        table.setRoot(root);
        table.setShowRoot(false);

    }

    public void initializingView() {
        rootItem = new TreeItem("History");
        rootItem.getChildren().addAll(new TreeItem("Past Hour"),
                new TreeItem("Today"),new TreeItem("One Week"),new TreeItem("One Month"));
        treeView.setRoot(rootItem);
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getValue().equals("History")) {
                resultSet = HistoryManagement.getFullHistory();
            }
            if (newValue.getValue().equals("Past Hour")) {
                resultSet = HistoryManagement.pastHoursHistory(-1);
            }
            if (newValue.getValue().equals("Today")) {
                resultSet = HistoryManagement.getHistory(0);
            }
            if (newValue.getValue().equals("One Week")) {
                resultSet = HistoryManagement.getHistory(-7);
            }
            if (newValue.getValue().equals("One Month")) {
                resultSet = HistoryManagement.getHistory(-30);
            }

            ObservableList<HistoryStoreView> list = FXCollections.observableArrayList();

            try{
                while (resultSet.next()){
                    list.add(new HistoryStoreView(resultSet.getString(1),
                            resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
                }
            }catch (Exception e){
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
            addListInTable(list);
        });
        treeView.getSelectionModel().select(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializingView();
    }
}

class HistoryStoreView extends RecursiveTreeObject<HistoryStoreView> {
    StringProperty link;
    StringProperty title;
    StringProperty time;
    StringProperty date;

    public HistoryStoreView( String link ,String title ,String time ,String date) {
        this.link = new SimpleStringProperty(link);
        this.title = new SimpleStringProperty(title);
        this.time = new SimpleStringProperty(time);
        this.date = new SimpleStringProperty(date);
    }
}
