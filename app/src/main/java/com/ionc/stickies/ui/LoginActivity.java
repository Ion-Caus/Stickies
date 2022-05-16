package com.ionc.stickies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.ionc.stickies.R;

import java.util.Collections;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;

    private CardViewHolder cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        checkIfSignedIn();

        initViews();
        displayCard();
    }

    private void initViews() {
        View cardLayout = findViewById(R.id.layout_card_intro);
        cardView = new CardViewHolder(cardLayout);

        findViewById(R.id.button_login_google).setOnClickListener(view -> signIn());
    }

    private void checkIfSignedIn() {
        viewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                String message = "Welcome " + user.getDisplayName();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                loadMainActivity();
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() != RESULT_OK) {
                    Toast.makeText(this, "SIGN IN CANCELLED", Toast.LENGTH_SHORT).show();
                }
            });


    private void signIn() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_logo)
                .build();

        activityResultLauncher.launch(signInIntent);
    }

    private void loadMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void displayCard() {
        cardView.word.setText( R.string.intro_question );
        cardView.partOfSpeech.setText( R.string.intro_italic );
        cardView.explanations.setText( R.string.intro_answer );
        cardView.favouriteButton.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
    }
}
