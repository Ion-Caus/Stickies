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

        updateViewCard(view);
    }

    public void flipCard() {
        if (isFlipped) {
            animator.reverse();
        }
        else {
            animator.start();
        }
        isFlipped = !isFlipped;
    }

    public void resetAnimation() {
        if (isFlipped) {
            animator.reverse();
            isFlipped = false;
        }
    }

    private void updateViewCard(View view) {

        View tvExplanations = view.findViewById(R.id.tv_explanations);
        View tvWord = view.findViewById(R.id.tv_word);
        View tvPartOfSpeech = view.findViewById(R.id.tv_partOfSpeech);

        animator.addUpdateListener(valueAnimator -> {
            float value = valueAnimator.getAnimatedFraction();

            if(value <= 0.5f){
                tvWord.setVisibility(View.VISIBLE);
                tvPartOfSpeech.setVisibility(View.VISIBLE);

                tvExplanations.setVisibility(View.GONE);
            }
            else {
                tvWord.setVisibility(View.GONE);
                tvPartOfSpeech.setVisibility(View.GONE);

                tvExplanations.setRotationY(DEGREES_180);
                tvExplanations.setVisibility(View.VISIBLE);
            }
        });

    }

}
