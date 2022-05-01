package com.ionc.stickies.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "synonyms_card",
        primaryKeys = {"word", "deck_id"},
        foreignKeys = @ForeignKey(entity = Deck.class, parentColumns = "deckId", childColumns = "deck_id", onDelete = CASCADE),
        indices = {@Index("deck_id")}
)
public class SynonymsCard {

    @NonNull
    private String word;
    private boolean isFavourite;
    private int recallScore;

    private String[] synonyms;

    @NonNull
    @ColumnInfo(name = "deck_id")
    private Integer deckId;

    public SynonymsCard(@NonNull String word, boolean isFavourite, int recallScore, String[] synonyms,@NonNull Integer deckId) {
        this.word = word;
        this.isFavourite = isFavourite;
        this.recallScore = recallScore;
        this.synonyms = synonyms;
        this.deckId = deckId;
    }

    @NonNull
    public String getWord() {
        return word;
    }

    public void setWord(@NonNull String word) {
        this.word = word;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getRecallScore() {
        return recallScore;
    }

    public void setRecallScore(int recallScore) {
        this.recallScore = recallScore;
    }

    public String[] getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String[] synonyms) {
        this.synonyms = synonyms;
    }

    @NonNull
    public Integer getDeckId() {
        return deckId;
    }

    public void setDeckId(@NonNull Integer deckId) {
        this.deckId = deckId;
    }
}
