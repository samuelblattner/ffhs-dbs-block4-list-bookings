package application;

import application.enums.DatabaseState;
import application.interfaces.ControllerListener;
import application.interfaces.DatabaseListener;
import application.models.Booking;
import application.models.Inquiry;
import application.models.Room;
import application.models.RoomType;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDate;


/**
 * Main Application
 */
public class Main extends Application implements DatabaseListener, ControllerListener {

    private static final int PREF_WIDTH = 1200, PREF_HEIGHT = 600;
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

    // FX Stuff


    private BookingTableController bookingTableController;
    private ListView<Inquiry> inquiryTable;
    private Inquiry draggedInquiry;
    private AvailabilityTableController availabilityTableController;
    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    private ObservableList<RoomType> roomTypes = FXCollections.observableArrayList();
    private ObservableList<Room> availableRoomList = FXCollections.observableArrayList();
    private ObservableList<Inquiry> inquiryList = FXCollections.observableArrayList();

    /**
     * Initializer
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;

        setUpGUI();
        setUpDatabase();
    }

    /**
     * Set up GUI elements and Booking table
     * @throws Exception
     */
    private void setUpGUI() throws Exception {

        FXMLLoader loader = new FXMLLoader();
        URL mainWindowURL = getClass().getResource("scenes/mainWindow.fxml");

        loader.setLocation(mainWindowURL);
        BorderPane mainWindow = loader.load();
        ((Controller) loader.getController()).setApplication(this);

        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(new Scene(mainWindow, PREF_WIDTH, PREF_HEIGHT));
        primaryStage.show();

        bookingTableController = new BookingTableController((TableView<Booking>) primaryStage.getScene().lookup("#tblMainTable"));
        availabilityTableController = new AvailabilityTableController((TableView<Room>) primaryStage.getScene().lookup("#tblAvailabilityTable"));
        inquiryTable = (ListView<Inquiry>) primaryStage.getScene().lookup("#tblInquiry");

        setUpDragAndDropInquiries();
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
        DatePicker pickerFromAvail = (DatePicker) primaryStage.getScene().lookup("#pickerDateFromAvailability");
        DatePicker pickerFrom = (DatePicker) primaryStage.getScene().lookup("#pickerDateFrom");
        DatePicker pickerToAvail = (DatePicker) primaryStage.getScene().lookup("#pickerDateToAvailability");
        DatePicker pickerTo= (DatePicker) primaryStage.getScene().lookup("#pickerDateTo");
        ComboBox<RoomType> ddRoomType = (ComboBox<RoomType>) primaryStage.getScene().lookup("#ddRoomType");
        pickerFrom.setDisable(!enabled);
        pickerTo.setDisable(!enabled);
        pickerFromAvail.setDisable(!enabled);
        pickerToAvail.setDisable(!enabled);
        ddRoomType.setDisable(!enabled);
        bookingTableController.setEnabled(enabled);
        availabilityTableController.setEnabled(enabled);
    }

    /**
     * Populate room type dropdown with RoomTypes
     */
    private void populateRoomTypeDropDown() {
        ComboBox<RoomType> roomTypeDropDown = (ComboBox<RoomType>) primaryStage.getScene().lookup("#ddRoomType");

        roomTypes.add(new RoomType(
                0, "Any", "", 0
        ));
        roomTypes.addAll(database.getRoomTypes());
        roomTypeDropDown.setItems(roomTypes);
        roomTypeDropDown.setCellFactory((ListView<RoomType> listView) ->
            new ListCell<RoomType>() {
                @Override
                public void updateItem(RoomType item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        setText(item.name.getValue());
                    }
                }
            });

        roomTypeDropDown.setValue(roomTypes.get(0));
    }

    private void setUpDragAndDropInquiries() {


        Label dropZone = (Label) primaryStage.getScene().lookup("#lbDropZone");

            dropZone.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });

        dropZone.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                DatePicker pickerFromAvail = (DatePicker) primaryStage.getScene().lookup("#pickerDateFromAvailability");
                DatePicker pickerToAvail = (DatePicker) primaryStage.getScene().lookup("#pickerDateToAvailability");

                pickerFromAvail.setValue(LocalDate.parse(draggedInquiry.from.getValue()));
                pickerToAvail.setValue(LocalDate.parse(draggedInquiry.to.getValue()));

                Label dropZone = (Label) primaryStage.getScene().lookup("#lbDropZone");
                dropZone.setText(draggedInquiry.group_name.getValue());
            }
        });
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
                inquiryList.setAll(database.getInquiries());
                System.out.format("HAVE %d", inquiryList.size());
                inquiryTable.setItems(inquiryList);
                bookingTableController.setDataSource(bookingList);
                availabilityTableController.setDataSource(availableRoomList);
                setInterfaceEnabled(true);
                btConnect.setDisable(false);
                btConnect.setText(BT_CONNECT_DISCONNECT);
                populateRoomTypeDropDown();
                inquiryTable.setCellFactory((ListView<Inquiry> listView) -> {

                    ListCell<Inquiry> cell = new ListCell<Inquiry>() {
                        @Override
                        public void updateItem(Inquiry item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!empty) {
                                setText(item.group_name.getValue());
                            }
                        }
                    };

                    cell.setOnDragDetected(event -> {
                        if (!cell.isEmpty()) {
                            Dragboard db = cell.startDragAndDrop(TransferMode.ANY);
                            ClipboardContent cc = new ClipboardContent();
                            cc.putString("Hello");
                            db.setContent(cc);
                            draggedInquiry = cell.getItem();

                        }
                    });

                    return cell;
                });

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
    public void bookingsTimeframeChanged(LocalDate from, LocalDate to) {
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

    @Override
    public void timeframeChanged(LocalDate from, LocalDate to, String timeframeId) {
        switch(timeframeId) {
            case "bookings":
                bookingList.setAll(database.getBookingsList(from, to));
                break;
            case "availability":
                System.out.println("CHANGEd");
                ComboBox ddRoomType = (ComboBox) primaryStage.getScene().lookup("#ddRoomType");
                availableRoomList.setAll(database.getAvailableRooms(from, to, (RoomType) ddRoomType.getValue()));
                break;
        }
    }

    @Override
    public void roomTypeChanged(RoomType roomType) {
        DatePicker pickerFromAvail = (DatePicker) primaryStage.getScene().lookup("#pickerDateFromAvailability");
        DatePicker pickerToAvail = (DatePicker) primaryStage.getScene().lookup("#pickerDateToAvailability");
        availableRoomList.setAll(database.getAvailableRooms(pickerFromAvail.getValue(), pickerToAvail.getValue(), (RoomType) roomType));
    }
}
