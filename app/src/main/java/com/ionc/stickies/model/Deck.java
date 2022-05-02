package com.ionc.stickies.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "deck", indices = {@Index("deckId"), @Index(value = "deckName", unique = true)})
public class Deck {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "deckId")
    private int id;
    @ColumnInfo(name = "deckName")
    private String name;
    private DeckType deckType;

    public Deck(String name, DeckType deckType) {
        this.name = name;
        this.deckType = deckType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeckType getDeckType() {
        return deckType;
    }

    public void setDeckType(DeckType deckType) {
        this.deckType = deckType;
    }

    public enum DeckType {
        SYNONYMS,
        TRANSLATION;

        public static DeckType convertToType(String type) {
            switch (type.toUpperCase()) {
                case "SYNONYMS":
                    return SYNONYMS;
                case "TRANSLATION":
                    return TRANSLATION;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + type);
            }
        }
    }
}
