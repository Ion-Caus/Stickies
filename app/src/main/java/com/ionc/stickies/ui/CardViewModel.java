package com.ionc.stickies.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ionc.stickies.data.CardRepository;
import com.ionc.stickies.model.SynonymsCard;

import java.util.List;

public class CardViewModel extends AndroidViewModel {

    private final CardRepository repository;

    public CardViewModel(Application app) {
        super(app);
        repository = CardRepository.getInstance(app);
    }

    public void init(int deckId) {
        repository.init(deckId);
    }

    public LiveData<List<SynonymsCard>> getAllSynonymCards() {
        return repository.getAllSynonymCards();
    }

    public LiveData<List<SynonymsCard>> getSynonymCardsByDeck() {
        return repository.getSynonymCardsByDeckId();
    }

    public void insert(final SynonymsCard card) {
        repository.insert(card);
    }
}
