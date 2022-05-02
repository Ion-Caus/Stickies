package com.ionc.stickies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.ionc.stickies.R;
import com.ionc.stickies.model.Deck;


public class AddDeckActivity extends AppCompatActivity {
    public static final String CREATE_DECK_STATUS = "com.ionc.stickies.createDeckStatus";

    private TextInputLayout inputName;
    private TextInputLayout selectType;

    private Button createDeckBtn;

    private DeckViewModel deckViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deck);

        initViews();
        initViewModel();
        setListeners();
    }

    private void initViews() {
        inputName = findViewById(R.id.tf_deck_name);

        selectType = findViewById(R.id.tl_deck_type);
        setUpDropdown();

        createDeckBtn = findViewById(R.id.create_deck_button);
    }

    private void setUpDropdown() {
        AutoCompleteTextView dropdownView = ((AutoCompleteTextView)selectType.getEditText());
        if (dropdownView != null) {
            String[] types = new String[] {
                    Deck.DeckType.SYNONYMS.name(),
                    Deck.DeckType.TRANSLATION.name()
            };

            dropdownView.setAdapter(
                    new ArrayAdapter<>(this, R.layout.dropdown_list_item, types)
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
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.fillInStackTrace();
            submit(false);
        }

    }

    public void submit(boolean status) {
        Intent intent = new Intent();
        intent.putExtra(CREATE_DECK_STATUS, status);
        setResult(RESULT_OK, intent);
        finish();
    }
}

