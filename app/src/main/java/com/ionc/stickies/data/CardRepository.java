package com.ionc.stickies.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ionc.stickies.model.SynonymsCard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CardRepository {
    private static CardRepository instance;
    private final CardDao cardDao;

    private LiveData<List<SynonymsCard>> allSynonymCards;
    private LiveData<List<SynonymsCard>> synonymCardsByDeck;

    private final ExecutorService executorService;

    private CardRepository(Application application) {
        StickiesDatabase database = StickiesDatabase.getInstance(application);
        cardDao = database.cardDao();
        executorService = Executors.newFixedThreadPool(2);

        allSynonymCards = new MutableLiveData<>(new ArrayList<>());
        synonymCardsByDeck = new MutableLiveData<>(new ArrayList<>());
    }

    public void init(int deckId) {
        allSynonymCards = cardDao.getAllSynonymsCards();
        synonymCardsByDeck = cardDao.getSynonymsCardsByDeckId(deckId);
    }


    public static synchronized CardRepository getInstance(Application application) {
        if (instance == null)
            instance = new CardRepository(application);

        return instance;
    }

    public LiveData<List<SynonymsCard>> getAllSynonymCards() {
        return allSynonymCards;
    }


    public LiveData<List<SynonymsCard>> getSynonymCardsByDeckId() {
        return synonymCardsByDeck;
    }

    public void insert(SynonymsCard synonymsCard) {
        executorService.execute(() -> cardDao.insert(synonymsCard));
    }
}
