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
    private static final int DATABASE_VERSION = 7;

    // Table Lessons
    private static final String TABLE_LESSONS = "lessons";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DIFFICULTY = "difficulty";

    // Table SRS
    private static final String TABLE_SRS = "srs_review";
    private static final String COLUMN_SRS_ID = "srs_id";
    private static final String COLUMN_SRS_LESSON_ID = "lesson_id";
    private static final String COLUMN_SRS_BOX = "current_box";
    private static final String COLUMN_SRS_NEXT_REVIEW = "next_review_timestamp";

    // Table Sync
    private static final String TABLE_SYNC = "sync_queue";
    private static final String COLUMN_SYNC_ID = "sync_id";
    private static final String COLUMN_SYNC_ACTION = "action";
    private static final String COLUMN_SYNC_PAYLOAD = "payload";
    private static final String COLUMN_SYNC_TIMESTAMP = "timestamp";

    // Table Lesson Progress (Pillar 1: Persistent Cache)
    private static final String TABLE_PROGRESS = "lesson_progress";
    private static final String COLUMN_PROG_LESSON_ID = "lesson_id";
    private static final String COLUMN_PROG_STEP_INDEX = "current_step_index";
    private static final String COLUMN_PROG_IS_COMPLETED = "is_completed";

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

        String CREATE_SRS_TABLE = "CREATE TABLE " + TABLE_SRS + "("
                + COLUMN_SRS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SRS_LESSON_ID + " INTEGER UNIQUE,"
                + COLUMN_SRS_BOX + " INTEGER DEFAULT 1,"
                + COLUMN_SRS_NEXT_REVIEW + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_SRS_LESSON_ID + ") REFERENCES " + TABLE_LESSONS + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_SRS_TABLE);

        String CREATE_SYNC_TABLE = "CREATE TABLE " + TABLE_SYNC + "("
                + COLUMN_SYNC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SYNC_ACTION + " TEXT,"
                + COLUMN_SYNC_PAYLOAD + " TEXT,"
                + COLUMN_SYNC_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_SYNC_TABLE);

        String CREATE_PROGRESS_TABLE = "CREATE TABLE " + TABLE_PROGRESS + "("
                + COLUMN_PROG_LESSON_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PROG_STEP_INDEX + " INTEGER DEFAULT 0,"
                + COLUMN_PROG_IS_COMPLETED + " INTEGER DEFAULT 0,"
                + "FOREIGN KEY(" + COLUMN_PROG_LESSON_ID + ") REFERENCES " + TABLE_LESSONS + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_PROGRESS_TABLE);
        
        seedLessons(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 7) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SRS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNC);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS);
            onCreate(db);
        }
    }

    private void seedLessons(SQLiteDatabase db) {
        insertLesson(db, "Les Noms", "[RULE] Un nom désigne une personne, un lieu ou une chose.\nIls ont un genre (masculin/féminin) et un nombre (singulier/pluriel).\n\n[EXAMPLE] Le chat -> [Masculin]\n[EXAMPLE] La ville -> [Féminin]\n[EXCEPTION] Certains noms ont la même forme au masculin et au féminin. Ex: Un/Une enfant.", "basics", 1);
        
        insertLesson(db, "Les Articles", "[RULE] Les articles se placent devant le nom.\n[RULE] Les articles définis (le, la, l', les) désignent une chose précise.\n[EXAMPLE] Le livre de Paul -> [Spécifique]\n[RULE] Les articles indéfinis (un, une, des) désignent une chose non précisée.\n[EXAMPLE] Un livre -> [N'importe lequel]", "basics", 1);
        
        insertLesson(db, "Les Pronoms Sujets", "[RULE] Ils remplacent le nom pour éviter les répétitions.\n[RULE] Singulier : Je, Tu, Il, Elle, On.\n[RULE] Pluriel : Nous, Vous, Ils, Elles.\n[EXAMPLE] Marie mange -> [Elle mange]\n[EXAMPLE] Paul et moi -> [Nous]", "basics", 1);
        
        insertLesson(db, "La Phrase Simple", "[RULE] Une phrase commence par une majuscule et finit par un point.\n[RULE] Structure de base : Sujet + Verbe + Complément.\n[EXAMPLE] Le chat (Sujet) dort (Verbe).\n[EXAMPLE] Marie regarde la télé -> [S + V + C]", "phrases", 1);
        
        insertLesson(db, "La Négation", "[RULE] Pour transformer une phrase à la forme négative, on utilise 'ne' et 'pas'.\n[RULE] 'ne' se place avant le verbe et 'pas' après.\n[EXAMPLE] Je travaille -> [Je ne travaille pas]\n[EXCEPTION] Devant une voyelle, 'ne' devient 'n''.\n[EXAMPLE] J'aime -> [Je n'aime pas]", "phrases", 2);
        
        insertLesson(db, "L'Interrogation", "[RULE] Il y a trois niveaux de langue pour poser une question.\n[EXAMPLE] Langue familière (intonation) : Tu viens ?\n[EXAMPLE] Langue courante (Est-ce que) : Est-ce que tu viens ?\n[EXAMPLE] Langue soutenue (inversion) : Viens-tu ?", "phrases", 2);
        
        insertLesson(db, "Salutations", "[RULE] Dire bonjour selon le moment de la journée.\n[EXAMPLE] Matin : Bonjour !\n[EXAMPLE] Soir : Bonsoir !\n[RULE] Entre amis, on peut utiliser des formes plus simples.\n[EXAMPLE] Salut ! -> [Informel]", "greeting", 1);
        
        insertLesson(db, "Se Présenter", "[RULE] Pour donner son prénom ou son nom.\n[EXAMPLE] Je m'appelle Jean.\n[EXAMPLE] Mon nom est Dupont.\n[RULE] Pour demander le nom de quelqu'un.\n[EXAMPLE] Comment t'appelles-tu ?", "greeting", 1);
        
        insertLesson(db, "Les Repas", "[RULE] Les quatre moments de consommation en France.\n[EXAMPLE] 8h : Le petit-déjeuner\n[EXAMPLE] 12h : Le déjeuner\n[EXAMPLE] 16h : Le goûter\n[EXAMPLE] 20h : Le dîner", "food", 1);
        
        insertLesson(db, "Les Animaux", "[RULE] Vocabulaire des animaux domestiques courants.\n[EXAMPLE] Le chat -> [Miaou]\n[EXAMPLE] Le chien -> [Ouaf]\n[EXAMPLE] L'oiseau -> [Cui-cui]\n[RULE] Les animaux de la ferme.\n[EXAMPLE] La vache -> [Meuh]", "animal", 1);

        insertLesson(db, "Le Passé Composé", "[RULE] Utilisé pour exprimer une action ponctuelle terminée dans le passé.\n[RULE] Formation : Auxiliaire (Être ou Avoir) au présent + Participe Passé.\n[EXAMPLE] J'ai fini mes devoirs.\n[EXCEPTION] 14 verbes de mouvement utilisent l'auxiliaire ÊTRE.\n[EXAMPLE] Il est allé au cinéma.", "advanced", 3);
        
        insertLesson(db, "Le Futur Simple", "[RULE] Le futur simple exprime une action qui aura lieu plus tard.\n[RULE] Pour les verbes en -er et -ir, on garde l'infinitif et on ajoute les terminaisons : -ai, -as, -a, -ons, -ez, -ont.\n[EXAMPLE] Je parlerai français.\n[EXAMPLE] Tu finiras ton travail.\n[EXCEPTION] Les verbes irréguliers changent de radical : Être -> ser-, Avoir -> aur-.", "advanced", 3);
        
        insertLesson(db, "Les Adverbes", "[RULE] Un adverbe modifie un verbe, un adjectif ou un autre adverbe.\n[RULE] Beaucoup d'adverbes se forment à partir de l'adjectif féminin + -ment.\n[EXAMPLE] Heureuse -> Heureusement\n[EXAMPLE] Lente -> Lentement\n[EXCEPTION] Certains sont irréguliers : Bon -> Bien, Mauvais -> Mal.", "advanced", 2);

        insertLesson(db, "Le Conditionnel", "[RULE] Utilisé pour exprimer un souhait, un conseil ou une hypothèse.\n[RULE] Formation : Radical du futur + terminaisons de l'imparfait (-ais, -ais, -ait, -ions, -iez, -aient).\n[EXAMPLE] J'aimerais voyager.\n[EXAMPLE] Si j'avais le temps, je dormirais.\n[EXAMPLE] Pourriez-vous m'aider ? -> [Politesse]", "advanced", 3);
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
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LESSONS, null);
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

    public void addToSyncQueue(String action, String payload) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SYNC_ACTION, action);
        values.put(COLUMN_SYNC_PAYLOAD, payload);
        db.insert(TABLE_SYNC, null, values);
    }

    public List<SyncAction> getSyncQueue() {
        List<SyncAction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SYNC, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new SyncAction(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void clearSyncQueue() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SYNC, null, null);
    }

    // --- Progress Persistence Methods ---

    public int getLessonProgress(int lessonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROGRESS, new String[]{COLUMN_PROG_STEP_INDEX}, 
                COLUMN_PROG_LESSON_ID + "=?", new String[]{String.valueOf(lessonId)}, null, null, null);
        int index = 0;
        if (cursor != null && cursor.moveToFirst()) {
            index = cursor.getInt(0);
            cursor.close();
        }
        return index;
    }

    public void saveLessonProgress(int lessonId, int stepIndex, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROG_LESSON_ID, lessonId);
        values.put(COLUMN_PROG_STEP_INDEX, stepIndex);
        values.put(COLUMN_PROG_IS_COMPLETED, isCompleted ? 1 : 0);
        
        long result = db.insertWithOnConflict(TABLE_PROGRESS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        if (result == -1) {
            throw new RuntimeException("Persistence failure: Could not save progress for lesson " + lessonId);
        }
    }

    public static class SyncAction {
        public int id;
        public String action;
        public String payload;
        public SyncAction(int id, String action, String payload) {
            this.id = id; this.action = action; this.payload = payload;
        }
    }
}
