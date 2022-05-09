package com.ionc.stickies.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ionc.stickies.model.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CardRepository {
    private static CardRepository instance;
    private final CardDao cardDao;

    private LiveData<List<Card>> cardsByDeck;

    private final ExecutorService executorService;

    private CardRepository(Application application) {
        StickiesDatabase database = StickiesDatabase.getInstance(application);
        cardDao = database.cardDao();
        executorService = Executors.newFixedThreadPool(2);

        cardsByDeck = new MutableLiveData<>(new ArrayList<>());
    }

    public void initData(int deckId) {
        cardsByDeck = cardDao.getCardsByDeckId(deckId);
    }


    public static synchronized CardRepository getInstance(Application application) {
        if (instance == null)
            instance = new CardRepository(application);

        return instance;
    }


    public LiveData<List<Card>> getCards() {
        return cardsByDeck;
    }

    public void insert(Card card) {
        executorService.execute(() -> cardDao.insert(card));
    }

    public void update(Card card) {
        executorService.execute(() -> cardDao.update(card));
    }

    public void delete(Card card) {
        executorService.execute(() -> cardDao.delete(card));
    }
}
