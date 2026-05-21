package com.example.mongrammaire.Quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

class TriviaQuizHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "TQuiz.db";
    private static final int DB_VERSION = 4; // Incremented version
    private static final String TABLE_NAME = "TQ";
    private static final String UID = "_UID";
    private static final String QUESTION = "QUESTION";
    private static final String OPTA = "OPTA";
    private static final String OPTB = "OPTB";
    private static final String OPTC = "OPTC";
    private static final String OPTD = "OPTD";
    private static final String ANSWER = "ANSWER";
    
    // Spaced Repetition Columns
    private static final String NEXT_REVIEW = "NEXT_REVIEW";
    private static final String INTERVAL = "INTERVAL";
    private static final String EASE_FACTOR = "EASE_FACTOR";
    private static final String REPETITIONS = "REPETITIONS";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " 
            + UID + " INTEGER PRIMARY KEY AUTOINCREMENT , " 
            + QUESTION + " VARCHAR(255), " 
            + OPTA + " VARCHAR(255), " 
            + OPTB + " VARCHAR(255), " 
            + OPTC + " VARCHAR(255), " 
            + OPTD + " VARCHAR(255), " 
            + ANSWER + " VARCHAR(255), "
            + NEXT_REVIEW + " INTEGER DEFAULT 0, "
            + INTERVAL + " INTEGER DEFAULT 0, "
            + EASE_FACTOR + " REAL DEFAULT 2.5, "
            + REPETITIONS + " INTEGER DEFAULT 0);";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    TriviaQuizHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    void allQuestion() {
        ArrayList<TriviaQuestion> arraylist = new ArrayList<>();

        arraylist.add(new TriviaQuestion("Choisissez la forme correcte : 'Ils (manger) une pomme.'", "mange", "manges", "mangent", "mangez", "mangent"));
        arraylist.add(new TriviaQuestion("Quel est le pluriel correct de 'le ciel' ?", "les ciels", "les cielle", "les cieux", "les cieuxs", "les cieux"));
        arraylist.add(new TriviaQuestion("Complétez la phrase : 'Elle ____ partie tôt ce matin.'", "a", "est", "es", "as", "est"));
        arraylist.add(new TriviaQuestion("Quel est le participe passé du verbe 'prendre' ?", "prendu", "pris", "prind", "prenne", "pris"));
        arraylist.add(new TriviaQuestion("Choisissez le pronom correct : 'Je parle à Pierre. Je ____ parle.'", "le", "la", "lui", "les", "lui"));
        arraylist.add(new TriviaQuestion("Quel temps est utilisé dans 'Nous mangions' ?", "Présent", "Futur simple", "Passé composé", "Imparfait", "Imparfait"));
        arraylist.add(new TriviaQuestion("Accordez l'adjectif : 'Des fleurs (blanc)'.", "blancs", "blanche", "blanches", "blanc", "blanches"));
        arraylist.add(new TriviaQuestion("Complétez : 'C'est le film ____ je vous ai parlé.'", "que", "qui", "dont", "où", "dont"));
        arraylist.add(new TriviaQuestion("Quel est le féminin de l'adjectif 'beau' ?", "belle", "belle", "belles", "beaus", "belle"));
        arraylist.add(new TriviaQuestion("Choisissez la bonne conjugaison : 'Vous (aller) au marché.'", "allons", "allez", "vont", "vas", "allez"));
        arraylist.add(new TriviaQuestion("Quel est l'auxiliaire utilisé pour 'Il est tombé' ?", "Avoir", "Être", "Faire", "Aller", "Être"));
        arraylist.add(new TriviaQuestion("Complétez : 'Si j'avais su, je ____ venu.'", "serais", "serai", "suis", "étais", "serais"));
        arraylist.add(new TriviaQuestion("Quel est le contraire de 'toujours' ?", "souvent", "parfois", "jamais", "encore", "jamais"));
        arraylist.add(new TriviaQuestion("Identifiez le COD : 'Le chat mange la souris.'", "Le chat", "mange", "la souris", "mange la", "la souris"));
        arraylist.add(new TriviaQuestion("Quel est le pluriel de 'journal' ?", "journals", "journaux", "journale", "journauxs", "journaux"));
        arraylist.add(new TriviaQuestion("Choisissez : '____-tu faim ?'", "Est", "As", "Es", "A", "As"));
        arraylist.add(new TriviaQuestion("Complétez : 'Je vais ____ France.'", "au", "à la", "en", "aux", "en"));
        arraylist.add(new TriviaQuestion("Quel est le futur simple de 'je suis' ?", "je serai", "je serais", "j'étais", "je fus", "je serai"));
        arraylist.add(new TriviaQuestion("Accordez : 'Les filles sont (partir)'.", "parti", "partis", "partie", "parties", "partie"));
        arraylist.add(new TriviaQuestion("Complétez : 'L'homme ____ chante est mon oncle.'", "qui", "que", "dont", "où", "qui"));

        this.addAllQuestions(arraylist);
    }

    private void addAllQuestions(ArrayList<TriviaQuestion> allQuestions) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (TriviaQuestion question : allQuestions) {
                values.put(QUESTION, question.getQuestion());
                values.put(OPTA, question.getOptA());
                values.put(OPTB, question.getOptB());
                values.put(OPTC, question.getOptC());
                values.put(OPTD, question.getOptD());
                values.put(ANSWER, question.getAnswer());
                values.put(NEXT_REVIEW, question.getNextReviewTime());
                values.put(INTERVAL, question.getInterval());
                values.put(EASE_FACTOR, question.getEaseFactor());
                values.put(REPETITIONS, question.getRepetitions());
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    List<TriviaQuestion> getAllOfTheQuestions() {
        List<TriviaQuestion> questionsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {UID, QUESTION, OPTA, OPTB, OPTC, OPTD, ANSWER, NEXT_REVIEW, INTERVAL, EASE_FACTOR, REPETITIONS};
        Cursor cursor = db.query(TABLE_NAME, coloumn, null, null, null, null, null);

        while (cursor.moveToNext()) {
            TriviaQuestion question = new TriviaQuestion();
            question.setId(cursor.getInt(0));
            question.setQuestion(cursor.getString(1));
            question.setOptA(cursor.getString(2));
            question.setOptB(cursor.getString(3));
            question.setOptC(cursor.getString(4));
            question.setOptD(cursor.getString(5));
            question.setAnswer(cursor.getString(6));
            question.setNextReviewTime(cursor.getLong(7));
            question.setInterval(cursor.getInt(8));
            question.setEaseFactor(cursor.getFloat(9));
            question.setRepetitions(cursor.getInt(10));
            questionsList.add(question);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return questionsList;
    }

    public void updateQuestionProgression(TriviaQuestion question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NEXT_REVIEW, question.getNextReviewTime());
        values.put(INTERVAL, question.getInterval());
        values.put(EASE_FACTOR, question.getEaseFactor());
        values.put(REPETITIONS, question.getRepetitions());
        db.update(TABLE_NAME, values, UID + " = ?", new String[]{String.valueOf(question.getId())});
        db.close();
    }
}
