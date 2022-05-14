package com.ionc.stickies.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ionc.stickies.data.DeckRepository;
import com.ionc.stickies.data.UserRepository;
import com.ionc.stickies.model.Deck;

import java.util.List;

public class DeckViewModel extends AndroidViewModel {

    private final DeckRepository deckRepository;
    private final UserRepository userRepository;

    public DeckViewModel(Application app) {
        super(app);
        deckRepository = DeckRepository.getInstance(app);
        userRepository = UserRepository.getInstance(app);

        deckRepository.initData(
                userRepository.getCurrentUserId()
        );
    }

    public LiveData<List<Deck>> getAllDecksByUser() {
        return deckRepository.getAllDecksByUser();
    }

    public void insert(final Deck deck) {
        setUser(deck);
        deckRepository.insert(deck);
    }

    public void delete(final Deck deck) {
        setUser(deck);
        deckRepository.delete(deck);
    }

    private void setUser(final Deck deck) {
        deck.setUserId(
                userRepository.getCurrentUserId()
        );
    }
}
