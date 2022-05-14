package com.ionc.stickies.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ionc.stickies.model.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeckRepository {
    private static DeckRepository instance;
    private final DeckDao deckDao;

    private LiveData<List<Deck>> allDecksByUser;

    private final ExecutorService executorService;

    private DeckRepository(Application application) {
        StickiesDatabase database = StickiesDatabase.getInstance(application);
        deckDao = database.deckDao();

        allDecksByUser = new MutableLiveData<>(new ArrayList<>());
        executorService = Executors.newFixedThreadPool(2);
    }

    public void initData(String userId) {
        allDecksByUser = deckDao.getAllDecksByUser(userId);
    }

    public static synchronized DeckRepository getInstance(Application application) {
        if (instance == null)
            instance = new DeckRepository(application);

        return instance;
    }

    public LiveData<List<Deck>> getAllDecksByUser() {
        return allDecksByUser;
    }

    public void insert(Deck deck) {
        executorService.execute(() -> deckDao.insert(deck));
    }

    public void delete(Deck deck) {
        executorService.execute(() -> deckDao.delete(deck));
    }
}
