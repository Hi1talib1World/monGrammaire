package com.example.mongrammaire.courslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mongrammaire.Model.Lesson;
import com.example.mongrammaire.R;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    private final List<Lesson> lessons;
    private final OnLessonClickListener listener;

    public interface OnLessonClickListener {
        void onLessonClick(Lesson lesson);
    }

    public LessonAdapter(List<Lesson> lessons, OnLessonClickListener listener) {
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.title.setText(lesson.getTitle());
        holder.description.setText(lesson.getDescription());
        
        if (lesson.isLocked()) {
            holder.lockIcon.setVisibility(View.VISIBLE);
            holder.itemView.setAlpha(0.5f);
        } else {
            holder.lockIcon.setVisibility(View.GONE);
            holder.itemView.setAlpha(1.0f);
        }

        holder.itemView.setOnClickListener(v -> listener.onLessonClick(lesson));
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView lockIcon;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.lessonTitle);
            description = view.findViewById(R.id.lessonDescription);
            lockIcon = view.findViewById(R.id.lockIcon);
        }
    }
}
