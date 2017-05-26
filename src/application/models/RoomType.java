package application.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by samuelblattner on 26.05.17.
 */
public class RoomType {
    public SimpleIntegerProperty id;
    public SimpleStringProperty name, description;
    public SimpleIntegerProperty maximum_number_of_guests;

    public static RoomType mapSingleFromDatabase(ResultSet resultSet) throws SQLException {
        ArrayList<RoomType> list = RoomType.mapListFromDatabase(resultSet);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public static ArrayList<RoomType> mapListFromDatabase(ResultSet resultSet) throws SQLException {

        ArrayList<RoomType> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new RoomType(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getInt("maximum_number_of_guests")
            ));
        }
        return list;
    }

    public RoomType(int id, String name, String description, int maximum_number_of_guests) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.maximum_number_of_guests = new SimpleIntegerProperty(maximum_number_of_guests);
    }
}
