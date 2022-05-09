package com.ionc.stickies.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ionc.stickies.R;
import com.ionc.stickies.model.SynonymsCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SynonymsCardsAdapter extends RecyclerView.Adapter<SynonymsCardsAdapter.ViewHolder> {
    private final List<SynonymsCard> synonymsCards;
    private OnClickListener listener;

    public SynonymsCardsAdapter(ArrayList<SynonymsCard> synonymsCards) {
        this.synonymsCards = synonymsCards;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }


    public void setSynonymsCards(List<SynonymsCard> synonymsCards) {
        this.synonymsCards.clear();
        this.synonymsCards.addAll(synonymsCards);
    }

    public List<SynonymsCard> getCards() {
        return synonymsCards;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_synonyms_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.word.setText(synonymsCards.get(position).getWord());
        holder.synonyms.setText(Arrays.toString(synonymsCards.get(position).getSynonyms()));
        holder.partOfSpeech.setText(synonymsCards.get(position).getPartOfSpeech().name());
    }

    @Override
    public int getItemCount() {
        return synonymsCards.size();
    }



    // View Holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView word;
        private final TextView synonyms;
        private final TextView partOfSpeech;

        private final CardAnimator animator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.tv_word);
            synonyms = itemView.findViewById(R.id.tv_synonyms);
            partOfSpeech = itemView.findViewById(R.id.tv_partOfSpeech);

            animator = new CardAnimator(itemView, 600);

            itemView.setOnClickListener(v -> {
                animator.flipSynonymCard();
                listener.onClick(synonymsCards.get(getBindingAdapterPosition()));
            });
        }
    }

    // On Click Listener
    public interface OnClickListener {
        void onClick(SynonymsCard card);
    }
}
