package com.ionc.stickies.ui;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.ionc.stickies.R;
import com.ionc.stickies.model.SynonymsCard;

import java.util.ArrayList;
import java.util.List;

public class AddCardFragment extends Fragment {

    private TextInputLayout inputWord;
    private TextInputLayout inputSynonym;

    private TextView synonymsView;
    private Button addSynonymBtn;
    private Button createCardBtn;

    private NavController navController;

    private CardViewModel cardViewModel;
    private List<String> synonyms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initNavController(view);
        initViewModel();
        setListeners();

        synonyms = new ArrayList<>();
    }

    private void initViews(@NonNull View view) {
        inputWord = view.findViewById(R.id.tf_card_word);
        inputSynonym = view.findViewById(R.id.tf_card_synonym);

        synonymsView = view.findViewById(R.id.tv_synonyms_list);
        synonymsView.setText("");
        synonymsView.setMovementMethod(new ScrollingMovementMethod());

        addSynonymBtn = view.findViewById(R.id.add_synonym_button);
        createCardBtn = view.findViewById(R.id.create_card_button);
    }

    private void initViewModel() {
        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
    }

    private void initNavController(@NonNull View view) {
        navController = Navigation.findNavController(view);
    }

    private void setListeners() {
        addSynonymBtn.setOnClickListener(view -> {
            if (inputSynonym.getEditText() == null
                    || inputSynonym.getEditText().getText() == null
                    || inputSynonym.getEditText().getText().toString().trim().isEmpty()) {
                Toast.makeText(getActivity(), "Please enter a synonym.", Toast.LENGTH_SHORT).show();
                return;
            }
            String synonym = inputSynonym.getEditText().getText().toString().trim();
            inputSynonym.getEditText().setText("");
            synonyms.add(synonym);
            updateSynonymsView();
        });

        createCardBtn.setOnClickListener(view -> {
            addCard();
        });
    }

    private void updateSynonymsView() {
        if (synonyms.isEmpty()) {
            synonymsView.setText("");
            return;
        }

        synonymsView.setText("");
        for (String synonym: synonyms) {
            synonymsView.append("• " + synonym + "\n");
        }
    }

    private int getDeck() {
         if (getArguments() == null || getArguments().isEmpty()) {
             throw new IllegalArgumentException("The deck ID was not passed.");
        }
        return getArguments().getInt("DeckId");

    }

    private SynonymsCard createCard(int deckId, List<String> synonyms) {
        if (inputWord.getEditText() == null
                || inputWord.getEditText().getText() == null
                || inputWord.getEditText().getText().toString().trim().isEmpty()) {

            throw new IllegalArgumentException("Please enter the word");
        }
        String word = inputWord.getEditText().getText().toString().trim();

        return new SynonymsCard(word, false, 0, synonyms.toArray(new String[0]), deckId);
    }

    private void addCard() {
        try {
            int deckId = getDeck();
            SynonymsCard card = createCard(deckId, synonyms);
            cardViewModel.insert(card);
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
                ? "Card was added."
                : "Failed to add the card";

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        navController.popBackStack();
    }
}
