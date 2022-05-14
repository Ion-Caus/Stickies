package com.ionc.stickies.ui;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ionc.stickies.R;

public class CardViewHolder {
    public final TextView word;
    public final TextView explanations;
    public final TextView partOfSpeech;
    public final ImageButton favouriteButton;

    private final CardAnimator animator;

    public CardViewHolder(@NonNull View itemView) {

        word = itemView.findViewById(R.id.tv_word);
        explanations = itemView.findViewById(R.id.tv_explanations);
        partOfSpeech = itemView.findViewById(R.id.tv_partOfSpeech);
        favouriteButton = itemView.findViewById(R.id.favourite_card_btn);

        animator = new CardAnimator(itemView, 600);

        itemView.setOnClickListener(v -> animator.flipCard());
    }

    public void resetAnimation() {
        animator.resetAnimation();
    }
}