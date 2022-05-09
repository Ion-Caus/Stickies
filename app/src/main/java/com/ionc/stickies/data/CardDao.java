package com.ionc.stickies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ionc.stickies.model.Card;

import java.util.List;

@Dao
public interface CardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Card card);

    @Update
    void update(Card card);

    @Delete
    void delete(Card card);

    @Query("SELECT * FROM card WHERE deck_id == :deckId")
    LiveData<List<Card>> getCardsByDeckId(int deckId);

    @Query("SELECT * FROM card WHERE deck_id == :deckId ORDER BY recallScore")
    List<Card> getCardsByDeckIdOrdered(int deckId);
}
