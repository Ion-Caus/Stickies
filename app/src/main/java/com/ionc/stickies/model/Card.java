package com.ionc.stickies.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "card",
        primaryKeys = {"word", "deck_id", "partOfSpeech"},
        foreignKeys = @ForeignKey(entity = Deck.class, parentColumns = "deckId", childColumns = "deck_id", onDelete = CASCADE),
        indices = {@Index("deck_id")}
)
public class Card {

    @NonNull
    private String word;
    private boolean isFavourite;
    private int recallScore;

    @NonNull
    private PartOfSpeech partOfSpeech;

    private String[] explanations;

    @NonNull
    @ColumnInfo(name = "deck_id")
    private Integer deckId;

    public Card(@NonNull String word, boolean isFavourite, int recallScore,@NonNull PartOfSpeech partOfSpeech, String[] explanations, @NonNull Integer deckId) {
        this.word = word;
        this.isFavourite = isFavourite;
        this.recallScore = recallScore;
        this.partOfSpeech = partOfSpeech;
        this.explanations = explanations;
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

    public String[] getExplanations() {
        return explanations;
    }

    public void setExplanations(String[] explanations) {
        this.explanations = explanations;
    }

    @NonNull
    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(@NonNull PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    @NonNull
    public Integer getDeckId() {
        return deckId;
    }

    public void setDeckId(@NonNull Integer deckId) {
        this.deckId = deckId;
    }
}
