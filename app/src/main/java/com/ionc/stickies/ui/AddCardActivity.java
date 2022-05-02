package com.ionc.stickies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.ionc.stickies.R;
import com.ionc.stickies.model.SynonymsCard;

import java.util.ArrayList;
import java.util.List;

public class AddCardActivity extends AppCompatActivity {
    public static final String CREATE_CARD_STATUS = "com.ionc.stickies.createCardStatus";

    private TextInputLayout inputWord;
    private TextInputLayout inputSynonym;

    private TextView synonymsView;
    private Button addSynonymBtn;
    private Button createCardBtn;

    private CardViewModel cardViewModel;
    private List<String> synonyms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        initViews();
        initViewModel();
        setListeners();

        synonyms = new ArrayList<>();
    }

    private void initViews() {
        inputWord = findViewById(R.id.tf_card_word);
        inputSynonym = findViewById(R.id.tf_card_synonym);

        synonymsView = findViewById(R.id.tv_synonyms_list);
        synonymsView.setText("");
        synonymsView.setMovementMethod(new ScrollingMovementMethod());

        addSynonymBtn = findViewById(R.id.add_synonym_button);
        createCardBtn = findViewById(R.id.create_card_button);
    }

    private void initViewModel() {
        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
    }

    private void setListeners() {
        addSynonymBtn.setOnClickListener(view -> {
            if (inputSynonym.getEditText() == null
                    || inputSynonym.getEditText().getText() == null
                    || inputSynonym.getEditText().getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter a synonym.", Toast.LENGTH_SHORT).show();
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
            synonymsView.append(synonym + "\n");
        }
    }

    private int getDeck() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(CardsActivity.SELECTED_DECK)) {
            return bundle.getInt(CardsActivity.SELECTED_DECK);
        }
        throw new IllegalArgumentException("The deck ID was not passed.");
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
            System.out.println("card was created");
            cardViewModel.insert(card);
            System.out.println("Adding the card");
            submit(true);
        }
        catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.fillInStackTrace();
            submit(false);
        }

    }

    public void submit(boolean status) {
        Intent intent = new Intent();
        intent.putExtra(CREATE_CARD_STATUS, status);
        setResult(RESULT_OK, intent);
        finish();
    }
}
