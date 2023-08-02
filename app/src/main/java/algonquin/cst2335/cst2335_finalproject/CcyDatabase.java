package algonquin.cst2335.cst2335_finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database
        (entities = {CcyListItem.class},version = 1)
public abstract class CcyDatabase extends RoomDatabase {
    public abstract CcyListItemDAO getDAO();
}
