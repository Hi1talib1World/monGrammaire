package com.example.mongrammaire;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {
    private List<DictionaryWord> words;

    public DictionaryAdapter(List<DictionaryWord> words) {
        this.words = words;
    }

    public void updateList(List<DictionaryWord> newWords) {
        this.words = newWords;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dictionary_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DictionaryWord word = words.get(position);
        holder.tvWord.setText(word.getWord());
        holder.tvDefinition.setText(word.getDefinition());
        if (word.getPartOfSpeech() != null && !word.getPartOfSpeech().isEmpty()) {
            holder.chipPartOfSpeech.setVisibility(View.VISIBLE);
            holder.chipPartOfSpeech.setText(word.getPartOfSpeech());
        } else {
            holder.chipPartOfSpeech.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWord, tvDefinition;
        Chip chipPartOfSpeech;

        ViewHolder(View view) {
            super(view);
            tvWord = view.findViewById(R.id.tv_word);
            tvDefinition = view.findViewById(R.id.tv_definition);
            chipPartOfSpeech = view.findViewById(R.id.chip_part_of_speech);
        }
    }
}
