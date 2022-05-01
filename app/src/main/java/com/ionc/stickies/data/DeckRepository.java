package com.ionc.stickies.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ionc.stickies.model.Deck;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeckRepository {
    private static DeckRepository instance;
    private final DeckDao deckDao;

    private final LiveData<List<Deck>> allDecks;

    private final ExecutorService executorService;

    private DeckRepository(Application application) {
        StickiesDatabase database = StickiesDatabase.getInstance(application);
        deckDao = database.deckDao();

        allDecks = deckDao.getAllDecks();
        executorService = Executors.newFixedThreadPool(2);
    }


    public static synchronized DeckRepository getInstance(Application application) {
        if (instance == null)
            instance = new DeckRepository(application);

        return instance;
    }

    public LiveData<List<Deck>> getAllDecks() {
        return allDecks;
    }

    public void insert(Deck deck) {
        executorService.execute(() -> deckDao.insert(deck));
    }
}
