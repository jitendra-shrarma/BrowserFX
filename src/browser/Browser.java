package browser;

import BrowserStage.Stage.SceneFX;
import database.BookmarksManagement;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import resources.Resources;
import tabpanefx.TabPaneFX;

import java.io.IOException;

/**
 * @author God-Hand
 */
public class Browser extends Application {

    private int Spacer = 196;
    private SceneFX sceneFX;
    private StackPane root;
    public static TabPaneFX tabPane = new TabPaneFX();
    public static Button addTab;
    private AnchorPane controlPane;

    /**
     * create new Tab and add the content in it.
     * it change tab selection
     */
    public void createNewTab(){
        Tab tab = new Tab("NEW TAB");
        tab.setClosable(true);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        try { tab.setContent(FXMLLoader.load(getClass().getResource(Resources.FXML+"Tab.fxml")));
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * add behavior for add button and root size
     */
    private void addBehavior(){
        addTab = sceneFX.getStageFX().addButton;
        controlPane = sceneFX.getStageFX().controlPane;

        //add new Tab on clicking addTab button
        addTab.setOnAction(e->{
            createNewTab();
            Spacer+=196;
            controlPane.setPrefWidth(root.getWidth()-Spacer);
        });

        //resize control pane on removal of a tab
        tabPane.getTabs().addListener((ListChangeListener<Tab>) change->{
            if(change.next() && change.wasRemoved()){
                Spacer-=196;
                controlPane.setPrefWidth(root.getWidth()-Spacer);
            }
        });

        //bind width of control pane with root pane
        root.widthProperty().addListener(change->{
            controlPane.setPrefWidth(root.getWidth()-Spacer);
        });
    }

    public void createBrowserStage(){
        tabPane.setTabMinWidth(180);
        tabPane.setTabMaxWidth(180);
        tabPane.setTabMinHeight(20);
        tabPane.autosize();
        addBehavior();
        createNewTab();
        root.getChildren().add(tabPane);
    }

    @Override
    public void start(Stage primaryStage) {
        // create new StackPane for root node
        root = new StackPane();
        root.setMinSize(700,450);
        root.setPrefSize(700,450);

        sceneFX = new SceneFX(primaryStage, root);
        createBrowserStage();
        sceneFX.setFadeInTransition();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                we.consume();   // Do not hide yet
                sceneFX.setFadeOutTransition();
            }
        });
        primaryStage.setScene(sceneFX);
        primaryStage.toFront();
        primaryStage.show();
    }

    public static void main(String[] args) {
        BookmarksManagement.createDataBase();
        launch(args);
    }
}