package com.ionc.stickies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ionc.stickies.model.Deck;

import java.util.List;

@Dao
public interface DeckDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Deck deck);

    @Update
    void update(Deck deck);

    @Delete
    void delete(Deck deck);

    @Query("SELECT * FROM deck WHERE userId == :userId")
    LiveData<List<Deck>> getAllDecksByUser(String userId);
}
