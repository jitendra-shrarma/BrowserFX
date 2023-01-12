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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
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

    // Lists for maintaining different Histories
    private ObservableList<HistoryStoreView> fullHistory = FXCollections.observableArrayList();
    private ObservableList<HistoryStoreView> pastHourHistory = FXCollections.observableArrayList();
    private ObservableList<HistoryStoreView> todayHistory = FXCollections.observableArrayList();
    private ObservableList<HistoryStoreView> WeekHistory = FXCollections.observableArrayList();
    private ObservableList<HistoryStoreView> MonthHistory = FXCollections.observableArrayList();

    private ArrayList<TreeItem> storeItems;
    private TreeItem rootItem;

    public HistoryController() {
        // TODO Auto-generated constructor stub
    }

    public void initializeListsWithData() {
        fullHistory = HistoryManagement.getfullHistory(fullHistory);
        pastHourHistory = HistoryManagement.pastHoursHistory(pastHourHistory, -1);
        todayHistory = HistoryManagement.getHistory(todayHistory, 0);
        WeekHistory = HistoryManagement.getHistory(WeekHistory, -7);
        MonthHistory = HistoryManagement.getHistory(MonthHistory, -30);
    }

    public static void addDataInList(String link, String title, String time, String date, ObservableList list) {
        list.add(new HistoryStoreView(link, title, time, date));
    }

    public void addListInTable(ObservableList<HistoryStoreView> list) {
        linkCol.setPrefWidth(300);
        linkCol.setCellValueFactory(
                new Callback<CellDataFeatures<HistoryStoreView, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<HistoryStoreView, String> param) {
                        return param.getValue().getValue().link;
                    }
                });

        titleCol.setPrefWidth(150);
        titleCol.setCellValueFactory(
                new Callback<CellDataFeatures<HistoryStoreView, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<HistoryStoreView, String> param) {
                        return param.getValue().getValue().title;
                    }
                });
        timeCol.setPrefWidth(150);
        timeCol.setCellValueFactory(
                new Callback<CellDataFeatures<HistoryStoreView, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<HistoryStoreView, String> param) {
                        return param.getValue().getValue().time;
                    }
                });
        dateCol.setPrefWidth(150);
        dateCol.setCellValueFactory(
                new Callback<CellDataFeatures<HistoryStoreView, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<HistoryStoreView, String> param) {
                        return param.getValue().getValue().date;
                    }
                });

        final TreeItem<HistoryStoreView> root = new RecursiveTreeItem<HistoryStoreView>(list,
                RecursiveTreeObject::getChildren);

        table.getColumns().setAll(linkCol, titleCol, timeCol, dateCol);
        table.setRoot(root);
        table.setShowRoot(false);

    }

    public static ArrayList<TreeItem> getStoreItems() {
        ArrayList<TreeItem> storeItems = new ArrayList<TreeItem>();
        storeItems.add(new TreeItem("Past hour"));
        storeItems.add(new TreeItem("Today"));
        storeItems.add(new TreeItem("One Week"));
        storeItems.add(new TreeItem("One Month"));
        return storeItems;
    }

    public void initializingView() {
        storeItems = getStoreItems();
        rootItem = new TreeItem("History");
        rootItem.getChildren().addAll(storeItems);
        rootItem.setExpanded(true);
        treeView.setRoot(rootItem);
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
            Node node = e.getPickResult().getIntersectedNode();
            if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                String name = (String) ((TreeItem) treeView.getSelectionModel().getSelectedItem()).getValue();
                System.out.println(name);

                if (name.equals("History")) {
                    addListInTable(fullHistory);
                }
                if (name.equals("Past hour")) {
                    addListInTable(pastHourHistory);
                }
                if (name.equals("Today")) {
                    addListInTable(todayHistory);
                }
                if (name.equals("One Week")) {
                    addListInTable(WeekHistory);
                }
                if (name.equals("One Month")) {
                    addListInTable(MonthHistory);
                }
            }
        });
        addListInTable(fullHistory);
    }

    @FXML void deleteAll(MouseEvent event) {
        HistoryManagement.deleteAll();
        initializeListsWithData();
    }

    @FXML void deleteSelected(MouseEvent event) {

    }

    @FXML void SearchDataInTable(KeyEvent event) {
        System.out.println("searching.....");
        search.textProperty().addListener((o, oldVal, newVal) -> {
            table.setPredicate(HistoryStoreView -> HistoryStoreView.getValue().link.get().contains(newVal)
                    || HistoryStoreView.getValue().title.get().contains(newVal)
                    || HistoryStoreView.getValue().date.get().contains(newVal));
        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeListsWithData();
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
