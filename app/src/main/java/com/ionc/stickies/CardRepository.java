package com.ionc.stickies;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ionc.stickies.model.SynonymsCard;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CardRepository {
    private static CardRepository instance;
    private final CardDao cardDao;

    private final LiveData<List<SynonymsCard>> allSynonymCards;
    private final ExecutorService executorService;

    private CardRepository(Application application) {
        CardDatabase database = CardDatabase.getInstance(application);
        cardDao = database.cardDao();

        allSynonymCards = cardDao.getAllSynonymsCards();
        executorService = Executors.newFixedThreadPool(2);
    }


    public static synchronized CardRepository getInstance(Application application) {
        if (instance == null)
            instance = new CardRepository(application);

        return instance;
    }

    public LiveData<List<SynonymsCard>> getAllSynonymCards() {
        return allSynonymCards;
    }

    public void insert(SynonymsCard synonymsCard) {
        executorService.execute(() -> cardDao.insert(synonymsCard));
    }
}
