package application.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

/**
 * Model for Bookings. Used to display table rows.
 */
public class Booking {
    public SimpleIntegerProperty id;
    public SimpleStringProperty checkin, checkout, surname, forename;
    public SimpleBooleanProperty cancelled_at;

    public Booking(int id, Date checkin, Date checkout, boolean cancelled_at, String surname, String forename) {
        this.id = new SimpleIntegerProperty(id);
        this.checkin = new SimpleStringProperty(checkin.toString());
        this.checkout = new SimpleStringProperty(checkout.toString());
        this.cancelled_at = new SimpleBooleanProperty(cancelled_at);
        this.surname = new SimpleStringProperty(surname);
        this.forename = new SimpleStringProperty(forename);
    }
}
