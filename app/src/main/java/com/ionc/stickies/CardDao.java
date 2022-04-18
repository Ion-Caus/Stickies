package com.ionc.stickies;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ionc.stickies.model.SynonymsCard;

import java.util.List;

@Dao
public interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SynonymsCard synonymsCard);

    @Update
    void update(SynonymsCard synonymsCard);

    @Delete
    void delete(SynonymsCard synonymsCard);

    @Query("SELECT * FROM synonyms_card ORDER BY recallScore")
    LiveData<List<SynonymsCard>> getAllSynonymsCards();
}
