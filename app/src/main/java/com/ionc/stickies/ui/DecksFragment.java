package com.ionc.stickies.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ionc.stickies.R;
import com.ionc.stickies.model.Deck;

import java.util.ArrayList;


public class DecksFragment extends Fragment {

    public static final String DECK_TYPE = "DeckType";

    private DeckViewModel deckViewModel;

    private NavController navController;

    private RecyclerView recyclerView;
    private DecksAdapter decksAdapter;

    private FloatingActionButton addBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_decks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initNavController(view);

        setupAdapter();
        initViewModel();

        setupObservers();
        setupListeners();

        setUpItemTouchHelper();
    }

    private void initViews(@NonNull View view) {
        recyclerView = view.findViewById(R.id.recycle_view_decks);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        addBtn = view.findViewById(R.id.floating_add__deck_button);
    }

    private void initViewModel() {
        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
    }

    private void setupAdapter() {
        decksAdapter = new DecksAdapter(new ArrayList<>());
        recyclerView.setAdapter(decksAdapter);
    }

    private void initNavController(@NonNull View view) {
        navController = Navigation.findNavController(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupObservers() {
        deckViewModel.getAllDecksByUser().observe(getViewLifecycleOwner(), decks -> {
            decksAdapter.setDecks(decks);
            decksAdapter.notifyDataSetChanged();
        });
    }

    private void setupListeners() {
        decksAdapter.setOnClickListener(deck -> {
            Bundle args = new Bundle();
            args.putInt(CardsFragment.DECK_ID, deck.getId());
            args.putString(DECK_TYPE, deck.getDeckType().name());
            navController.navigate(R.id.action_fragment_decks_to_fragment_cards, args);

        });

        addBtn.setOnClickListener(v -> navController.navigate(R.id.action_fragment_decks_to_fragment_add_deck));
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback swipeCallback =  new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    int position = viewHolder.getAbsoluteAdapterPosition();
                    Deck deck = decksAdapter.getDecks().get(position);
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            deleteDeck(position, deck);
                            Toast.makeText(getActivity(), "Deck deleted", Toast.LENGTH_SHORT).show();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            undoSwipe(position, deck);
                            Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
                            break;
                    }
                };

                new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void undoSwipe(int position, Deck deck) {
        decksAdapter.getDecks().add(deck);
        decksAdapter.notifyItemInserted(position);
    }

    private void deleteDeck(int position, Deck deck) {
        deckViewModel.delete(deck);
        decksAdapter.notifyItemRemoved(position);
    }
}
