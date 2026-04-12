package com.example.mongrammaire.Data;


import android.service.autofill.UserData;

import com.example.mongrammaire.Model.PairModel;
import com.example.mongrammaire.Model.QuestionModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public interface DataSource {

    interface Local{

        ArrayList<PairModel> getPairs();

        QuestionModel getRandomQuestionObj();

        ArrayList<String> getAnswer();
    }

    interface Remote {

        FirebaseDatabase getDatabaseInstance();

        void setNewLanguage(String language);

        void setDailyXp(int xp);

        void setUserTotalXp(int xp);

        void setLastTimeVisited();

        void setDailyGoal(int dailyGoal);

        void setUserInfo(UserData userData);

        void setLessonComplete(String lesson, boolean completeness);

        void setLessonProgress(String lesson, int progress);

        void setLessonCompleteDate(String lesson);

        void setStreak(int streak);

        void getDailyGoal();

        void getDailyXp();

        void getWeekXp();

        void getLessonCompleted();

        void getOverallProgress();

        void getStreak();
    }
}
