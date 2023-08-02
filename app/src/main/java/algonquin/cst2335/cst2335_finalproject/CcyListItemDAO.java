package algonquin.cst2335.cst2335_finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CcyListItemDAO {

    @Insert
    long insertCCYListItem(CcyListItem listItem);

    @Query
            ("Select * From CcyListItem")
    List<CcyListItem> getAllCCYListItem();

    @Delete
    int deleteCCYListItem(CcyListItem listItem);
}
