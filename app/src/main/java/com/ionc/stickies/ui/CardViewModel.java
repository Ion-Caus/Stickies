package com.ionc.stickies.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ionc.stickies.data.CardRepository;
import com.ionc.stickies.model.Card;

import java.util.List;

public class CardViewModel extends AndroidViewModel {

    private int deckId;
    private String deckType;

    private final CardRepository repository;

    public CardViewModel(Application app) {
        super(app);
        repository = CardRepository.getInstance(app);
    }

    public void initData(int deckId) {
        repository.initData(deckId);
    }

    public LiveData<List<Card>> getCardsByDeck() {
        return repository.getCards();
    }

    public void insert(final Card card) {
        repository.insert(card);
    }

    public void delete(final Card card) {
        repository.delete(card);
    }


    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }

    public String getDeckType() {
        return deckType;
    }

    public void setDeckType(String deckType) {
        this.deckType = deckType;
    }

}
