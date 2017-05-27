package application;

import application.models.Booking;
import application.models.Room;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


/**
 * Booking Table controller wrapper for the main bookings table.
 * Creats and initializes all columns and sets up the datasource.
 */
public class AvailabilityTableController {

    private TableView<Room> availabilityTableView;

    /**
     * Constructor
     * @param tableView Reference to tableView control
     */
    AvailabilityTableController(TableView<Room> tableView) {
        this.availabilityTableView = tableView;
        initColumns();
    }

    /**
     * Set the datasource
     * @param source Datasource
     */
    void setDataSource(ObservableList<Room> source) {
        this.availabilityTableView.setItems(source);
    }

    /**
     * Initialize table columns
     */
    private void initColumns() {
        TableColumn<Room, Integer> bId = new TableColumn<>("id");
        bId.setMaxWidth(40);
        bId.setCellValueFactory((TableColumn.CellDataFeatures<Room, Integer> param ) -> param.getValue().id.asObject());

        TableColumn<Room, String> bName = new TableColumn<>("Name");
        bName.setMinWidth(200);
        bName.setCellValueFactory((TableColumn.CellDataFeatures<Room, String> param) -> param.getValue().name);

        TableColumn<Room, String> bDescription = new TableColumn<>("Description");
        bDescription.setMinWidth(400);
        bDescription.setCellValueFactory((TableColumn.CellDataFeatures<Room, String> param) -> param.getValue().description);

        TableColumn<Room, Integer> bRoomType = new TableColumn<>("Room Type");
        bRoomType.setCellValueFactory((TableColumn.CellDataFeatures<Room, Integer> param) -> param.getValue().room_type.asObject());

        this.availabilityTableView.getColumns().setAll(bId, bName, bDescription, bRoomType);
    }

    /**
     * Set table to enabled/disabled for user
     * @param enabled
     */
    void setEnabled(boolean enabled) {
        this.availabilityTableView.setDisable(!enabled);
    }
}
