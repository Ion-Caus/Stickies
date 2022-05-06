package com.ionc.stickies.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ionc.stickies.data.CardRepository;
import com.ionc.stickies.model.SynonymsCard;

import java.util.Collections;
import java.util.List;


public class PlayViewModel extends AndroidViewModel {

    private final CardRepository repository;

    private LiveData<List<SynonymsCard>> liveCards;
    private List<SynonymsCard> cards;

    private int nextCardDisplayedPos;

    public PlayViewModel(Application app) {
        super(app);
        repository = CardRepository.getInstance(app);
    }

    public void loadData() {
        liveCards = repository.getSynonymCards();
        cards = liveCards.getValue();

        if (cards == null) return;
        Collections.sort(cards, (o1, o2) -> {
            if(o1.getRecallScore() == o2.getRecallScore())
                return 0;
            return o1.getRecallScore() < o2.getRecallScore() ? -1 : 1;
        });

        nextCardDisplayedPos = 0;
    }

    public SynonymsCard getNextDisplayCard() {
        if (nextCardDisplayedPos + 1 > cards.size()) {
            return null;
        }
        SynonymsCard card = cards.get(nextCardDisplayedPos);
        nextCardDisplayedPos++;
        return card;
    }

    public void setGoodRecallScore() {
        updateRecallScore(5);
    }

    public void setOkRecallScore() {
        updateRecallScore(1);
    }

    public void setBadRecallScore() {
        updateRecallScore(-3);
    }

    private void updateRecallScore(int value) {
        SynonymsCard card = cards.get(nextCardDisplayedPos-1);
        card.setRecallScore(
                card.getRecallScore() + value
        );
        repository.update(card);
    }
}