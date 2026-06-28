package com.example.mongrammaire.Quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TriviaQuizHelper extends SQLiteOpenHelper {

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

    public TriviaQuizHelper(Context context) {
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

        // Original Questions
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

        // New TCF Simulation Questions (A1-C2)
        arraylist.add(new TriviaQuestion("Niveau A1 : Quel jour vient après le mardi ?", "Lundi", "Mercredi", "Jeudi", "Vendredi", "Mercredi"));
        arraylist.add(new TriviaQuestion("Niveau A1 : 'Je ____ un café, s'il vous plaît.'", "veux", "voudrais", "voulais", "voulu", "voudrais"));
        arraylist.add(new TriviaQuestion("Niveau A2 : Il ____ froid hier soir.", "a fait", "fait", "faisait", "fera", "a fait"));
        arraylist.add(new TriviaQuestion("Niveau A2 : Où est mon sac ? Je ne ____ trouve pas.", "le", "la", "lui", "en", "le"));
        arraylist.add(new TriviaQuestion("Niveau B1 : Bien qu'il ____ tard, il continue de travailler.", "est", "soit", "sera", "était", "soit"));
        arraylist.add(new TriviaQuestion("Niveau B1 : Si j'avais de l'argent, je ____ une voiture.", "achète", "achèterai", "achèterais", "avais acheté", "achèterais"));
        arraylist.add(new TriviaQuestion("Niveau B2 : Il est impératif que vous ____ cette décision.", "prenez", "preniez", "prendrez", "prendrez", "preniez"));
        arraylist.add(new TriviaQuestion("Niveau B2 : C'est le problème ____ tout le monde discute.", "que", "dont", "lequel", "où", "dont"));
        arraylist.add(new TriviaQuestion("Niveau C1 : Sa réussite est due à son travail, ____ acharné.", "quoique", "sinon", "voire", "même", "voire"));
        arraylist.add(new TriviaQuestion("Niveau C1 : ____ il soit intelligent, il a échoué.", "Malgré", "Quoiqu'", "Cependant", "Pourtant", "Quoiqu'"));
        arraylist.add(new TriviaQuestion("Niveau C2 : Il a agi ____ de cause.", "en toute connaissance", "à cause", "parce que", "en raison", "en toute connaissance"));
        arraylist.add(new TriviaQuestion("Niveau C2 : Quel est le synonyme de 'procrastiner' ?", "se hâter", "différer", "travailler", "avancer", "différer"));

        arraylist.add(new TriviaQuestion("Structure : 'Je voudrais savoir si vous ____ demain.'", "venez", "viendrez", "êtes venu", "veniez", "viendrez"));
        arraylist.add(new TriviaQuestion("Grammaire : 'Les personnes ____ j'ai rencontrées sont sympathiques.'", "que", "qui", "dont", "auxquelles", "que"));
        arraylist.add(new TriviaQuestion("Vocabulaire : Un synonyme de 'bruyant' est ____.", "calme", "sonore", "silencieux", "tranquille", "sonore"));
        arraylist.add(new TriviaQuestion("Orthographe : 'Il est ____ de partir maintenant.'", "temps", "tan", "tant", "tem", "temps"));
        arraylist.add(new TriviaQuestion("TCF : Quel est l'antonyme de 'éphémère' ?", "court", "durable", "rapide", "bref", "durable"));
        arraylist.add(new TriviaQuestion("TCF : 'Il s'en est fallu de peu ____ il ne tombe.'", "qu'", "pour qu'", "qu'à", "afin qu'", "qu'"));
        arraylist.add(new TriviaQuestion("TCF : 'Je ne pense pas qu'il ____ raison.'", "a", "as", "ait", "aura", "ait"));
        arraylist.add(new TriviaQuestion("TCF : 'Pourriez-vous me dire ____ se trouve la gare ?'", "que", "qui", "où", "quand", "où"));

        arraylist.add(new TriviaQuestion("Niveau B1 : J'ai acheté ce livre ____ l'offrir à mon frère.", "pour", "par", "avec", "dans", "pour"));
        arraylist.add(new TriviaQuestion("Niveau B2 : Il ne cesse de se plaindre ____ son travail.", "de", "sur", "pour", "avec", "de"));
        arraylist.add(new TriviaQuestion("Niveau C1 : On ne peut ____ nier l'évidence.", "guère", "pas", "plus", "jamais", "guère"));
        arraylist.add(new TriviaQuestion("Niveau A2 : Ma sœur est plus ____ que moi.", "grand", "grande", "grands", "grandes", "grande"));
        arraylist.add(new TriviaQuestion("Niveau B1 : Il faut que nous ____ la vérité.", "sachons", "savons", "saurons", "sachent", "sachons"));
        arraylist.add(new TriviaQuestion("Niveau B2 : C'est la raison ____ il est parti.", "pour laquelle", "dont", "laquelle", "que", "pour laquelle"));
        arraylist.add(new TriviaQuestion("Niveau C1 : ____ il pleuve, la fête aura lieu.", "En dépit qu'", "Bien qu'", "Malgré", "Quoique", "Bien qu'"));
        arraylist.add(new TriviaQuestion("Niveau A1 : Comment ____-tu ?", "vas", "va", "allez", "vont", "vas"));
        arraylist.add(new TriviaQuestion("Niveau A2 : Nous ____ (finir) notre travail.", "finissons", "finis", "finissez", "finit", "finissons"));
        arraylist.add(new TriviaQuestion("Niveau B1 : Je ne crois pas qu'il ____ (venir) ce soir.", "vient", "vienne", "viendra", "venait", "vienne"));

        arraylist.add(new TriviaQuestion("TCF : 'Il a été ____ surpris par la nouvelle.'", "fort", "fortement", "très", "beaucoup", "fort"));
        arraylist.add(new TriviaQuestion("TCF : '____ que vous fassiez, je vous soutiendrai.'", "Quoi", "Quoi que", "Quoique", "Quel que", "Quoi que"));
        arraylist.add(new TriviaQuestion("TCF : 'Il a échoué ____ ses efforts.'", "malgré", "en dépit de", "pourtant", "cependant", "malgré"));
        arraylist.add(new TriviaQuestion("TCF : 'C'est un travail de longue ____.'", "haleine", "durée", "temps", "vie", "haleine"));
        arraylist.add(new TriviaQuestion("TCF : 'Il est indispensable ____ vous soyez présent.'", "que", "de", "pour que", "afin que", "que"));
        arraylist.add(new TriviaQuestion("TCF : 'Elle a agi en ____ de cause.'", "connaissance", "raison", "vertu", "dépit", "connaissance"));
        arraylist.add(new TriviaQuestion("TCF : 'Quel est le participe présent du verbe savoir ?'", "sachant", "savant", "savoirant", "su", "sachant"));
        arraylist.add(new TriviaQuestion("TCF : 'Il s'est ____ (souvenir) de son enfance.'", "souvenu", "souvenue", "souvenus", "souvenues", "souvenu"));
        arraylist.add(new TriviaQuestion("TCF : 'Je ____ (lire) ce livre le mois prochain.'", "lirai", "lirais", "lisais", "lus", "lirai"));
        arraylist.add(new TriviaQuestion("TCF : 'Si j'____ (avoir) le temps, je viendrais.'", "avais", "ai", "aurai", "aurais", "avais"));

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

    public List<TriviaQuestion> getAllOfTheQuestions() {
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
