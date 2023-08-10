package algonquin.cst2335.cst2335_finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FlightModel.class}, version=1)
public abstract class FlightDatabase extends RoomDatabase {
    public abstract FlightModelDAO fmDAO();

}
