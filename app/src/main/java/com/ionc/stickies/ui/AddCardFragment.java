package com.ionc.stickies.ui;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.ionc.stickies.model.Card;
import com.ionc.stickies.model.Deck;
import com.ionc.stickies.model.PartOfSpeech;

import java.util.ArrayList;
import java.util.List;

public class AddCardFragment extends Fragment {

    private TextInputLayout inputWord;
    private TextInputLayout inputExplanation;

    private TextInputLayout selectPartOfSpeech;

    private TextView explanationsView;
    private Button addExplanationBtn;
    private Button createCardBtn;

    private NavController navController;

    private CardViewModel cardViewModel;
    private List<String> explanations;

    private String errorMessageExplanation;

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
        updateInputTitle();
        setListeners();

        explanations = new ArrayList<>();
    }

    private void initViews(@NonNull View view) {
        inputWord = view.findViewById(R.id.tf_card_word);
        inputExplanation = view.findViewById(R.id.tf_card_explanation);

        selectPartOfSpeech = view.findViewById(R.id.tl_card_partOfSpeech);
        setUpDropdown();

        explanationsView = view.findViewById(R.id.tv_explanations_list);
        explanationsView.setText("");
        explanationsView.setMovementMethod(new ScrollingMovementMethod());

        addExplanationBtn = view.findViewById(R.id.add_explanation_button);
        createCardBtn = view.findViewById(R.id.create_card_button);
    }

    private void setUpDropdown() {
        AutoCompleteTextView dropdownView = ((AutoCompleteTextView)selectPartOfSpeech.getEditText());
        if (dropdownView != null) {
            String[] types = new String[] {
                    PartOfSpeech.Phrase.name(),
                    PartOfSpeech.Noun.name(),
                    PartOfSpeech.Verb.name(),
                    PartOfSpeech.Adjective.name(),
                    PartOfSpeech.Adverb.name()
            };

            dropdownView.setAdapter(
                    new ArrayAdapter<>(getActivity(), R.layout.list_item_dropdown, types)
            );
        }
    }

    private void initViewModel() {
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
    }

    private void initNavController(@NonNull View view) {
        navController = Navigation.findNavController(view);
    }

    private void setListeners() {
        addExplanationBtn.setOnClickListener(view -> {
            if (inputExplanation.getEditText() == null
                    || inputExplanation.getEditText().getText() == null
                    || inputExplanation.getEditText().getText().toString().trim().isEmpty()) {
                Toast.makeText(getActivity(), errorMessageExplanation, Toast.LENGTH_SHORT).show();
                return;
            }
            String explanation = inputExplanation.getEditText().getText().toString().trim();
            inputExplanation.getEditText().setText("");
            explanations.add(explanation);
            updateExplanationsView();
        });

        createCardBtn.setOnClickListener(view -> addCard());
    }

    private void updateExplanationsView() {
        if (explanations.isEmpty()) {
            explanationsView.setText("");
            return;
        }

        explanationsView.setText("");
        for (String explanation: explanations) {
            explanationsView.append("• " + explanation + "\n");
        }
    }

    private Card createCard(int deckId, List<String> explanation) {
        if (inputWord.getEditText() == null
                || inputWord.getEditText().getText() == null
                || inputWord.getEditText().getText().toString().trim().isEmpty()) {

            throw new IllegalArgumentException("Please enter the word.");
        }
        String word = inputWord.getEditText().getText().toString().trim();

        if (selectPartOfSpeech.getEditText() == null
                || selectPartOfSpeech.getEditText().getText() == null
                || selectPartOfSpeech.getEditText().getText().toString().trim().isEmpty()) {

            throw new IllegalArgumentException("Please select the part of speech for the word.");
        }
        String pos = selectPartOfSpeech.getEditText().getText().toString().trim();
        PartOfSpeech partOfSpeech = PartOfSpeech.convertToPOS(pos);

        if (explanation.isEmpty()) {
            throw new IllegalArgumentException(errorMessageExplanation);
        }
        return new Card(word, false, 0, partOfSpeech, explanation.toArray(new String[0]), deckId);
    }

    private void addCard() {
        try {
            int deckId = cardViewModel.getDeckId();
            Card card = createCard(deckId, explanations);
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

    private void updateInputTitle() {
        Deck.DeckType deckType = Deck.DeckType.convertToType(cardViewModel.getDeckType());
        String hint;
        switch (deckType) {
            case SYNONYMS:
                hint = getResources().getString(R.string.insert_synonym);
                errorMessageExplanation = "Please enter a synonym.";
                break;
            case TRANSLATION:
                hint = getResources().getString(R.string.insert_translation);
                errorMessageExplanation = "Please enter a translation.";
                break;
            default:
                return;
        }
        inputExplanation.setHint(hint);

    }
}
