package com.ionc.stickies.ui;

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

import com.ionc.stickies.R;
import com.ionc.stickies.model.Card;

import java.util.Arrays;

public class PlayFragment extends Fragment {

    private PlayViewModel playViewModel;

    private NavController navController;
    private CardViewHolder cardView;

    private Button goodBtn;
    private Button okBtn;
    private Button badBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initViewModel();
        initNavController(view);

        setupButtonsPress();
        startGame();
    }

    private void initViews(@NonNull View view) {
        View cardLayout = view.findViewById(R.id.layout_card);
        cardView = new CardViewHolder(cardLayout);
        
        goodBtn = view.findViewById(R.id.play_btn_good);
        okBtn = view.findViewById(R.id.play_btn_ok);
        badBtn = view.findViewById(R.id.play_btn_bad);
    }

    private void initNavController(@NonNull View view) {
        navController = Navigation.findNavController(view);
    }

    private void initViewModel() {
        playViewModel = new ViewModelProvider(this).get(PlayViewModel.class);
    }

    private void startGame() {
        playViewModel.loadData();
        displayNextCard();
    }

    private void displayNextCard() {
        Card card = playViewModel.getNextDisplayCard();
        if (card == null) {
            Toast.makeText(getActivity(), "Play time over.", Toast.LENGTH_SHORT).show();
            navController.popBackStack();
            return;
        }
        cardView.word.setText( card.getWord() );
        cardView.partOfSpeech.setText( card.getPartOfSpeech().name() );
        cardView.explanations.setText( Arrays.toString(card.getExplanations()) );

        int icon = card.isFavourite()
                ? R.drawable.ic_baseline_favorite_24
                : R.drawable.ic_baseline_favorite_border_24;
        cardView.favouriteButton.setBackgroundResource(icon);
    }

    private void setupButtonsPress() {
        goodBtn.setOnClickListener(v -> {
            cardView.resetAnimation();
            playViewModel.setGoodRecallScore();
            displayNextCard();
        });

        okBtn.setOnClickListener(v -> {
            cardView.resetAnimation();
            playViewModel.setOkRecallScore();
            displayNextCard();
        });

        badBtn.setOnClickListener(v -> {
            cardView.resetAnimation();
            playViewModel.setBadRecallScore();
            displayNextCard();
        });
    }
}
