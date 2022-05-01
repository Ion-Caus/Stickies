package com.ionc.stickies.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ionc.stickies.R;
import com.ionc.stickies.model.Deck;
import com.ionc.stickies.model.SynonymsCard;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_DECK= "com.ionc.stickies.DECK";


    private DeckViewModel deckViewModel;

    private RecyclerView recyclerView;
    private DecksAdapter decksAdapter;

    private FloatingActionButton addBtn;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle_view_decks);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);


        decksAdapter = new DecksAdapter(new ArrayList<>());

        deckViewModel.getAllDecks().observe(this, decks -> {
            decksAdapter.setDecks(decks);
            decksAdapter.notifyDataSetChanged();
        });

        if (deckViewModel.getAllDecks().getValue() == null || deckViewModel.getAllDecks().getValue().isEmpty()) {
            System.out.println(deckViewModel.getAllDecks().getValue());
            insertDummy();
        }

        decksAdapter.setOnClickListener(deck -> {
            Toast.makeText(this, "Opening deck " + deck.getName(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, CardsActivity.class);
            intent.putExtra(EXTRA_DECK, deck.getId());
            startActivity(intent);

        });
        recyclerView.setAdapter(decksAdapter);

        addBtn = findViewById(R.id.floating_add__deck_button);
        addBtn.setOnClickListener(view -> {
            deckViewModel.insert(  new Deck("New deck", Deck.DeckType.TRANSLATION) );
            decksAdapter.notifyDataSetChanged();
        });

    }

    private void insertDummy() {
        Deck[] decks = new Deck[] {
                new Deck("Toelf", Deck.DeckType.SYNONYMS),
                new Deck("Cambridge", Deck.DeckType.SYNONYMS)
        };

        for (Deck deck: decks) {
            deckViewModel.insert(deck);
        }
    }
}