package com.ionc.stickies.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.ionc.stickies.R;
import com.ionc.stickies.model.Deck;


public class AddDeckFragment extends Fragment {

    private TextInputLayout inputName;
    private TextInputLayout selectType;

    private Button createDeckBtn;

    private DeckViewModel deckViewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_deck, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initNavController(view);
        initViewModel();
        setListeners();
    }

    private void initViews(@NonNull View view) {
        inputName = view.findViewById(R.id.tf_deck_name);

        selectType = view.findViewById(R.id.tl_deck_type);
        setUpDropdown();

        createDeckBtn = view.findViewById(R.id.create_deck_button);
    }

    private void initNavController(@NonNull View view) {
        navController = Navigation.findNavController(view);
    }

    private void setUpDropdown() {
        AutoCompleteTextView dropdownView = ((AutoCompleteTextView)selectType.getEditText());
        if (dropdownView != null) {
            String[] types = new String[] {
                    Deck.DeckType.SYNONYMS.name(),
                    Deck.DeckType.TRANSLATION.name()
            };

            dropdownView.setAdapter(
                    new ArrayAdapter<>(getActivity(), R.layout.list_item_dropdown, types)
            );
        }
    }

    private void initViewModel() {
        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
    }

    private void setListeners() {
        createDeckBtn.setOnClickListener(view -> {
            addDeck();
        });
    }

    private Deck createDeck() {
        if (inputName.getEditText() == null
                || inputName.getEditText().getText() == null
                || inputName.getEditText().getText().toString().trim().isEmpty()) {

            throw new IllegalArgumentException("Please enter the name");
        }
        String name = inputName.getEditText().getText().toString().trim();

        if (selectType.getEditText() == null
                || selectType.getEditText().getText() == null
                || selectType.getEditText().getText().toString().trim().isEmpty()) {

            throw new IllegalArgumentException("Please select the type");
        }
        String type = selectType.getEditText().getText().toString().trim();
        Deck.DeckType deckType = Deck.DeckType.convertToType(type);
        return new Deck(name, deckType);
    }

    private void addDeck() {
        try {
            Deck deck = createDeck();
            deckViewModel.insert(deck);
            submit(true);
        }
        catch (IllegalArgumentException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.fillInStackTrace();
            submit(false);
        }

    }

    public void submit(boolean status) {
        String message = status
                ? "Deck was added."
                : "Failed to add the deck";

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        navController.popBackStack();
    }
}

