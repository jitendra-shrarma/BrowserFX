package controllers;

import browser.Browser;
import com.jfoenix.controls.*;
import database.BookmarksManagement;
import hub.Hub;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import resources.Resources;
import settings.SearchEngine;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.ResourceBundle;

public class TabController implements Initializable{

    @FXML private JFXTextField searchField;
    @FXML private Label bookmark;
    @FXML private Label backword;
    @FXML private Label forward;
    @FXML private Label refresh;
    @FXML private JFXDrawer drawer;
    @FXML private JFXProgressBar progressBar;
    @FXML private BorderPane borderpane;

    private WebView webView = new WebView();
    private WebEngine webEngine = webView.getEngine();
    private WebHistory webHistory = webEngine.getHistory();
    private Worker<Void> worker = webEngine.getLoadWorker();
    public static String searchEngine = SearchEngine.BING_ENGINE;
    private static URL Home = null;
    private String folderName = null;
    private String title = null;
    private String domain = null;
    private Tab tab = null;
    private PopOver popOver = new PopOver();
    private static Hub hub = new Hub();

    static {
        hub.start(new Stage());
    }

    public TabController(){
        // TODO Auto-generated constructor stub
    }

    @FXML void goBack(MouseEvent event) {
        System.out.println("Max size :" + webHistory.getEntries().size());
        System.out.println("Current index backword: " + webHistory.getCurrentIndex());
        webHistory.go(-1);
    }

    @FXML void goNext(MouseEvent event) {
        System.out.println("Max size :" + webHistory.getEntries().size());
        System.out.println("Current index forward: " + webHistory.getCurrentIndex());
        webHistory.go(1);
    }

    @FXML void refreshPage(MouseEvent event) {
        if(webEngine.getLoadWorker().isRunning()){
            webEngine.getLoadWorker().cancel();
        }else {
            webEngine.reload();
        }
    }

    private void refreshOrCancel(boolean b){
        if (b) {
            refresh.getStyleClass().add("cancel-button");
        } else {
            refresh.getStyleClass().remove("cancel-button");
        }
    }

    @FXML void goBackHome(MouseEvent event) {
        pageRender("");
    }

    public void setPopOver() {
        try {
            popOver.setContentNode(FXMLLoader.load(getClass().getResource(Resources.FXML + "BookmarkPop.fxml")));
            popOver.setDetachable(false);
            popOver.setAutoHide(true);
            popOver.setCornerRadius(0);
            popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        } catch (IOException e){
        System.out.println("BookmarkPop.fxml error");
        }
    }

    @FXML void addBookmark(MouseEvent event) {
        if(BookmarksManagement.isBookmarked(domain, folderName, title)){
            showBookmarked(domain, folderName, title);
            bookmark.getStyleClass().add("bookmarked");
        }
        popOver.show(bookmark);
    }

    @FXML private JFXTextField name;
    @FXML private JFXComboBox<String> folderBox;
    @FXML private JFXButton newFolder;
    @FXML private JFXButton save;
    @FXML private JFXButton cancel;
    private ObservableList<String> options;

    @FXML void cancelOrRemove(MouseEvent event) {

    }

    @FXML void createFolder(MouseEvent event) {
        TextInputDialog dialog = new TextInputDialog("All Bookmarks");

        dialog.setTitle("Create New Folder");
        dialog.setHeaderText("Create New Folder");
        dialog.setContentText("Please enter folder name:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(input->{
            if(input != null){
                if(!options.contains(input)){
                    options.add(input);
                }
                folderBox.getSelectionModel().select(input);
                save(event);
            }else{
                Notifications notify = Notifications.create().title("BookMark Folder")
                        .text("No Folder specified.")
                        .hideAfter(Duration.seconds(10))
                        .position(Pos.BOTTOM_RIGHT);
                notify.darkStyle();
                notify.showInformation();
            }
        });
    }

    @FXML void save(MouseEvent event) {
        title = name.getText();
        folderName = folderBox.getSelectionModel().getSelectedItem();
        BookmarksManagement.insert(domain,folderName,title);
    }

    public void showBookmarked(String domain, String folderName, String title){
        this.folderName = folderName;
        this.domain = domain;
        this.title = title;

        name.setText(title);
        folderBox.getSelectionModel().select(folderName);
        cancel.setText("delete");
    }

    @FXML void search(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            searchField.getText().trim();
            pageRender(searchField.getText());
        }
    }

    @FXML void openHub(MouseEvent event){
        hub.show();
    }

    public void pageRender(String url) {
        webEngine.load(searchEngine + url);
    }

    private void webViewBehaviour(){
        webView.setMinSize(600, 350);
        webView.setMaxSize(1366, 750);
        borderpane.setCenter(webView);
    }

    private void engineBehaviour(){
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue observableValue, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    System.out.println(newState.toString());
                    searchField.setText(webEngine.getLocation());
                    domain = null;
                    if (!(webEngine.getLocation().equals("about:blank"))) {
                            domain = webEngine.getLocation();
                    }
                    title = webEngine.getTitle();
                    refreshOrCancel(false);
                    if(title == null){
                        title = domain.toString();
                    }
                    tab.setText(title);
                    tab.setGraphic(getWebIcon());
                } else if(newState == Worker.State.CANCELLED){
                    System.out.println(newState.toString());/*--delete--*/
                    refreshOrCancel(false);
                } else if(newState == Worker.State.RUNNING){
                    System.out.println(newState.toString());/*--delete--*/
                    refreshOrCancel(true);
                } else if(newState == Worker.State.READY){
                    System.out.println(newState.toString());/*--delete--*/
                } else if(newState == Worker.State.FAILED){
                    System.out.println(newState.toString());/*--delete--*/
                    refreshOrCancel(false);
                    title = "You're Not Connected";
                    tab.setText(title);
                    tab.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Resources.IMAGES + "unable.png"))));
                }
            }
        });
        backword.setDisable(true);
        forward.setDisable(true);
        webEngine.getHistory().currentIndexProperty().addListener(event->{
            if (webHistory.getCurrentIndex() == 0) {
                backword.setDisable(true);
                forward.setDisable(true);
                if(webHistory.getEntries().size()>1){
                    forward.setDisable(false);
                }
            } else if (webHistory.getCurrentIndex() > 0) {
                backword.setDisable(false);
                forward.setDisable(false);
            }

            if ((webHistory.getCurrentIndex()+1) == webHistory.getEntries().size()) {
                forward.setDisable(true);
            }
        });
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
        try {
            tab = Browser.tabPane.getSelectionModel().getSelectedItem();
            engineBehaviour();
            progressBar.progressProperty().bind(worker.progressProperty());
            pageRender("");
            webViewBehaviour();
            setPopOver();
        }catch (Exception e){
            System.out.println("Tab Component Error");
        }
    }

    public ImageView getWebIcon(){
        ImageView imageView = null;
        try {
            String faviconUrl = String.format("http://www.google.com/s2/favicons?domain_url=%s", URLEncoder.encode(domain, "UTF-8"));
            Image favicon = new Image(faviconUrl, true);
            imageView = new ImageView(favicon);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return imageView;
    }
}