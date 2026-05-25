package com.example.mongrammaire;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.mongrammaire.Data.Local.LessonDatabaseHelper;
import com.example.mongrammaire.Model.LessonModel;
import com.example.mongrammaire.courslist.DetailsActivity;

import java.util.List;
import java.util.Random;

public class GrammarWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        LessonDatabaseHelper db = new LessonDatabaseHelper(context);
        List<LessonModel> lessons = db.getAllLessons();
        
        if (lessons == null || lessons.isEmpty()) return;

        LessonModel randomLesson = lessons.get(new Random().nextInt(lessons.size()));

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.rule_widget);
            views.setTextViewText(R.id.widget_content, randomLesson.getTitle() + " : " + randomLesson.getContent());

            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("iTitleTv", randomLesson.getTitle());
            intent.putExtra("iDescTv", randomLesson.getCategory());
            intent.putExtra("iContent", randomLesson.getContent());
            
            PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
