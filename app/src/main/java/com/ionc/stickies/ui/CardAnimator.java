package com.ionc.stickies.ui;

import android.animation.ObjectAnimator;
import android.view.View;

import androidx.annotation.NonNull;

import com.ionc.stickies.R;

public class CardAnimator {
    private final int DEGREES_180 = 180;

    private boolean isFlipped;
    private final ObjectAnimator animator;

    public CardAnimator(@NonNull View view, int duration) {
        isFlipped = false;

        animator = ObjectAnimator.ofFloat(view, "rotationY", 0, DEGREES_180);
        animator.setDuration(duration);

        updateViewSynonymCard(view);
    }

    public void flipSynonymCard() {
        if (isFlipped) {
            animator.reverse();
        }
        else {
            animator.start();
        }
        isFlipped = !isFlipped;
    }

    private void updateViewSynonymCard(View view) {

        View tvSynonyms = view.findViewById(R.id.tv_synonyms);
        View tvWord = view.findViewById(R.id.tv_word);

        animator.addUpdateListener(valueAnimator -> {
            float value = valueAnimator.getAnimatedFraction();

            if(value <= 0.5f){
                tvWord.setVisibility(View.VISIBLE);
                tvSynonyms.setVisibility(View.GONE);
            }
            else {
                tvWord.setVisibility(View.GONE);
                tvSynonyms.setRotationY(DEGREES_180);
                tvSynonyms.setVisibility(View.VISIBLE);
            }
        });

    }

}
