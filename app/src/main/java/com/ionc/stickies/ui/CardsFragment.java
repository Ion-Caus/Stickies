package com.ionc.stickies.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ionc.stickies.R;

import java.util.ArrayList;
import java.util.Arrays;

public class CardsFragment extends Fragment {

    public static final String DECK_ID = "DeckId";

    private CardViewModel cardViewModel;

    private RecyclerView recyclerView;
    private SynonymsCardsAdapter cardsAdapter;

    private NavController navController;

    private FloatingActionButton addBtn;
    private Button playBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initNavController(view);

        setupAdapter();

        initViewModel();

        int deckId = getDeckArg();
        initDataViewModel(deckId);
        setupButtonsPress(deckId);

        setupObservers();
        setupCardPress();

    }

    private int getDeckArg() {
        if (getArguments() == null || getArguments().isEmpty()) {
            Toast.makeText(getActivity(), "Can not load the cards.", Toast.LENGTH_SHORT).show();
            return -1;
        }
        return getArguments().getInt(DECK_ID);
    }

    private void initViews(@NonNull View view) {
        recyclerView = view.findViewById(R.id.recycle_view_cards);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        addBtn = view.findViewById(R.id.floating_add__card_button);
        playBtn = view.findViewById(R.id.play_card_btn);
    }

    private void initViewModel() {
        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
    }

    private void initNavController(@NonNull View view) {
        navController = Navigation.findNavController(view);
    }

    private void setupAdapter() {
        cardsAdapter = new SynonymsCardsAdapter(new ArrayList<>());
        recyclerView.setAdapter(cardsAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupObservers() {
        cardViewModel.getSynonymCardsByDeck().observe(getViewLifecycleOwner(), cards -> {
            cardsAdapter.setSynonymsCards(cards);
            cardsAdapter.notifyDataSetChanged();
        });

    }

    private void initDataViewModel(int deckId) {
        cardViewModel.initData(deckId);
    }

    private void setupButtonsPress(int deckId) {
        addBtn.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt(DECK_ID, deckId);
            navController.navigate(R.id.action_fragment_cards_to_fragment_add_card, args);
        });

        playBtn.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt(DECK_ID, deckId);
            navController.navigate(R.id.action_fragment_cards_to_fragment_play, args);
        });
    }

    private void setupCardPress() {
        cardsAdapter.setOnClickListener(card -> {
            Toast.makeText(getActivity(), Arrays.toString(card.getSynonyms()), Toast.LENGTH_SHORT).show();
        });
    }
}