package com.ionc.stickies;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ionc.stickies.model.SynonymsCard;

@Database(entities = {SynonymsCard.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class CardDatabase extends RoomDatabase {

    private static CardDatabase instance;

    public abstract CardDao cardDao();

    public static synchronized CardDatabase getInstance(Context context){
        if(instance == null) {
            instance = Room.databaseBuilder(context,
                    CardDatabase.class, "card_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
