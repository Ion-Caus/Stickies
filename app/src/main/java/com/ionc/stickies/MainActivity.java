package com.ionc.stickies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.ionc.stickies.model.SynonymsCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private CardViewModel cardViewModel;

    private RecyclerView recyclerView;
    private SynonymsCardsAdapter cardsAdapter;

    private Button insertBtn;

    private int deckSize;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);


        cardsAdapter = new SynonymsCardsAdapter(new ArrayList<>());
        deckSize = 0;

        cardViewModel.getAllSynonymCards().observe(this, cards -> {
            cardsAdapter.setSynonymsCards(cards);
            cardsAdapter.notifyDataSetChanged();
        });

        cardsAdapter.setOnClickListener(card -> {
            Toast.makeText(this, Arrays.toString(card.getSynonyms()), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(cardsAdapter);

        insertBtn = findViewById(R.id.insert_synonyms_card_btn);
        insertBtn.setOnClickListener(view -> {
            cardViewModel.insert(  new SynonymsCard("Stable",true, 0, new String[] {"constant", "even", "steady"}) );
            cardsAdapter.notifyItemInserted(deckSize++);
        });

        if (cardViewModel.getAllSynonymCards() == null || cardViewModel.getAllSynonymCards().getValue() == null || cardViewModel.getAllSynonymCards().getValue().isEmpty()) {
            System.out.println(cardViewModel.getAllSynonymCards());
            System.out.println(cardViewModel.getAllSynonymCards().getValue());
            insertDummy();
        }
    }

    private void insertDummy() {
        SynonymsCard[] cards = new SynonymsCard[]{
                new SynonymsCard("Only",true, 0, new String[] {"Merely", "Solely"}),
                new SynonymsCard("Unique",true, 0, new String[] {"special", "matchless", "distinctive", "peculiar"}),
                new SynonymsCard("Assign",true, 0, new String[] {"give", "allocate"}),
                new SynonymsCard("Hypothesize",true, 0, new String[] {"predict", "theorize", "put forward"}),
                new SynonymsCard("Mutual",true, 0, new String[] {"shared", "reciprocal", "joint"}),
        };

        for (SynonymsCard card: cards) {
            cardViewModel.insert(card);
            cardsAdapter.notifyItemInserted(deckSize++);
            System.out.println("inserted " + card.getWord());
        }
    }
}