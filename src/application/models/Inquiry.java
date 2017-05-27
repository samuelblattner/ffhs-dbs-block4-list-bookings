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
public class Inquiry {
    public SimpleIntegerProperty id, number_of_guests, person_id;
    public SimpleStringProperty group_name, from, to, created_at, reservation_until, cancelled_at;

    public static Inquiry mapSingleFromDatabase(ResultSet resultSet) throws SQLException {
        ArrayList<Inquiry> list = Inquiry.mapListFromDatabase(resultSet);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public static ArrayList<Inquiry> mapListFromDatabase(ResultSet resultSet) throws SQLException {

        ArrayList<Inquiry> list = new ArrayList<>();

        while (resultSet.next()) {
            list.add(new Inquiry(
                resultSet.getInt("id"),
                resultSet.getString("group_name"),
                resultSet.getDate("from"),
                resultSet.getDate("to"),
                resultSet.getInt("number_of_guests"),
                resultSet.getDate("created_at"),
                resultSet.getDate("reservation_until"),
                resultSet.getDate("cancelled_at"),
                resultSet.getInt("person_id")
            ));
        }

        return list;
    }

    public Inquiry(int id, String group_name, Date from, Date to, int number_of_guests, Date created_at, Date reservation_until, Date cancelled_at, int person_id) {
        this.id = new SimpleIntegerProperty(id);
        this.group_name = new SimpleStringProperty(group_name);
        this.number_of_guests = new SimpleIntegerProperty(number_of_guests);
        this.from = new SimpleStringProperty(from.toString());
        this.to = new SimpleStringProperty(to.toString());
        this.created_at = new SimpleStringProperty(created_at.toString());
        this.reservation_until = new SimpleStringProperty(reservation_until.toString());
        this.cancelled_at = new SimpleStringProperty("");
        this.person_id = new SimpleIntegerProperty(person_id);
    }
}
