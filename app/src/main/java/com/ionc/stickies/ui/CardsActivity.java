package com.ionc.stickies.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ionc.stickies.R;
import com.ionc.stickies.model.SynonymsCard;

import java.util.ArrayList;
import java.util.Arrays;

public class CardsActivity extends AppCompatActivity {
    private CardViewModel cardViewModel;

    private RecyclerView recyclerView;
    private SynonymsCardsAdapter cardsAdapter;

    private FloatingActionButton addBtn;

    private int deckId;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        recyclerView = findViewById(R.id.recycle_view_cards);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);

        cardsAdapter = new SynonymsCardsAdapter(new ArrayList<>());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(MainActivity.EXTRA_DECK)) {
            deckId = bundle.getInt(MainActivity.EXTRA_DECK);

            cardViewModel.init(deckId);
        }

        cardViewModel.getSynonymCardsByDeck().observe(this, cards -> {
            cardsAdapter.setSynonymsCards(cards);
            cardsAdapter.notifyDataSetChanged();
        });



        cardsAdapter.setOnClickListener(card -> {
            Toast.makeText(this, Arrays.toString(card.getSynonyms()), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(cardsAdapter);

        addBtn = findViewById(R.id.floating_add__card_button);
        addBtn.setOnClickListener(view -> {
            cardViewModel.insert(  new SynonymsCard("Stable",true, 0, new String[] {"constant", "even", "steady"}, deckId) );
            cardsAdapter.notifyDataSetChanged();
        });

        if (cardViewModel.getAllSynonymCards().getValue() == null || cardViewModel.getAllSynonymCards().getValue().isEmpty()) {
            System.out.println(cardViewModel.getAllSynonymCards().getValue());
            insertDummy();
        }
    }

    private void insertDummy() {
        SynonymsCard[] cards = new SynonymsCard[]{
                new SynonymsCard("Only",true, 0, new String[] {"Merely", "Solely"}, 1),
                new SynonymsCard("Unique",true, 0, new String[] {"special", "matchless", "distinctive", "peculiar"}, 1),
                new SynonymsCard("Assign",true, 0, new String[] {"give", "allocate"}, 1),
                new SynonymsCard("Hypothesize",true, 0, new String[] {"predict", "theorize", "put forward"}, 2),
                new SynonymsCard("Mutual",true, 0, new String[] {"shared", "reciprocal", "joint"}, 2),
        };

        for (SynonymsCard card: cards) {
            cardViewModel.insert(card);
            System.out.println("inserted " + card.getWord());
        }
        cardsAdapter.notifyDataSetChanged();


    }
}
