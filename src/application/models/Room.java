package application.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by samuelblattner on 26.05.17.
 */
public class Room {
    public SimpleIntegerProperty id;
    public SimpleStringProperty name, description;
    public SimpleIntegerProperty room_type;

    public static Room mapSingleFromDatabase(ResultSet resultSet) throws SQLException {
        ArrayList<Room> list = Room.mapListFromDatabase(resultSet);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public static ArrayList<Room> mapListFromDatabase(ResultSet resultSet) throws SQLException {

        ArrayList<Room> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Room(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getInt("room_type_id")
            ));
        }
        return list;
    }

    public Room(int id, String name, String description, int room_type) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.room_type = new SimpleIntegerProperty(room_type);
    }

    @Override
    public String toString() {
        return this.name.getValue();
    }
}
