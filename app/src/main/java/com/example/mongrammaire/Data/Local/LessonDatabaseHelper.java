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
    private static final int DATABASE_VERSION = 5;

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
        
        seedLessons(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SRS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNC);
            onCreate(db);
        }
    }

    private void seedLessons(SQLiteDatabase db) {
        insertLesson(db, "Les Noms", "Un nom désigne une personne, un lieu ou une chose. Ils ont un genre (masculin/féminin) et un nombre (singulier/pluriel). Ex: Le chat, une ville.", "basics", 1);
        insertLesson(db, "Les Articles", "Les articles définis (le, la, les) désignent une chose précise. Les indéfinis (un, une, des) désignent une chose non précisée.", "basics", 1);
        insertLesson(db, "Les Pronoms Sujets", "Ils remplacent le nom pour éviter les répétitions : Je, Tu, Il/Elle, Nous, Vous, Ils/Elles. Ex: 'Il mange' au lieu de 'Le garçon mange'.", "basics", 1);
        insertLesson(db, "La Phrase Simple", "Une phrase se construit généralement ainsi : Sujet + Verbe + Complément. Ex: 'Marie regarde un film.'", "phrases", 1);
        insertLesson(db, "La Négation", "Pour nier une action, on utilise 'ne' avant le verbe et 'pas' après. Ex: 'Je ne travaille pas.'", "phrases", 2);
        insertLesson(db, "L'Interrogation", "Il existe trois façons : l'intonation (Tu viens ?), 'Est-ce que' (Est-ce que tu viens ?), ou l'inversion (Viens-tu ?).", "phrases", 2);
        insertLesson(db, "Salutations de base", "Le matin, on dit 'Bonjour'. Le soir, 'Bonsoir'. 'Salut' est utilisé de manière informelle entre amis.", "greeting", 1);
        insertLesson(db, "Se Présenter", "Pour dire son nom : 'Je m'appelle...'. Pour demander l'identité : 'Comment t'appelles-tu ?'. Enchanté de vous rencontrer !", "greeting", 1);
        insertLesson(db, "Dire Au Revoir", "La formule classique est 'Au revoir'. On peut aussi dire 'À bientôt', 'À demain' ou 'Bonne journée'.", "greeting", 1);
        insertLesson(db, "Fruits et Légumes", "Les fruits : pomme, banane, orange. Les légumes : carotte, courgette, haricot. Indispensables pour une bonne santé !", "food", 1);
        insertLesson(db, "Boissons", "L'eau est la boisson essentielle. On boit aussi du lait, du jus de fruit, du café ou du thé.", "food", 1);
        insertLesson(db, "Les Repas", "Il y a quatre moments : le petit-déjeuner (matin), le déjeuner (midi), le goûter (après-midi) et le dîner (soir).", "food", 1);
        insertLesson(db, "Animaux Domestiques", "Le chien et le chat sont les plus populaires. On trouve aussi le lapin, le hamster et les poissons.", "animal", 1);
        insertLesson(db, "Animaux de la Ferme", "La vache produit du lait, la poule pond des oeufs. On y trouve aussi le cochon, le canard et le mouton.", "animal", 2);
        insertLesson(db, "Animaux Sauvages", "Le lion est le roi de la savane. L'éléphant est le plus gros animal terrestre. Le singe vit dans les arbres.", "animal", 2);
        insertLesson(db, "Vêtements de base", "Le t-shirt, le pantalon (un jean), le pull, la chemise. On porte des chaussettes et des chaussures aux pieds.", "clothing", 1);
        insertLesson(db, "Vêtements d'Hiver", "Quand il fait froid, on porte un manteau, une écharpe, un bonnet et des gants pour rester au chaud.", "clothing", 1);
        insertLesson(db, "Les Accessoires", "La ceinture, la montre, les lunettes de soleil, le chapeau ou la casquette. Ils complètent votre tenue.", "clothing", 2);
        insertLesson(db, "Le Passé Composé", "Utilisé pour des actions terminées. Se construit avec l'auxiliaire être ou avoir + le participe passé. Ex: 'J'ai mangé'.", "advanced", 3);
        insertLesson(db, "Le Futur Simple", "Indique une action à venir. Ex: 'Je mangerai'. On ajoute les terminaisons -ai, -as, -a, -ons, -ez, -ont à l'infinitif.", "advanced", 3);
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

    public static class SyncAction {
        public int id;
        public String action;
        public String payload;
        public SyncAction(int id, String action, String payload) {
            this.id = id; this.action = action; this.payload = payload;
        }
    }
}
