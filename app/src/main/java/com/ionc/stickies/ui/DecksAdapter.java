package com.ionc.stickies.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ionc.stickies.R;
import com.ionc.stickies.model.Deck;

import java.util.ArrayList;
import java.util.List;

public class DecksAdapter extends RecyclerView.Adapter<DecksAdapter.ViewHolder> {
    private final List<Deck> decks;
    private DecksAdapter.OnClickListener listener;

    public DecksAdapter(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public void setOnClickListener(DecksAdapter.OnClickListener listener) {
        this.listener = listener;
    }


    public void setDecks(List<Deck> decks) {
        this.decks.clear();
        this.decks.addAll(decks);
    }

    public List<Deck> getDecks() {
        return decks;
    }

    @NonNull
    @Override
    public DecksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_deck, parent, false);
        return new DecksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DecksAdapter.ViewHolder holder, int position) {
        String deckId = decks.get(position).getId() + ".";
        holder.deckId.setText(deckId);
        holder.deckName.setText(decks.get(position).getName());
        holder.deckType.setText(decks.get(position).getDeckType().toString());
    }

    @Override
    public int getItemCount() {
        return decks.size();
    }


    // View Holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView deckId;
        private final TextView deckName;
        private final TextView deckType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deckId = itemView.findViewById(R.id.deck_id);
            deckName = itemView.findViewById(R.id.deck_name);
            deckType = itemView.findViewById(R.id.deck_type);

            itemView.setOnClickListener(v -> listener.onClick(decks.get(getBindingAdapterPosition())));
        }
    }

    // On Click Listener
    public interface OnClickListener {
        void onClick(Deck deck);
    }
}
