package controllers;

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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

/** @author God-Hand */
public class HistoryController implements Initializable{

    @FXML private TreeView<?> treeView;
    @FXML private JFXTreeTableView<HistoryStoreView> table;

    private JFXTreeTableColumn<HistoryStoreView, String> linkCol = new JFXTreeTableColumn<HistoryStoreView, String>("URL");
    private JFXTreeTableColumn<HistoryStoreView, String> titleCol = new JFXTreeTableColumn<HistoryStoreView, String>("Title");
    private JFXTreeTableColumn<HistoryStoreView, String> timeCol = new JFXTreeTableColumn<HistoryStoreView, String>("Time");
    private JFXTreeTableColumn<HistoryStoreView, String> dateCol = new JFXTreeTableColumn<HistoryStoreView, String>("Date");

    private ResultSet resultSet = null;
    private TreeItem rootItem;

    public HistoryController() {
        // TODO Auto-generated constructor stub
    }

    @FXML void deleteSelectedRow(MouseEvent event) {
        try {
            TreeItem<HistoryStoreView> selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.getParent().getChildren().remove(selectedItem);
            HistoryManagement.delete(selectedItem.getValue().link.getValue(),selectedItem.getValue().date.getValue());
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @FXML void clearSpecificHistory(MouseEvent event) {
        String selectedTime = treeView.getSelectionModel().getSelectedItem().getValue().toString();

        if (selectedTime.equals("History")) {
            HistoryManagement.deleteFullHistory();
        }else if (selectedTime.equals("Past Hour")) {
            HistoryManagement.deletePastHoursHistory(-1);
        }else if (selectedTime.equals("Today")) {
            HistoryManagement.deleteHistory(0);
        }else if (selectedTime.equals("One Week")) {
            HistoryManagement.deleteHistory(-7);
        }else if (selectedTime.equals("One Month")) {
            HistoryManagement.deleteHistory(-30);
        }
        table.getRoot().getChildren().clear();
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
        linkCol.setPrefWidth(335);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootItem = new TreeItem("History");
        rootItem.getChildren().addAll(new TreeItem("Past Hour"),
                new TreeItem("Today"),new TreeItem("One Week"),new TreeItem("One Month"));
        treeView.setRoot(rootItem);
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getValue().equals("History")) {
                resultSet = HistoryManagement.getFullHistory();
            }else if (newValue.getValue().equals("Past Hour")) {
                resultSet = HistoryManagement.pastHoursHistory(-1);
            }else if (newValue.getValue().equals("Today")) {
                resultSet = HistoryManagement.getHistory(0);
            }else if (newValue.getValue().equals("One Week")) {
                resultSet = HistoryManagement.getHistory(-7);
            }else if (newValue.getValue().equals("One Month")) {
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
}