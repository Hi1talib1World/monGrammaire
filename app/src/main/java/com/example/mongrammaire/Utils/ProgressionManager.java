package com.example.mongrammaire.Utils;

import android.content.Context;
import com.orhanobut.hawk.Hawk;
import java.util.Calendar;
import java.util.Date;

public class ProgressionManager {

    private static final String KEY_STREAK = "current_streak";
    private static final String KEY_LAST_ACTIVE_DATE = "last_active_date";
    private static final String KEY_UNLOCKED_LEVEL = "unlocked_level";
    private static final String KEY_USER_SCORE = "user_score";

    public static void init(Context context) {
        Hawk.init(context).build();
    }

    public static int getStreak() {
        return Hawk.get(KEY_STREAK, 0);
    }

    public static void updateStreak() {
        long lastActive = Hawk.get(KEY_LAST_ACTIVE_DATE, 0L);
        long today = getStartOfDay(new Date()).getTime();

        if (lastActive == 0) {
            Hawk.put(KEY_STREAK, 1);
        } else if (today > lastActive) {
            if (today - lastActive <= 24 * 60 * 60 * 1000) {
                // Consecutive day
                Hawk.put(KEY_STREAK, getStreak() + 1);
            } else {
                // Streak broken
                Hawk.put(KEY_STREAK, 1);
            }
        }
        Hawk.put(KEY_LAST_ACTIVE_DATE, today);
    }

    public static int getUnlockedLevel() {
        return Hawk.get(KEY_UNLOCKED_LEVEL, 1); // Default level 1 unlocked
    }

    public static void unlockNextLevel(int currentScore) {
        int unlocked = getUnlockedLevel();
        if (currentScore > 50 && unlocked < 8) { // Example condition: score > 50 to unlock next
            Hawk.put(KEY_UNLOCKED_LEVEL, unlocked + 1);
        }
    }

    public static int getUserScore() {
        return Hawk.get(KEY_USER_SCORE, 0);
    }

    public static void addScore(int points) {
        Hawk.put(KEY_USER_SCORE, getUserScore() + points);
    }

    private static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
