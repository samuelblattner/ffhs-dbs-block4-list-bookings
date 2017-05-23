package application;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalDate;

public class Controller {

    private static String ERROR_MSG_FROM_AFTER_TO = "'from' date cannot be after 'to' date!";
    private static String ERROR_MSG_TO_BEFORE_FROM = "'to' date cannot be before 'to' date!";

    private DatePicker pickerDateFrom;
    private DatePicker pickerDateTo;
    private LocalDate fromDate, toDate;

    private ChangeListener fromDateValidator = new ChangeListener<LocalDate>() {
        @Override
        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
            if(toDate != null && newValue.isAfter(toDate)) {
                pickerDateFrom.setValue(oldValue);
//                showErrorBox(ERROR_MSG_FROM_AFTER_TO);
            } else {
                fromDate = newValue;
            }
        }
    };

    private ChangeListener toDateValidator = new ChangeListener<LocalDate>() {
        @Override
        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
            if(fromDate != null && newValue.isBefore(fromDate)) {
                pickerDateTo.setValue(oldValue);
//                showErrorBox(ERROR_MSG_TO_BEFORE_FROM);
            } else {
                toDate = newValue;
            }
        }
    };

    public Controller() {

    }

    public Controller(Stage stage) {
        pickerDateFrom = (DatePicker) stage.getScene().lookup("#dateFrom");
        pickerDateTo = (DatePicker) stage.getScene().lookup("#dateTo");

        pickerDateFrom.valueProperty().addListener(this.fromDateValidator);
        pickerDateTo.valueProperty().addListener(this.toDateValidator);

    }

    public void showErrorBox(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.show();

    }

}
