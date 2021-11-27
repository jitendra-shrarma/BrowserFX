/*
* MIT License
*
* Copyright (c) 2018 majestic_gohan
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package browser;

import BrowserStage.Stage.SceneFX;
import database.BookmarksManagement;
import database.HistoryManagement;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tabpanefx.TabPaneFX;

/** @author God-Hand */
public class Browser extends Application {

    private int Spacer = 196;
    private SceneFX sceneFX;
    private StackPane root;
    public static TabPaneFX tabPane = new TabPaneFX();
    public static Button addTab;
    private AnchorPane controlPane;

    /** create new Tab and add the content in it.
     * it change tab selection*/
    public void createNewTab(){
        Tab tab = new Tab("NEW TAB");
        tab.setClosable(true);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        try {
            tab.setContent(FXMLLoader.load(getClass().getResource("/resources/fxml/Tab.fxml")));
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /** add behavior for add button and root size*/
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
                if (tabPane.getTabs().size() == 0) {
                    Platform.exit();
                }
                Spacer-=196;
                controlPane.setPrefWidth(root.getWidth()-Spacer);
            }
        });

        //bind width of control pane with root pane
        root.widthProperty().addListener(change->{
            controlPane.setPrefWidth(root.getWidth()-Spacer);
        });
    }

    /** initialize tabPane size and add tabPane in root*/
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

    /**launch application and create database*/
    public static void main(String[] args) {
        HistoryManagement.create();
        BookmarksManagement.create();
        launch(args);
        System.exit(0);
    }
}