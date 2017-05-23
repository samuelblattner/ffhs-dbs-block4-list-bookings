package application;

import application.interfaces.DatabaseListener;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;

public class Main extends Application implements DatabaseListener {

    private static int PREF_WIDTH = 600, PREF_HEIGHT = 400;
    private static String WINDOW_TITLE = "List bookings";

    private Stage primaryStage;
    private GridPane mainPane;
    private Controller controller;


    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(new Scene(root, PREF_WIDTH, PREF_HEIGHT));
        primaryStage.show();
        this.primaryStage = primaryStage;

        setUpGUI();
        controller = new Controller(this.primaryStage);
        connectToDatabase();
    }

    public void setUpGUI() throws Exception {
        mainPane = (GridPane) primaryStage.getScene().lookup("#mainPane");
        mainPane.setPrefWidth(PREF_WIDTH);
        mainPane.setPrefHeight(PREF_HEIGHT);
    }

    public void connectToDatabase() throws Exception {
        databaseConnectionSuccessful();
    }


    public static void main(String[] args) {
        launch(args);
    }

    /* -----------------------
    DatabaseListener methods
    ------------------------ */
    @Override
    public void databaseConnectionSuccessful() {
        mainPane.setDisable(false);
    }

    @Override
    public void databaseConnectionFailed() {

    }


}
