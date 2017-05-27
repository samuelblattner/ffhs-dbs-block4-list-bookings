package application;

import application.interfaces.ControllerListener;
import application.models.RoomType;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

/**
 * Main FX Controller
 */
public class Controller {

    private static final String ERROR_MSG_FROM_AFTER_TO = "'from' date cannot be after 'to' date!";
    private static final String ERROR_MSG_TO_BEFORE_FROM = "'to' date cannot be before 'from' date!";

    private ControllerListener application;

    @FXML
    private Button btConnect;

    @FXML
    private DatePicker pickerDateFrom;

    @FXML
    private DatePicker pickerDateTo;

    @FXML
    private DatePicker pickerDateFromAvailability;

    @FXML
    private DatePicker pickerDateToAvailability;

    @FXML
    private ComboBox<RoomType> ddRoomType;

    /**
     * Sets up the menu and click listeners
     */
    private void setUpMenu() {
        btConnect.setOnAction((ActionEvent event) -> application.toggleDatabaseConnection());
    }

    private void setUpRoomType() {
        ddRoomType.valueProperty().addListener((ObservableValue<? extends RoomType> observable, RoomType oldValue, RoomType newValue) -> application.roomTypeChanged(newValue));
    }

    /**
     * Add date picker event handlers and validators. Always verify that from and to dates are
     * consistent and don't overlap.
     */
    private void setUpDateValidators(DatePicker fromDatePicker, DatePicker toDatePicker, String timeframeId) {

        fromDatePicker.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            LocalDate toDate = toDatePicker.getValue();
            LocalDate fromDate = fromDatePicker.getValue();
            if (toDate != null && newValue != null && newValue.isAfter(toDate)) {
                if (oldValue != null) {
                    fromDatePicker.setValue(oldValue);
                } else {
                    fromDatePicker.setValue(toDate);
                }
                showErrorBox(ERROR_MSG_FROM_AFTER_TO);
            } else {
                application.timeframeChanged(fromDate, toDate, timeframeId);
            }
        });

        toDatePicker.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            LocalDate toDate = toDatePicker.getValue();
            LocalDate fromDate = fromDatePicker.getValue();
            if (fromDate != null && newValue != null && newValue.isBefore(fromDate)) {

                if (oldValue != null) {
                    toDatePicker.setValue(oldValue);
                } else {
                    toDatePicker.setValue(fromDate);
                }
                showErrorBox(ERROR_MSG_TO_BEFORE_FROM);
            } else {
                application.timeframeChanged(fromDate, toDate, timeframeId);
            }
        });
    }


    /**
     * Utility method to show an error dialog
     * @param message Message to display.
     */
    private void showErrorBox(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.show();
    }

    /**
     * Attach the application to the controller for back reference.
     * @param application
     */
    void setApplication(ControllerListener application) {
        this.application = application;
        setUpMenu();
        setUpDateValidators(pickerDateFrom, pickerDateTo, "bookings");
        setUpDateValidators(pickerDateFromAvailability, pickerDateToAvailability, "availability");
        setUpRoomType();

    }
}
