package com.example.mongrammaire.Data.Local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mongrammaire.Model.LessonModel;

import android.database.DatabaseUtils;
import java.util.ArrayList;
import java.util.List;

public class LessonDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Lessons.db";
    private static final int DATABASE_VERSION = 12;

    // Table Lessons
    private static final String TABLE_LESSONS = "lessons";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
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

    // Table Lesson Progress
    private static final String TABLE_PROGRESS = "lesson_progress";
    private static final String COLUMN_PROG_LESSON_ID = "lesson_id";
    private static final String COLUMN_PROG_STEP_INDEX = "current_step_index";
    private static final String COLUMN_PROG_IS_COMPLETED = "is_completed";
    private static final String COLUMN_PROG_IS_MASTERED = "is_mastered";
    private static final String COLUMN_PROG_BOOKMARKED_CARD = "bookmarked_card_index";

    // Table App Settings
    private static final String TABLE_SETTINGS = "app_settings";
    private static final String COLUMN_SETTING_KEY = "setting_key";
    private static final String COLUMN_SETTING_VALUE = "setting_value";

    public LessonDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 14); // Incrementing to 14
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LESSONS_TABLE = "CREATE TABLE " + TABLE_LESSONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
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
                + COLUMN_PROG_IS_MASTERED + " INTEGER DEFAULT 0,"
                + COLUMN_PROG_BOOKMARKED_CARD + " INTEGER DEFAULT -1,"
                + "FOREIGN KEY(" + COLUMN_PROG_LESSON_ID + ") REFERENCES " + TABLE_LESSONS + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_PROGRESS_TABLE);

        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
                + COLUMN_SETTING_KEY + " TEXT PRIMARY KEY,"
                + COLUMN_SETTING_VALUE + " TEXT)";
        db.execSQL(CREATE_SETTINGS_TABLE);
        
        seedLessons(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SRS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNC);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
            onCreate(db);
        }
    }

    private void seedLessons(SQLiteDatabase db) {
        // --- GRAMMAIRE ---
        insertLesson(db, "Les Noms", "Introduction aux noms et leurs genres", "[RULE] Un nom désigne une personne, un lieu ou une chose.\nIls ont un genre (masculin/féminin) et un nombre (singulier/pluriel).\n\n[EXAMPLE] Le chat -> [Masculin]\n[EXAMPLE] La ville -> [Féminin]\n[EXCEPTION] Certains noms ont la même forme au masculin et au féminin. Ex: Un/Une enfant.", "Grammaire", 1);
        insertLesson(db, "Les Articles", "Définis, indéfinis et partitifs", "[RULE] Les articles se placent devant le nom.\n[RULE] Les articles définis (le, la, l', les) désignent une chose précise.\n[EXAMPLE] Le livre de Paul -> [Spécifique]\n[RULE] Les articles indéfinis (un, une, des) désignent une chose non précisée.\n[EXAMPLE] Un livre -> [N'importe lequel]", "Grammaire", 1);
        insertLesson(db, "Les Pronoms Sujets", "Je, tu, il, elle...", "[RULE] Ils remplacent le nom pour éviter les répétitions.\n[RULE] Singulier : Je, Tu, Il, Elle, On.\n[RULE] Pluriel : Nous, Vous, Ils, Elles.\n[EXAMPLE] Marie mange -> [Elle mange]\n[EXAMPLE] Paul et moi -> [Nous]", "Grammaire", 1);
        insertLesson(db, "La Phrase Simple", "Sujet + Verbe + Complément", "[RULE] Une phrase commence par une majuscule et finit par un point.\n[RULE] Structure de base : Sujet + Verbe + Complément.\n[EXAMPLE] Le chat (Sujet) dort (Verbe).\n[EXAMPLE] Marie regarde la télé -> [S + V + C]", "Grammaire", 1);
        insertLesson(db, "La Négation", "Ne... pas", "[RULE] Pour transformer une phrase à la forme négative, on utilise 'ne' et 'pas'.\n[RULE] 'ne' se place avant le verbe et 'pas' après.\n[EXAMPLE] Je travaille -> [Je ne travaille pas]\n[EXCEPTION] Devant une voyelle, 'ne' devient 'n''.\n[EXAMPLE] J'aime -> [Je n'aime pas]", "Grammaire", 2);
        insertLesson(db, "Les Adjectifs Possessifs", "Mon, ton, son...", "[RULE] Ils indiquent à qui appartient l'objet.\n[RULE] Masculin : mon, ton, son.\n[EXAMPLE] ____ livre (mon) -> [Mon]\n[EXCEPTION] Devant une voyelle, on utilise 'mon, ton, son' même pour le féminin. [EXAMPLE] ____ amie (ma) -> [mon]", "Grammaire", 1);
        insertLesson(db, "Les Adjectifs Démonstratifs", "Ce, cette, ces", "[RULE] Ils servent à montrer quelque chose.\n[EXAMPLE] ____ chat (ce) -> [Ce]\n[EXCEPTION] Masculin + voyelle/h muet = cet. [EXAMPLE] ____ homme (ce) -> [cet]", "Grammaire", 1);
        insertLesson(db, "L'Accord de l'Adjectif", "Genre et nombre", "[RULE] L'adjectif s'accorde avec le nom.\n[EXAMPLE] Un petit garçon / Une ____ fille -> [petite]\n[EXAMPLE] Des ____ garçons (petit) -> [petits]", "Grammaire", 1);
        insertLesson(db, "Le Pronom 'On'", "Nous ou quelqu'un", "[RULE] 'On' se conjugue comme 'il'.\n[EXAMPLE] ____ y va ? -> [On]\n[RULE] Signifie souvent 'nous' dans la langue parlée.", "Grammaire", 1);
        insertLesson(db, "Les Pronoms COD", "Le, la, l', les", "[RULE] Remplacent un nom après le verbe.\n[EXAMPLE] Je mange la pomme -> Je ____ mange -> [la]", "Grammaire", 2);
        insertLesson(db, "Les Pronoms COI", "Lui, leur", "[RULE] Remplacent 'à + personne'.\n[EXAMPLE] Je parle à Pierre -> Je ____ parle -> [lui]", "Grammaire", 2);
        insertLesson(db, "Le Pronom 'En'", "Quantité et 'de'", "[RULE] Remplace un nom précédé de 'de' ou une quantité.\n[EXAMPLE] Je veux du café -> J'____ veux -> [en]", "Grammaire", 3);
        insertLesson(db, "Le Pronom 'Y'", "Lieu et 'à'", "[RULE] Remplace un lieu ou une chose précédée de 'à'.\n[EXAMPLE] Je vais à Paris -> J'____ vais -> [y]", "Grammaire", 3);
        insertLesson(db, "La Comparaison", "Plus, moins, aussi", "[RULE] Plus/Moins/Aussi + Adjectif + Que.\n[EXAMPLE] Paul est ____ grand que moi -> [plus]", "Grammaire", 2);
        insertLesson(db, "Le Superlatif", "Le plus, le moins", "[RULE] Article + Plus/Moins + Adjectif.\n[EXAMPLE] C'est le ____ beau film -> [plus]", "Grammaire", 2);
        insertLesson(db, "La Cause", "Parce que, car, puisque", "[RULE] Expliquer pourquoi.\n[EXAMPLE] Je sors ____ il fait beau -> [parce qu']", "Grammaire", 2);
        insertLesson(db, "La Conséquence", "Donc, alors", "[RULE] Exprimer le résultat.\n[EXAMPLE] J'ai faim, ____ je mange -> [donc]", "Grammaire", 2);
        insertLesson(db, "L'Opposition", "Mais, pourtant", "[RULE] Exprimer une contradiction.\n[EXAMPLE] Il est petit ____ fort -> [mais]", "Grammaire", 2);
        insertLesson(db, "Le But", "Pour, afin de", "[RULE] Exprimer l'objectif.\n[EXAMPLE] Je travaille ____ réussir -> [pour]", "Grammaire", 2);
        insertLesson(db, "L'Hypothèse", "Si + Présent", "[RULE] Exprimer une condition réelle.\n[EXAMPLE] Si tu viens, je ____ (être) content -> [serai]", "Grammaire", 3);

        // --- VERBE ---
        insertLesson(db, "Le Présent - 1er groupe", "Verbes en -er", "[RULE] Terminaisons : -e, -es, -e, -ons, -ez, -ent.\n[EXAMPLE] Je ____ (parler) -> [parle]", "Verbe", 1);
        insertLesson(db, "Le Présent - 2ème groupe", "Verbes en -ir", "[RULE] Terminaisons : -is, -is, -it, -issons, -issez, -issent.\n[EXAMPLE] Je ____ (finir) -> [finis]", "Verbe", 2);
        insertLesson(db, "Le Présent - 3ème groupe", "Verbes irréguliers", "[RULE] Être, Avoir, Faire, Aller.\n[EXAMPLE] Nous ____ (être) ici -> [sommes]", "Verbe", 2);
        insertLesson(db, "Le Passé Composé", "Actions terminées", "[RULE] Auxiliaire + Participe Passé.\n[EXAMPLE] J'ai fini mes devoirs.", "Verbe", 2);
        insertLesson(db, "L'Imparfait", "Habitudes", "[RULE] Radical de 'nous' + -ais, -ais, -ait, -ions, -iez, -aient.\n[EXAMPLE] Je ____ (parler) -> [parlais]", "Verbe", 2);
        insertLesson(db, "Le Futur Proche", "Aller + Infinitif", "[RULE] Action imminente.\n[EXAMPLE] Je ____ (aller) manger -> [vais]", "Verbe", 1);
        insertLesson(db, "Le Futur Simple", "Actions à venir", "[RULE] Infinitif + -ai, -as, -a, -ons, -ez, -ont.\n[EXAMPLE] Je ____ (parler) -> [parlerai]", "Verbe", 2);
        insertLesson(db, "Le Conditionnel Présent", "Souhait", "[RULE] Radical du futur + terminaisons imparfait.\n[EXAMPLE] Je ____ (vouloir) un café -> [voudrais]", "Verbe", 3);
        insertLesson(db, "L'Impératif", "Ordres", "[RULE] Pas de sujet.\n[EXAMPLE] (Parler) ! -> [Parle]", "Verbe", 1);
        insertLesson(db, "Le Gérondif", "En + -ant", "[RULE] Simultanéité.\n[EXAMPLE] Il chante ____ ____ (marcher) -> [en marchant]", "Verbe", 2);
        insertLesson(db, "Le Plus-que-parfait", "Antériorité passée", "[RULE] Auxiliaire imparfait + participe passé.\n[EXAMPLE] J'____ (manger) quand il est venu -> [avais mangé]", "Verbe", 3);
        insertLesson(db, "Le Futur Antérieur", "Antériorité future", "[RULE] Auxiliaire futur + participe passé.\n[EXAMPLE] J'____ ____ (finir) demain -> [aurai fini]", "Verbe", 3);
        insertLesson(db, "Le Subjonctif Présent", "Nécessité", "[RULE] 'Il faut que' + subjonctif.\n[EXAMPLE] Il faut que tu ____ (venir) -> [viennes]", "Verbe", 3);
        insertLesson(db, "Le Passé Simple", "Temps écrit", "[RULE] Récit littéraire.\n[EXAMPLE] Il ____ (parler) longtemps -> [parla]", "Verbe", 3);
        insertLesson(db, "Verbes Pronominaux", "Se + Verbe", "[RULE] Action sur soi-même.\n[EXAMPLE] Je ____ (se laver) -> [me lave]", "Verbe", 2);

        // --- VOCABULAIRE ---
        insertLesson(db, "Salutations", "Matin et soir", "[RULE] Bonjour / Bonsoir / Salut.", "Vocabulaire", 1);
        insertLesson(db, "Se Présenter", "Prénom et nom", "[EXAMPLE] Je m'appelle Jean.", "Vocabulaire", 1);
        insertLesson(db, "Les Nombres Ordinaux", "Premier, second...", "[RULE] Indiquent l'ordre.\n[EXAMPLE] Le ____ (1) jour -> [premier]\n[EXAMPLE] La ____ (2) fois -> [deuxième]", "Vocabulaire", 1);
        insertLesson(db, "Les Pays", "Où habitez-vous ?", "[EXAMPLE] J'habite en ____ (France) -> [France]", "Vocabulaire", 1);
        insertLesson(db, "Le Corps Humain", "Tête, bras, jambes", "[EXAMPLE] J'ai mal à la ____ (head) -> [tête]", "Vocabulaire", 1);
        insertLesson(db, "La Maison", "Pièces et meubles", "[EXAMPLE] Je dors dans la ____ à coucher -> [chambre]", "Vocabulaire", 1);
        insertLesson(db, "La Ville", "Directions", "[EXAMPLE] Tournez à ____ (right) -> [droite]", "Vocabulaire", 1);
        insertLesson(db, "Le Travail", "Métiers", "[EXAMPLE] Il est ____ (doctor) -> [médecin]", "Vocabulaire", 2);
        insertLesson(db, "Les Loisirs", "Sport et musique", "[EXAMPLE] Je joue ____ piano -> [du]", "Vocabulaire", 1);
        insertLesson(db, "La Nature", "Climat", "[EXAMPLE] Il y a du ____ (sun) -> [soleil]", "Vocabulaire", 1);

        // --- ORTHOGRAPHE ---
        insertLesson(db, "Les Adverbes en -ment", "Formation", "[RULE] Adjectif féminin + -ment.\n[EXAMPLE] Heureuse -> Heureusement", "Orthographe", 2);
        insertLesson(db, "Accord Participe (Avoir)", "Cas du COD avant", "[RULE] Accord si COD avant.\n[EXAMPLE] La lettre que j'ai ____ (écrire) -> [écrite]", "Orthographe", 3);
        insertLesson(db, "Accord Participe (Pronominaux)", "Cas complexes", "[RULE] Accord avec le sujet si pas de COD après.\n[EXAMPLE] Elle s'est ____ (laver) -> [lavée]", "Orthographe", 3);
        insertLesson(db, "Les Accents", "Aigu, grave, circonflexe", "[RULE] é, è, ê.\n[EXAMPLE] Un ____ (summer) -> [été]", "Orthographe", 1);
        insertLesson(db, "La Ponctuation", "Points et virgules", "[RULE] . , ! ? : ;", "Orthographe", 1);
        // --- DIVERS / ADVANCED (4) ---
        insertLesson(db, "Les Pronoms Relatifs (Qui, Que)", "Remplacer le sujet ou COD", "[RULE] 'Qui' remplace le sujet.\n[EXAMPLE] L'homme ____ parle est mon oncle -> [qui]\n[RULE] 'Que' remplace le complément d'objet direct.\n[EXAMPLE] Le livre ____ je lis est passionnant -> [que]\n[EXCEPTION] Devant une voyelle, 'que' devient 'qu''.\n[EXAMPLE] Le film qu'il a vu était triste.", "Grammaire", 2);
        insertLesson(db, "Dont et Où", "Relatifs de lieu et de complément", "[RULE] 'Dont' remplace un complément introduit par 'de'.\n[EXAMPLE] C'est l'ami ____ je t'ai parlé -> [dont]\n[RULE] 'Où' exprime le lieu ou le temps.\n[EXAMPLE] La ville ____ j'habite est belle -> [où]\n[EXAMPLE] Le jour ____ il est arrivé -> [où]\n[EXCEPTION] Ne pas confondre 'où' (lieu) et 'ou' (choix).", "Grammaire", 2);
        insertLesson(db, "Les Adjectifs Démonstratifs", "Désigner quelqu'un ou quelque chose", "[RULE] Utilisés pour désigner quelque chose ou quelqu'un.\n[RULE] Masculin : Ce. Féminin : Cette. Pluriel : Ces.\n[EXAMPLE] ____ livre est intéressant -> [Ce]\n[EXAMPLE] ____ fille est gentille -> [Cette]\n[RULE] Devant un nom masculin commençant par une voyelle, on utilise 'Cet'.\n[EXAMPLE] ____ homme est grand -> [Cet]\n[EXCEPTION] Ne pas confondre 'ce' (adjectif) et 'se' (pronom).", "Grammaire", 1);
        insertLesson(db, "Les Pronoms Possessifs", "Le mien, le tien...", "[RULE] Remplacent l'adjectif possessif et le nom.\n[EXAMPLE] C'est mon livre. C'est le ____ -> [mien]\n[EXAMPLE] C'est sa voiture. C'est la ____ -> [sienne]\n[RULE] S'accordent avec le nom qu'ils remplacent.\n[EXAMPLE] Ce sont nos amis. Ce sont les ____ -> [nôtres]\n[EXCEPTION] Ils sont toujours précédés de l'article défini (le, la, les).", "Grammaire", 2);
    }

    private void insertLesson(SQLiteDatabase db, String title, String description, String content, String category, int difficulty) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
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
                lesson.setDescription(cursor.getString(2));
                lesson.setContent(cursor.getString(3));
                lesson.setCategory(cursor.getString(4));
                lesson.setDifficulty(cursor.getInt(5));
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
                lesson.setDescription(cursor.getString(2));
                lesson.setContent(cursor.getString(3));
                lesson.setCategory(cursor.getString(4));
                lesson.setDifficulty(cursor.getInt(5));
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
        
        // First check if the record exists to preserve other columns
        Cursor cursor = db.query(TABLE_PROGRESS, null, COLUMN_PROG_LESSON_ID + "=?", 
                new String[]{String.valueOf(lessonId)}, null, null, null);
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROG_STEP_INDEX, stepIndex);
        
        if (cursor != null && cursor.moveToFirst()) {
            // Record exists, update it
            boolean alreadyCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROG_IS_COMPLETED)) == 1;
            values.put(COLUMN_PROG_IS_COMPLETED, (isCompleted || alreadyCompleted) ? 1 : 0);
            db.update(TABLE_PROGRESS, values, COLUMN_PROG_LESSON_ID + "=?", new String[]{String.valueOf(lessonId)});
            cursor.close();
        } else {
            // New record, insert it
            values.put(COLUMN_PROG_LESSON_ID, lessonId);
            values.put(COLUMN_PROG_IS_COMPLETED, isCompleted ? 1 : 0);
            db.insert(TABLE_PROGRESS, null, values);
            if (cursor != null) cursor.close();
        }
    }

    public void setLessonMastered(int lessonId, boolean isMastered) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROG_IS_MASTERED, isMastered ? 1 : 0);
        
        int rows = db.update(TABLE_PROGRESS, values, COLUMN_PROG_LESSON_ID + "=?", new String[]{String.valueOf(lessonId)});
        if (rows == 0) {
            values.put(COLUMN_PROG_LESSON_ID, lessonId);
            db.insert(TABLE_PROGRESS, null, values);
        }
    }

    public void setCardBookmarked(int lessonId, int cardIndex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROG_BOOKMARKED_CARD, cardIndex);
        
        int rows = db.update(TABLE_PROGRESS, values, COLUMN_PROG_LESSON_ID + "=?", new String[]{String.valueOf(lessonId)});
        if (rows == 0) {
            values.put(COLUMN_PROG_LESSON_ID, lessonId);
            db.insert(TABLE_PROGRESS, null, values);
        }
    }

    public void resetLessonProgress(int lessonId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROGRESS, COLUMN_PROG_LESSON_ID + "=?", new String[]{String.valueOf(lessonId)});
    }

    public void saveSetting(String key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SETTING_KEY, key);
        values.put(COLUMN_SETTING_VALUE, value);
        db.insertWithOnConflict(TABLE_SETTINGS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public String getSetting(String key, String defaultValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SETTINGS, new String[]{COLUMN_SETTING_VALUE},
                COLUMN_SETTING_KEY + "=?", new String[]{key}, null, null, null);
        String value = defaultValue;
        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getString(0);
            cursor.close();
        }
        return value;
    }

    public int getCompletionPercentage(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_LESSONS + " WHERE " + COLUMN_CATEGORY + "=?";
        long total = DatabaseUtils.longForQuery(db, query, new String[]{category});
        if (total == 0) return 0;

        String progressQuery = "SELECT COUNT(*) FROM " + TABLE_PROGRESS + " p " +
                "JOIN " + TABLE_LESSONS + " l ON p." + COLUMN_PROG_LESSON_ID + " = l." + COLUMN_ID + " " +
                "WHERE l." + COLUMN_CATEGORY + "=? AND p." + COLUMN_PROG_IS_COMPLETED + "=1";
        long completed = DatabaseUtils.longForQuery(db, progressQuery, new String[]{category});
        
        return (int) ((completed * 100) / total);
    }

    public int getLessonProgressPercentage(int lessonId, String content) {
        if (content == null || content.isEmpty()) return 0;
        
        // Count steps (tags + 1 for finish)
        int steps = 1;
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\[(RULE|EXAMPLE|EXCEPTION)]");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            steps++;
        }
        
        int currentStep = getLessonProgress(lessonId);
        return ((currentStep + 1) * 100) / steps;
    }


    public static class SyncAction {
        public int id;
        public String action;
        public String payload;
        public SyncAction(int id, String action, String payload) {
            this.id = id; this.action = action; this.payload = payload;
        }
    }
    public int getOverallCompletionPercentage() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_LESSONS;
        long total = DatabaseUtils.longForQuery(db, query, null);
        if (total == 0) return 0;

        String progressQuery = "SELECT COUNT(*) FROM " + TABLE_PROGRESS + " WHERE " + COLUMN_PROG_IS_COMPLETED + "=1";
        long completed = DatabaseUtils.longForQuery(db, progressQuery, null);
        
        return (int) ((completed * 100) / total);
    }

    public int getMasteredLessonCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_PROGRESS + " WHERE " + COLUMN_PROG_IS_MASTERED + "=1";
        return (int) DatabaseUtils.longForQuery(db, query, null);
    }

    public int getDueReviewsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long now = System.currentTimeMillis();
        String query = "SELECT COUNT(*) FROM " + TABLE_SRS + " WHERE " + COLUMN_SRS_NEXT_REVIEW + " <= ?";
        return (int) DatabaseUtils.longForQuery(db, query, new String[]{String.valueOf(now)});
    }
}
