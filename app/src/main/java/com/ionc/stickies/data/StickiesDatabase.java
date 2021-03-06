package com.ionc.stickies.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ionc.stickies.model.Card;
import com.ionc.stickies.model.Deck;

@Database(entities = {Card.class, Deck.class}, version = 10, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class StickiesDatabase extends RoomDatabase {

    private static StickiesDatabase instance;

    public abstract CardDao cardDao();
    public abstract DeckDao deckDao();

    public static synchronized StickiesDatabase getInstance(Context context){
        if(instance == null) {
            instance = Room.databaseBuilder(context,
                    StickiesDatabase.class, "stickies_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
