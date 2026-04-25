package com.example.mongrammaire.Data.Local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mongrammaire.Model.LessonModel;

import java.util.ArrayList;
import java.util.List;

public class LessonDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Lessons.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_LESSONS = "lessons";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DIFFICULTY = "difficulty";

    public LessonDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LESSONS_TABLE = "CREATE TABLE " + TABLE_LESSONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_CONTENT + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_DIFFICULTY + " INTEGER" + ")";
        db.execSQL(CREATE_LESSONS_TABLE);
        
        seedLessons(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSONS);
        onCreate(db);
    }

    private void seedLessons(SQLiteDatabase db) {
        // Module 1: Basics
        insertLesson(db, "Introduction to Nouns", "A noun is a word that represents a person, place, thing, or idea. Examples: dog, city, happiness.", "Basics", 1);
        insertLesson(db, "Pronouns 101", "Pronouns replace nouns to avoid repetition. Subject pronouns include I, you, he, she, it, we, they.", "Basics", 1);
        insertLesson(db, "Action Verbs", "Verbs express action or state of being. 'Run', 'eat', and 'sleep' are action verbs.", "Basics", 1);
        
        // Module 2: Phrases
        insertLesson(db, "Ordering Food", "Useful phrases: 'I would like...', 'Can I have the menu?', 'The bill, please.'", "Phrases", 1);
        insertLesson(db, "Asking for Directions", "Phrases: 'Where is the...?', 'How do I get to...?', 'Turn left/right.'", "Phrases", 1);
        insertLesson(db, "Emergency Phrases", "Important: 'Help me!', 'Call an ambulance', 'I am lost.'", "Phrases", 2);

        // Module 3: Greetings
        insertLesson(db, "Formal Greetings", "Used with strangers or in business: 'Good morning', 'How do you do?', 'It is a pleasure to meet you.'", "Greeting", 1);
        insertLesson(db, "Informal Greetings", "Used with friends: 'Hi', 'What's up?', 'How's it going?'", "Greeting", 1);
        insertLesson(db, "Saying Goodbye", "Ways to leave: 'See you later', 'Take care', 'Have a nice day.'", "Greeting", 1);

        // Module 4: Food
        insertLesson(db, "Fruit and Vegetables", "Common items: Apple, Banana, Carrot, Broccoli, Tomato.", "Food", 1);
        insertLesson(db, "Drinks and Beverages", "Water, Coffee, Tea, Juice, Milk.", "Food", 1);
        insertLesson(db, "Meals of the Day", "Breakfast (morning), Lunch (midday), Dinner (evening), Snack.", "Food", 1);

        // Module 5: Animals
        insertLesson(db, "Pets", "Animals at home: Cat, Dog, Hamster, Rabbit, Fish.", "Animal", 1);
        insertLesson(db, "Wild Animals", "In nature: Lion, Tiger, Elephant, Bear, Wolf.", "Animal", 2);
        insertLesson(db, "Birds and Insects", "Flying creatures: Eagle, Sparrow, Bee, Butterfly, Ant.", "Animal", 2);

        // Module 6: Clothing
        insertLesson(db, "Daily Clothes", "Shirt, Pants, Socks, Underwear, Shoes.", "Clothing", 1);
        insertLesson(db, "Outerwear", "Jacket, Coat, Scarf, Gloves, Hat.", "Clothing", 1);
        insertLesson(db, "Accessories", "Watch, Belt, Sunglasses, Wallet, Backpack.", "Clothing", 2);
        
        // Advanced Modules
        insertLesson(db, "Present Continuous", "Describes actions happening now: 'I am reading', 'She is dancing'. Structure: Be + Verb-ing.", "Advanced", 3);
        insertLesson(db, "Past Tense", "Describes completed actions. Regular verbs end in -ed: 'walked', 'talked'.", "Advanced", 3);
    }

    private void insertLesson(SQLiteDatabase db, String title, String content, String category, int difficulty) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DIFFICULTY, difficulty);
        db.insert(TABLE_LESSONS, null, values);
    }

    public List<LessonModel> getAllLessons() {
        List<LessonModel> lessonList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_LESSONS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LessonModel lesson = new LessonModel();
                lesson.setId(cursor.getInt(0));
                lesson.setTitle(cursor.getString(1));
                lesson.setContent(cursor.getString(2));
                lesson.setCategory(cursor.getString(3));
                lesson.setDifficulty(cursor.getInt(4));
                lessonList.add(lesson);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lessonList;
    }

    public List<LessonModel> getLessonsByCategory(String category) {
        List<LessonModel> lessonList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LESSONS, null, COLUMN_CATEGORY + "=?", new String[]{category}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                LessonModel lesson = new LessonModel();
                lesson.setId(cursor.getInt(0));
                lesson.setTitle(cursor.getString(1));
                lesson.setContent(cursor.getString(2));
                lesson.setCategory(cursor.getString(3));
                lesson.setDifficulty(cursor.getInt(4));
                lessonList.add(lesson);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lessonList;
    }
}
