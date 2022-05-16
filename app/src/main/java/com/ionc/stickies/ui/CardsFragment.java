package com.ionc.stickies.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.ionc.stickies.model.Card;
import com.ionc.stickies.model.Deck;

import java.util.ArrayList;

public class CardsFragment extends Fragment {

    public static final String DECK_ID = "DeckId";

    private CardViewModel cardViewModel;

    private RecyclerView recyclerView;
    private CardsAdapter cardsAdapter;

    private NavController navController;

    private FloatingActionButton addBtn;
    private Button playBtn;

    private RadioButton radioDateBtn;
    private RadioButton radioFavouriteBtn;

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

        initDataViewModel();
        setupButtonsPress();

        updateTitle(view);

        setupObservers();
        setupCardPress();

        setUpItemTouchHelper();

    }

    private void getArg() {
        if (getArguments() == null || getArguments().isEmpty()) {
            Toast.makeText(getActivity(), "Can not load the cards.", Toast.LENGTH_SHORT).show();
            return;
        }
        int deckId = getArguments().getInt(DECK_ID);

        cardViewModel.setDeckId(deckId);
        cardViewModel.setDeckType(getArguments().getString(DecksFragment.DECK_TYPE));
    }

    private void initViews(@NonNull View view) {
        recyclerView = view.findViewById(R.id.recycle_view_cards);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        addBtn = view.findViewById(R.id.floating_add__card_button);
        playBtn = view.findViewById(R.id.play_card_btn);

        radioDateBtn = view.findViewById(R.id.radio_date);
        radioDateBtn.setChecked(true);
        radioFavouriteBtn = view.findViewById(R.id.radio_favourite);
    }

    private void initViewModel() {
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);

        getArg(); // get the arguments
    }

    private void initNavController(@NonNull View view) {
        navController = Navigation.findNavController(view);
    }

    private void setupAdapter() {
        cardsAdapter = new CardsAdapter(new ArrayList<>());
        recyclerView.setAdapter(cardsAdapter);
        recyclerView.setHorizontalScrollBarEnabled(true);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupObservers() {
        cardViewModel.getCardsByDeck().observe(getViewLifecycleOwner(), cards -> {
            cardsAdapter.setCards(cards);
            cardsAdapter.notifyDataSetChanged();
        });

    }

    private void updateTitle(@NonNull View view) {
        if (getArguments() == null || getArguments().isEmpty()) {
           return;
        }
        cardViewModel.setDeckType(getArguments().getString(DecksFragment.DECK_TYPE));

        TextView tv = view.findViewById(R.id.title_cards_fragment);

        Deck.DeckType deckType = Deck.DeckType.convertToType(cardViewModel.getDeckType());
        String title;
        switch (deckType) {
            case SYNONYMS:
                title = getResources().getString(R.string.title_synonyms_cards);
                break;
            case TRANSLATION:
                title = getResources().getString(R.string.titles_translations_cards);
                break;
            default:
                return;
        }
        tv.setText(title);
    }

    private void initDataViewModel() {
        cardViewModel.initData(cardViewModel.getDeckId());
    }

    private void setupButtonsPress() {
        addBtn.setOnClickListener(v -> navController.navigate(R.id.action_fragment_cards_to_fragment_add_card));

        playBtn.setOnClickListener(v -> navController.navigate(R.id.action_fragment_cards_to_fragment_play));

        radioDateBtn.setOnClickListener(this::onRadioButtonClicked);
        radioFavouriteBtn.setOnClickListener(this::onRadioButtonClicked);
    }

    private void setupCardPress() {
        cardsAdapter.setOnClickListener(card -> {
            //Toast.makeText(getActivity(), Arrays.toString(card.getSynonyms()), Toast.LENGTH_SHORT).show();
        });

        cardsAdapter.setFavouriteListener(card -> {
            cardViewModel.updateFavouriteStatus(card);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) return;

        int id = view.getId();
        if (id == R.id.radio_date) {
            // do sorting
            cardsAdapter.notifyDataSetChanged();

        }
        else if (id == R.id.radio_favourite) {
            // do sorting
            cardsAdapter.notifyDataSetChanged();
        }
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback swipeCallback =  new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    int position = viewHolder.getAbsoluteAdapterPosition();
                    Card card = cardsAdapter.getCards().get(position);
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            deleteCard(position, card);
                            Toast.makeText(getActivity(), "Card deleted", Toast.LENGTH_SHORT).show();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            undoSwipe(position);
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

    @SuppressLint("NotifyDataSetChanged")
    private void undoSwipe(int position) {
        cardsAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(position);
    }

    private void deleteCard(int position, Card card) {
        cardViewModel.delete(card);
        cardsAdapter.notifyItemRemoved(position);
    }
}
