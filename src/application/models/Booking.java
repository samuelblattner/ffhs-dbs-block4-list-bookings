package application.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Model for Bookings. Used to display table rows.
 */
public class Booking {
    public SimpleIntegerProperty id;
    public SimpleStringProperty checkin, checkout, surname, forename;
    public SimpleBooleanProperty cancelled_at;

    public static Booking mapSingleFromDatabase(ResultSet resultSet) throws SQLException {
        ArrayList<Booking> list = Booking.mapListFromDatabase(resultSet);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public static ArrayList<Booking> mapListFromDatabase(ResultSet resultSet) throws SQLException {

        ArrayList<Booking> list = new ArrayList<>();

        while (resultSet.next()) {
            list.add(new Booking(
                resultSet.getInt("id"),
                resultSet.getDate("checkin"),
                resultSet.getDate("checkout"),
                resultSet.getBoolean("cancelled_at"),
                resultSet.getString("surname"),
                resultSet.getString("forename")
            ));
        }

        return list;
    }

    public Booking(int id, Date checkin, Date checkout, boolean cancelled_at, String surname, String forename) {
        this.id = new SimpleIntegerProperty(id);
        this.checkin = new SimpleStringProperty(checkin.toString());
        this.checkout = new SimpleStringProperty(checkout.toString());
        this.cancelled_at = new SimpleBooleanProperty(cancelled_at);
        this.surname = new SimpleStringProperty(surname);
        this.forename = new SimpleStringProperty(forename);
    }
}
