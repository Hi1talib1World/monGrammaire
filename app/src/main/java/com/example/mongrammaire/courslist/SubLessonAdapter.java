package com.example.mongrammaire.courslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.Model.LessonModel;
import com.example.mongrammaire.R;

import java.util.List;

public class SubLessonAdapter extends RecyclerView.Adapter<SubLessonAdapter.ViewHolder> {

    private List<LessonModel> lessons;
    private OnLessonClickListener listener;

    public interface OnLessonClickListener {
        void onLessonLongClick(LessonModel lesson);
    }

    public SubLessonAdapter(List<LessonModel> lessons, OnLessonClickListener listener) {
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LessonModel lesson = lessons.get(position);
        holder.title.setText(lesson.getTitle());
        holder.content.setText(lesson.getContent());
        holder.difficulty.setText("Difficulty: " + getDifficultyString(lesson.getDifficulty()));

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onLessonLongClick(lesson);
            }
            return true;
        });
    }

    private String getDifficultyString(int difficulty) {
        switch (difficulty) {
            case 1: return "Easy";
            case 2: return "Medium";
            case 3: return "Hard";
            default: return "Unknown";
        }
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, difficulty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sub_lesson_title);
            content = itemView.findViewById(R.id.sub_lesson_content);
            difficulty = itemView.findViewById(R.id.sub_lesson_difficulty);
        }
    }
}
