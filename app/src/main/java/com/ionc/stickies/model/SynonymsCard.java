package com.ionc.stickies.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "synonyms_card")
public class SynonymsCard {

    @PrimaryKey
    @NonNull
    private String word;
    private boolean isFavourite;
    private int recallScore;

    private String[] synonyms;

    public SynonymsCard(@NonNull String word, boolean isFavourite, int recallScore, String[] synonyms) {
        this.word = word;
        this.isFavourite = isFavourite;
        this.recallScore = recallScore;
        this.synonyms = synonyms;
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
}
