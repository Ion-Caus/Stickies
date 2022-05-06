package com.ionc.stickies.data;

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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SynonymsCard synonymsCard);

    @Update
    void update(SynonymsCard synonymsCard);

    @Delete
    void delete(SynonymsCard synonymsCard);

    @Query("SELECT * FROM synonyms_card ORDER BY recallScore")
    LiveData<List<SynonymsCard>> getAllSynonymsCards();

    @Query("SELECT * FROM synonyms_card WHERE deck_id == :deckId")
    LiveData<List<SynonymsCard>> getSynonymsCardsByDeckId(int deckId);

    @Query("SELECT * FROM synonyms_card WHERE deck_id == :deckId ORDER BY recallScore")
    List<SynonymsCard> getSynonymsCardsByDeckIdOrdered(int deckId);
}
