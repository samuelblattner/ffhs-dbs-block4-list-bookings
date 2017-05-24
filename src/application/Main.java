package application;

import application.enums.DatabaseState;
import application.interfaces.ControllerListener;
import application.interfaces.DatabaseListener;
import application.models.Booking;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.time.LocalDate;


/**
 * Main Application
 */
public class Main extends Application implements DatabaseListener, ControllerListener {

    private static final int PREF_WIDTH = 600, PREF_HEIGHT = 400;
    private static final String
            WINDOW_TITLE = "List bookings",
            DATABASE_CONNECTING = "Connecting...",
            DATABASE_CONNECTED = "Connected",
            DATABASE_DISCONNECTED = "Disconnected",
            DATABASE_CONNECTION_FAILED = "Connection failed!",
            BT_CONNECT_CONNECT = "Connect",
            BT_CONNECT_DISCONNECT = "Disconnect";

    private Stage primaryStage;
    private Database database;

    private BookingTableController bookingTableController;
    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();

    /**
     * Initializer
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("mainWindow.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(new Scene(root, PREF_WIDTH, PREF_HEIGHT));
        primaryStage.show();
        this.primaryStage = primaryStage;

        setUpGUI();
        setUpDatabase();
        ((Controller) loader.getController()).setApplication(this);
    }

    /**
     * Set up GUI elements and Booking table
     * @throws Exception
     */
    private void setUpGUI() throws Exception {
        GridPane mainPane = (GridPane) primaryStage.getScene().lookup("#mainPane");
        mainPane.setPrefWidth(PREF_WIDTH);
        mainPane.setPrefHeight(PREF_HEIGHT);

        bookingTableController = new BookingTableController((TableView<Booking>) primaryStage.getScene().lookup("#tblMainTable"));

        setInterfaceEnabled(false);
    }

    /**
     * Set up Database
     * @throws Exception
     */
    private void setUpDatabase() throws Exception {
        database = new Database();
        database.addDatabaseObserver(this);
    }

    /**
     * Utility method to enable/disable user interface
     * @param enabled
     */
    private void setInterfaceEnabled(boolean enabled) {
        DatePicker pickerFrom = (DatePicker) primaryStage.getScene().lookup("#pickerDateFrom");
        DatePicker pickerTo= (DatePicker) primaryStage.getScene().lookup("#pickerDateTo");
        pickerFrom.setDisable(!enabled);
        pickerTo.setDisable(!enabled);
        bookingTableController.setEnabled(enabled);
    }

    /**
     * Main entry point
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Deinitializer, release database.
     */
    @Override
    public void stop() {
        database.disconnect();
    }


    /* -----------------------
    DatabaseListener methods
    ------------------------ */
    /**
     * Handler for database state changes.
     * @param state The new state the database controller is in.
     */
    @Override
    public void databaseStateChanged(DatabaseState state) {
        Label lbDatabaseState = (Label) primaryStage.getScene().lookup("#lbDatabaseState");
        Button btConnect= (Button) primaryStage.getScene().lookup("#btConnect");

        switch (state) {
            case CONNECTING:
                lbDatabaseState.setText(DATABASE_CONNECTING);
                setInterfaceEnabled(false);
                btConnect.setDisable(true);
                break;
            case CONNECTION_FAILED:
                lbDatabaseState.setText(DATABASE_CONNECTION_FAILED);
                setInterfaceEnabled(false);
                btConnect.setDisable(false);
                break;
            case CONNECTED:
                lbDatabaseState.setText(DATABASE_CONNECTED);
                bookingList.setAll(database.getBookingsList());
                bookingTableController.setDataSource(bookingList);
                setInterfaceEnabled(true);
                btConnect.setDisable(false);
                btConnect.setText(BT_CONNECT_DISCONNECT);
                break;
            default:
                lbDatabaseState.setText(DATABASE_DISCONNECTED);
                setInterfaceEnabled(false);
                btConnect.setDisable(false);
                btConnect.setText(BT_CONNECT_CONNECT);
                bookingList.clear();
        }
    }

    /* -----------------------
    ControllerListener methods
    ------------------------ */
    /**
     *
     * @param from From date for new timeframe
     * @param to To date for new timeframe
     */
    @Override
    public void timeframeChanged(LocalDate from, LocalDate to) {
        bookingList.setAll(database.getBookingsList(from, to));
    }

    /**
     * Connect / Disconnect
     */
    @Override
    public void toggleDatabaseConnection() {
        if (database.isConnected()) {
            database.disconnect();
        } else {
            database.connect();
        }
    }
}
