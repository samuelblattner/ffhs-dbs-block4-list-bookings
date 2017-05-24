package application.interfaces;

import java.time.LocalDate;

/**
 * Interface for Controller observers
 */
public interface ControllerListener {
    void timeframeChanged(LocalDate from, LocalDate to);
    void toggleDatabaseConnection();
}
