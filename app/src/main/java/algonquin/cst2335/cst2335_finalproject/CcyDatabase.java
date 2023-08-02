package algonquin.cst2335.cst2335_finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * CcyDatabase is a Room database class used for managing the currency converter's data.
 * It extends RoomDatabase and defines the entities and version for the database.
 * This database stores instances of the CcyListItem class as entities.
 *
 * @see androidx.room.RoomDatabase
 * @see algonquin.cst2335.cst2335_finalproject.CcyListItem
 * @see algonquin.cst2335.cst2335_finalproject.CcyListItemDAO
 * @version 1.0
 */
@Database(entities = {CcyListItem.class},version = 1)
public abstract class CcyDatabase extends RoomDatabase {
    /**
     * Abstract method to get the Data Access Object (DAO) for performing database operations.
     *
     * @return The Data Access Object (DAO) for the CcyListItem entity.
     */
    public abstract CcyListItemDAO getDAO();
}
