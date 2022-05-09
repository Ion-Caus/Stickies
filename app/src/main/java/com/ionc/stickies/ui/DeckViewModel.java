package com.ionc.stickies.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ionc.stickies.data.DeckRepository;
import com.ionc.stickies.model.Deck;

import java.util.List;

public class DeckViewModel extends AndroidViewModel {

    private final DeckRepository repository;

    public DeckViewModel(Application app) {
        super(app);
        repository = DeckRepository.getInstance(app);
    }

    public LiveData<List<Deck>> getAllDecks() {
        return repository.getAllDecks();
    }

    public void insert(final Deck deck) {
        repository.insert(deck);
    }

    public void delete(final Deck deck) {
        repository.delete(deck);
    }
}
