package application;

import application.interfaces.ControllerListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

/**
 * Main FX Controller
 */
public class Controller {

    private static final String ERROR_MSG_FROM_AFTER_TO = "'from' date cannot be after 'to' date!";
    private static final String ERROR_MSG_TO_BEFORE_FROM = "'to' date cannot be before 'from' date!";

    private LocalDate fromDate, toDate;
    private ControllerListener application;

    @FXML
    private Button btConnect;

    @FXML
    private DatePicker pickerDateFrom;

    @FXML
    private DatePicker pickerDateTo;

    /**
     * Sets up the menu and click listeners
     */
    private void setUpMenu() {
        btConnect.setOnAction((ActionEvent event) -> application.toggleDatabaseConnection());
    }

    /**
     * Add date picker event handlers and validators. Always verify that from and to dates are
     * consistent and don't overlap.
     */
    private void setUpDateValidators() {
        pickerDateFrom.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (toDate != null && newValue != null && newValue.isAfter(toDate)) {
                if (oldValue != null) {
                    pickerDateFrom.setValue(oldValue);
                } else {
                    pickerDateFrom.setValue(toDate);
                }
                showErrorBox(ERROR_MSG_FROM_AFTER_TO);
            } else {
                fromDate = newValue;
                application.timeframeChanged(fromDate, toDate);

            }
        });

        pickerDateTo.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
                    if (fromDate != null && newValue != null && newValue.isBefore(fromDate)) {

                        if (oldValue != null) {
                            pickerDateTo.setValue(oldValue);
                        } else {
                            pickerDateTo.setValue(fromDate);
                        }
                        showErrorBox(ERROR_MSG_TO_BEFORE_FROM);
                    } else {
                        toDate = newValue;
                        application.timeframeChanged(fromDate, toDate);
                    }
                }
        );
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
        setUpDateValidators();
    }
}
