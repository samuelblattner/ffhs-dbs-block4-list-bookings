package application;

import application.models.Booking;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


/**
 * Booking Table controller wrapper for the main bookings table.
 * Creats and initializes all columns and sets up the datasource.
 */
public class BookingTableController {

    private TableView<Booking> bookingTableView;

    /**
     * Constructor
     * @param tableView Reference to tableView control
     */
    BookingTableController(TableView<Booking> tableView) {
        this.bookingTableView = tableView;
        initColumns();
    }

    /**
     * Set the datasource
     * @param source Datasource
     */
    void setDataSource(ObservableList<Booking> source) {
        this.bookingTableView.setItems(source);
    }

    /**
     * Initialize table columns
     */
    private void initColumns() {
        TableColumn<Booking, Integer> bId = new TableColumn<>("id");
        bId.setMaxWidth(40);
        bId.setCellValueFactory((TableColumn.CellDataFeatures<Booking, Integer> param ) -> param.getValue().id.asObject());

        TableColumn<Booking, String> bFrom = new TableColumn<>("From");
        bFrom.setMinWidth(100);
        bFrom.setCellValueFactory((TableColumn.CellDataFeatures<Booking, String> param) -> param.getValue().checkin);

        TableColumn<Booking, String> bTo = new TableColumn<>("To");
        bTo.setMinWidth(100);
        bTo.setCellValueFactory((TableColumn.CellDataFeatures<Booking, String> param) -> param.getValue().checkout);

        TableColumn<Booking, String> bSurname = new TableColumn<>("Last Name");
        bSurname.setCellValueFactory((TableColumn.CellDataFeatures<Booking, String> param) -> param.getValue().surname);

        TableColumn<Booking, String> bForename = new TableColumn<>("First Name");
        bForename.setCellValueFactory((TableColumn.CellDataFeatures<Booking, String> param) -> param.getValue().forename);

        TableColumn<Booking, Boolean> bCancelled = new TableColumn<>("Canceled");
        bCancelled.setCellValueFactory((TableColumn.CellDataFeatures<Booking, Boolean> param) -> param.getValue().cancelled_at);

        this.bookingTableView.getColumns().setAll(bId, bFrom, bTo, bCancelled, bSurname, bForename);
    }

    /**
     * Set table to enabled/disabled for user
     * @param enabled
     */
    void setEnabled(boolean enabled) {
        this.bookingTableView.setDisable(!enabled);
    }
}
