package com.ionc.stickies.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ionc.stickies.R;
import com.ionc.stickies.model.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {
    private final List<Card> cards;
    private OnClickListener listener;

    public CardsAdapter(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }


    public void setCards(List<Card> cards) {
        this.cards.clear();
        this.cards.addAll(cards);
    }

    public List<Card> getCards() {
        return cards;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_explanations_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.word.setText(cards.get(position).getWord());
        holder.explanations.setText(Arrays.toString(cards.get(position).getExplanations()));
        holder.partOfSpeech.setText(cards.get(position).getPartOfSpeech().name());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }



    // View Holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView word;
        private final TextView explanations;
        private final TextView partOfSpeech;

        private final CardAnimator animator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.tv_word);
            explanations = itemView.findViewById(R.id.tv_explanations);
            partOfSpeech = itemView.findViewById(R.id.tv_partOfSpeech);

            animator = new CardAnimator(itemView, 600);

            itemView.setOnClickListener(v -> {
                animator.flipCard();
                listener.onClick(cards.get(getBindingAdapterPosition()));
            });
        }
    }

    // On Click Listener
    public interface OnClickListener {
        void onClick(Card card);
    }
}
