package application;

import application.enums.DatabaseState;
import application.interfaces.DatabaseListener;
import application.models.Booking;
import application.models.RoomType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Main Database Handler.
 */
public class Database {

    private static final String
            dbDriver = "com.mysql.jdbc.Driver",
            dbConnectionStatement = "jdbc:mysql://localhost:3306/starview?verifyServerCertificate=false&useSSL=false",
            dbUser = "root",
            dbPass = "root";

    private Connection connection;
    private HashMap<String, PreparedStatement> preparedStatements;
    private DatabaseState curState = DatabaseState.DISCONNECTED;

    private HashSet<DatabaseListener> observers;

    /**
     * Constructor
     */
    public Database() {
        this.observers = new HashSet<>();
    }

    /**
     * Utility method to set the state of the Database Instance. Notifies all
     * Observers on the new DatabaseState
     *
     * @param newState New DatabaseState to set
     */
    private void setState(DatabaseState newState) {
        this.curState = newState;
        for (DatabaseListener l : this.observers) {
            l.databaseStateChanged(this.curState);
        }
    }

    /**
     * Helper method to verify that the JDBC Driver is successfully installed and instantiable.
     *
     * @return {boolean} True if Driver ok
     */
    private boolean verifyDriver() {
        try {
            Class.forName(dbDriver).newInstance();
            return true;
        } catch (Exception e) {
            System.out.format(
                    "No JDBC Driver found. Make sure that the driver package is compiled with this program! Stack trace: %s");
            return false;
        }
    }

    /**
     * PreparedStatement setup. Prepared statements are put into a HashMap so that they
     * can be accessed easily by key/value later on. Note: This method must not be called unless
     * a connection has been established.
     */
    private void prepareStatements() {

        String baseStatement = "" +
                "SELECT b.id, b.checkin, b.checkout, b.cancelled_at, p.surname, p.forename " +
                "FROM booking b INNER JOIN booking_person bp ON b.id = bp.booking_id INNER JOIN " +
                "person p ON bp.person_id = p.id WHERE bp.isResponsible = TRUE ";

        preparedStatements = new HashMap<>();
        try {
            preparedStatements.put("get-all-bookings", connection.prepareStatement(baseStatement));
            preparedStatements.put("get-timeframe-from-bookings", connection.prepareStatement(baseStatement + "AND checkin >= ?"));
            preparedStatements.put("get-timeframe-to-bookings", connection.prepareStatement(baseStatement + "AND checkout <= ?"));
            preparedStatements.put("get-timeframe-both-bookings", connection.prepareStatement(baseStatement + "AND checkin >= ? AND checkout <= ?"));
            preparedStatements.put("get-roomtypes", connection.prepareStatement("SELECT * FROM room_type"));

        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
        }
    }

    /**
     * Returns True if database is connected
     *
     * @return True if connected
     */
    boolean isConnected() {
        return this.connection != null;
    }

    /**
     * Adds a DatabaseListener as observer. Observers get notified upon DatabaseState changes.
     *
     * @param observer Observer to add
     */
    void addDatabaseObserver(DatabaseListener observer) {
        this.observers.add(observer);
    }

    /**
     * Removes a DatabaseListener observer from the list.
     *
     * @param observer Observer to remove
     */
    public void removeDatabaseObserver(DatabaseListener observer) {
        this.observers.remove(observer);
    }

    /**
     * Establishes a connection to the Mysql database
     *
     * @return {boolean} True if connection successful
     */
    boolean connect() {
        if (verifyDriver()) {
            this.setState(DatabaseState.CONNECTING);
            try {
                connection = DriverManager.getConnection(dbConnectionStatement, dbUser, dbPass);
                prepareStatements();
                this.setState(DatabaseState.CONNECTED);
                return true;
            } catch (SQLException e) {
                this.setState(DatabaseState.CONNECTION_FAILED);
                return false;
            }
        } else {
            this.setState(DatabaseState.CONNECTION_FAILED);
            return false;
        }
    }

    /**
     * Closes an existing connection to the database
     *
     * @return {boolean} True if disconnect successful
     */
    boolean disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                setState(DatabaseState.DISCONNECTED);
                return true;
            } catch (SQLException e) {
                return false;
            }
        }

        return false;
    }

    /**
     * Retrieves all bookings from the database
     *
     * @return ArrayList<Booking> List with bookings
     */
    ArrayList<Booking> getBookingsList() {
        PreparedStatement prep = preparedStatements.get("get-all-bookings");
        try {
            return Booking.mapListFromDatabase(prep.executeQuery());
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Returns a list of all bookings within a specific timeframe.
     *
     * @param fromDate Checkin date has to be equal or greater
     * @param toDate   Checkout date has to be equal or less
     * @return ArrayList<Booking> List of bookings
     */
    ArrayList<Booking> getBookingsList(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null && toDate == null) {
            return getBookingsList();
        } else {

            PreparedStatement prep;

            try {
                if (fromDate != null && toDate != null) {
                    prep = preparedStatements.get("get-timeframe-both-bookings");
                    prep.setString(1, fromDate.toString());
                    prep.setString(2, toDate.toString());
                } else if (toDate != null) {
                    prep = preparedStatements.get("get-timeframe-to-bookings");
                    prep.setString(1, toDate.toString());
                } else {
                    prep = preparedStatements.get("get-timeframe-from-bookings");
                    prep.setString(1, fromDate.toString());
                }

                return Booking.mapListFromDatabase(prep.executeQuery());
            } catch (SQLException e) {
                return null;
            }
        }
    }

    ArrayList<RoomType> getRoomTypes() {
        PreparedStatement prep = preparedStatements.get("get-roomtypes");
        try {
            return RoomType.mapListFromDatabase(prep.executeQuery());
        } catch (SQLException e) {
            return null;
        }
    }

}
