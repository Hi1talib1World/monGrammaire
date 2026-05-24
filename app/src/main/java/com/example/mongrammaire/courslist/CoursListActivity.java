package com.example.mongrammaire.courslist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import com.example.mongrammaire.Utils.ToastHelper;
import com.example.mongrammaire.Model.Lesson;
import com.example.mongrammaire.R;
import com.example.mongrammaire.Utils.ProgressionManager;
import java.util.ArrayList;
import java.util.List;

public class CoursListActivity extends AppCompatActivity implements LessonAdapter.OnLessonClickListener {

    private RecyclerView recyclerView;
    private LessonAdapter adapter;
    private List<Lesson> lessonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cour);
        
        ProgressionManager.init(this);

        recyclerView = findViewById(R.id.lessonRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initLessons();
        
        int unlockedLevel = ProgressionManager.getUnlockedLevel();
        for (Lesson lesson : lessonList) {
            if (lesson.getLevel() <= unlockedLevel) {
                lesson.setLocked(false);
            }
        }

        adapter = new LessonAdapter(lessonList, this);
        recyclerView.setAdapter(adapter);
    }

    private void initLessons() {
        lessonList = new ArrayList<>();
        lessonList.add(new Lesson(1, "Les noms", "Introduction aux noms et leurs genres", 1));
        lessonList.add(new Lesson(2, "Les articles", "Définis, indéfinis et partitifs", 2));
        lessonList.add(new Lesson(3, "Les adjectifs", "Accords et places des adjectifs", 3));
        lessonList.add(new Lesson(4, "Le présent", "Conjugaison des verbes au présent", 4));
        lessonList.add(new Lesson(5, "Le passé composé", "Utilisation des auxiliaires être et avoir", 5));
        lessonList.add(new Lesson(6, "L'imparfait", "Formation et emplois de l'imparfait", 6));
        lessonList.add(new Lesson(7, "Le futur proche", "Exprimer des actions futures imminentes", 7));
        lessonList.add(new Lesson(8, "Le subjonctif", "Introduction au mode subjonctif", 8));
    }

    @Override
    public void onLessonClick(Lesson lesson) {
        if (lesson.isLocked()) {
            ToastHelper.showCustomToast(this, "Niveau verrouillé ! Complétez les quiz précédents.");
        } else {
            Intent intent = new Intent(this, LessonActivity1.class);
            startActivity(intent);
        }
    }
}
