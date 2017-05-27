package application.interfaces;

import application.models.RoomType;

import java.time.LocalDate;

/**
 * Interface for Controller observers
 */
public interface ControllerListener {
    void bookingsTimeframeChanged(LocalDate from, LocalDate to);
    void toggleDatabaseConnection();
    void timeframeChanged(LocalDate from, LocalDate to, String timeframeId);
    void roomTypeChanged(RoomType roomType);

}
