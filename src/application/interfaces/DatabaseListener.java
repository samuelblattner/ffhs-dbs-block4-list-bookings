package application.interfaces;

import application.enums.DatabaseState;

/**
 * Interface for database observers
 */
public interface DatabaseListener {
    void databaseStateChanged(DatabaseState state);
}
