package com.ionc.stickies;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ionc.stickies.model.SynonymsCard;

import java.util.List;
import java.util.Objects;

public class CardViewModel extends AndroidViewModel {

    private final CardRepository repository;

    public CardViewModel(Application app) {
        super(app);
        repository = CardRepository.getInstance(app);
    }

    public LiveData<List<SynonymsCard>> getAllSynonymCards() {
        return repository.getAllSynonymCards();
    }

    public void insert(final SynonymsCard card) {
        repository.insert(card);
    }
}
