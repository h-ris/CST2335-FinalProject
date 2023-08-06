package algonquin.cst2335.cst2335_finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FlightModelDAO {
    @Insert
    void insertFlight(FlightModel f);

    @Query("SELECT * FROM FlightModel")
    List<FlightModel> getAllFlights();

    @Delete
    void deleteFlight(FlightModel f);
}
