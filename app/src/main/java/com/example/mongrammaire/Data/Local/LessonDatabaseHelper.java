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
        super(context, DATABASE_NAME, null, 13); // Incrementing to 13
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
            onCreate(db);
        }
    }

    private void seedLessons(SQLiteDatabase db) {
        // --- GRAMMAIRE (5) ---
        insertLesson(db, "Les Noms", "Introduction aux noms et leurs genres", "[RULE] Un nom désigne une personne, un lieu ou une chose.\nIls ont un genre (masculin/féminin) et un nombre (singulier/pluriel).\n\n[EXAMPLE] Le chat -> [Masculin]\n[EXAMPLE] La ville -> [Féminin]\n[EXCEPTION] Certains noms ont la même forme au masculin et au féminin. Ex: Un/Une enfant.", "Grammaire", 1);
        insertLesson(db, "Les Articles", "Définis, indéfinis et partitifs", "[RULE] Les articles se placent devant le nom.\n[RULE] Les articles définis (le, la, l', les) désignent une chose précise.\n[EXAMPLE] Le livre de Paul -> [Spécifique]\n[RULE] Les articles indéfinis (un, une, des) désignent une chose non précisée.\n[EXAMPLE] Un livre -> [N'importe lequel]", "Grammaire", 1);
        insertLesson(db, "Les Pronoms Sujets", "Je, tu, il, elle...", "[RULE] Ils remplacent le nom pour éviter les répétitions.\n[RULE] Singulier : Je, Tu, Il, Elle, On.\n[RULE] Pluriel : Nous, Vous, Ils, Elles.\n[EXAMPLE] Marie mange -> [Elle mange]\n[EXAMPLE] Paul et moi -> [Nous]", "Grammaire", 1);
        insertLesson(db, "La Phrase Simple", "Sujet + Verbe + Complément", "[RULE] Une phrase commence par une majuscule et finit par un point.\n[RULE] Structure de base : Sujet + Verbe + Complément.\n[EXAMPLE] Le chat (Sujet) dort (Verbe).\n[EXAMPLE] Marie regarde la télé -> [S + V + C]", "Grammaire", 1);
        insertLesson(db, "La Négation", "Ne... pas", "[RULE] Pour transformer une phrase à la forme négative, on utilise 'ne' et 'pas'.\n[RULE] 'ne' se place avant le verbe et 'pas' après.\n[EXAMPLE] Je travaille -> [Je ne travaille pas]\n[EXCEPTION] Devant une voyelle, 'ne' devient 'n''.\n[EXAMPLE] J'aime -> [Je n'aime pas]", "Grammaire", 2);

        // --- VERBE (5) ---
        insertLesson(db, "Le Passé Composé", "Actions terminées", "[RULE] Utilisé pour exprimer une action ponctuelle terminée dans le passé.\n[RULE] Formation : Auxiliaire (Être ou Avoir) au présent + Participe Passé.\n[EXAMPLE] J'ai fini mes devoirs.\n[EXCEPTION] 14 verbes de mouvement utilisent l'auxiliaire ÊTRE.\n[EXAMPLE] Il est allé au cinéma.", "Verbe", 3);
        insertLesson(db, "Le Futur Simple", "Actions à venir", "[RULE] Le futur simple exprime une action qui aura lieu plus tard.\n[RULE] Pour les verbes en -er et -ir, on garde l'infinitif et on ajoute les terminaisons : -ai, -as, -a, -ons, -ez, -ont.\n[EXAMPLE] Je parlerai français.\n[EXAMPLE] Tu finiras ton travail.\n[EXCEPTION] Les verbes irréguliers changent de radical : Être -> ser-, Avoir -> aur-.", "Verbe", 3);
        insertLesson(db, "Le Conditionnel", "Souhait ou hypothèse", "[RULE] Utilisé pour exprimer un souhait, un conseil ou une hypothèse.\n[RULE] Formation : Radical du futur + terminaisons de l'imparfait (-ais, -ais, -ait, -ions, -iez, -aient).\n[EXAMPLE] J'aimerais voyager.\n[EXAMPLE] Si j'avais le temps, je dormirais.\n[EXAMPLE] Pourriez-vous m'aider ? -> [Politesse]", "Verbe", 3);
        insertLesson(db, "L'Impératif", "Donner des ordres", "[RULE] Utilisé pour donner un ordre ou un conseil.\n[RULE] Pas de sujet exprimé (Tu, Nous, Vous).\n[EXAMPLE] (Parler) plus fort ! -> [Parle]\n[EXAMPLE] (Finir) vos devoirs ! -> [Finissez]\n[RULE] Pour les verbes en -er, on enlève le 's' à la 2e personne du singulier.\n[EXCEPTION] Sauf devant 'en' et 'y' : Manges-en ! Vas-y !", "Verbe", 1);
        insertLesson(db, "Le Gérondif", "Simultanéité et manière", "[RULE] Exprime la simultanéité, la cause ou la manière.\n[RULE] Formation : En + participe présent (-ant).\n[EXAMPLE] Il chante ____ (marcher) -> [en marchant]\n[EXAMPLE] J'apprends ____ (écouter) la radio -> [en écoutant]\n[RULE] Le sujet doit être le même pour les deux actions.\n[EXCEPTION] Être -> en étant, Avoir -> en ayant, Savoir -> en sachant.", "Verbe", 2);

        // --- VOCABULAIRE (5) ---
        insertLesson(db, "Salutations", "Bonjour, au revoir", "[RULE] Dire bonjour selon le moment de la journée.\n[EXAMPLE] Matin : Bonjour !\n[EXAMPLE] Soir : Bonsoir !\n[RULE] Entre amis, on peut utiliser des formes plus simples.\n[EXAMPLE] Salut ! -> [Informel]", "Vocabulaire", 1);
        insertLesson(db, "Se Présenter", "Je m'appelle...", "[RULE] Pour donner son prénom ou son nom.\n[EXAMPLE] Je m'appelle Jean.\n[EXAMPLE] Mon nom est Dupont.\n[RULE] Pour demander le nom de quelqu'un.\n[EXAMPLE] Comment t'appelles-tu ?", "Vocabulaire", 1);
        insertLesson(db, "Les Repas", "Petit-déjeuner, déjeuner...", "[RULE] Les quatre moments de consommation en France.\n[EXAMPLE] 8h : Le petit-déjeuner\n[EXAMPLE] 12h : Le déjeuner\n[EXAMPLE] 16h : Le goûter\n[EXAMPLE] 20h : Le dîner", "Vocabulaire", 1);
        insertLesson(db, "Les Animaux", "Chat, chien, vache...", "[RULE] Vocabulaire des animaux domestiques courants.\n[EXAMPLE] Le chat -> [Miaou]\n[EXAMPLE] Le chien -> [Ouaf]\n[EXAMPLE] L'oiseau -> [Cui-cui]\n[RULE] Les animaux de la ferme.\n[EXAMPLE] La vache -> [Meuh]", "Vocabulaire", 1);
        insertLesson(db, "Adjectifs et Pronoms Indéfinis", "Tout, chaque, certains", "[RULE] Désignent une quantité ou une identité vague.\n[RULE] 'Tout' s'accorde : Tout, Toute, Tous, Toutes.\n[EXAMPLE] ____ les enfants sont là -> [Tous]\n[RULE] 'Certains' exprime une partie d'un groupe.\n[EXAMPLE] ____ élèves sont absents -> [Certains]\n[EXCEPTION] 'Chaque' est toujours suivi d'un nom au singulier.", "Vocabulaire", 2);

        // --- ORTHOGRAPHE (5) ---
        insertLesson(db, "Les Adverbes", "Modifier un verbe", "[RULE] Un adverbe modifie un verbe, un adjectif ou un autre adverbe.\n[RULE] Beaucoup d'adverbes se forment à partir de l'adjectif féminin + -ment.\n[EXAMPLE] Heureuse -> Heureusement\n[EXAMPLE] Lente -> Lentement\n[EXCEPTION] Certains sont irréguliers : Bon -> Bien, Mauvais -> Mal.", "Orthographe", 2);
        insertLesson(db, "Les Noms Composés", "Accords complexes", "[RULE] Noms formés de deux mots ou plus.\n[RULE] Pluriel : En général, seuls le nom et l'adjectif prennent la marque du pluriel.\n[EXAMPLE] Un chou-fleur -> des ____ -> [choux-fleurs]\n[EXAMPLE] Un coffre-fort -> des ____ -> [coffres-forts]\n[RULE] Le verbe reste invariable.\n[EXCEPTION] Un gratte-ciel -> des gratte-ciel (Invariable).", "Orthographe", 2);
        insertLesson(db, "La Nominalisation", "Transformer en noms", "[RULE] Transformer une phrase avec un verbe en phrase avec un nom.\n[EXAMPLE] Il part demain -> Son ____ a lieu demain -> [départ]\n[EXAMPLE] On ferme à 18h -> La ____ est à 18h -> [fermeture]\n[RULE] Utile pour les titres de journaux ou les recettes.\n[EXCEPTION] Le nom n'a pas toujours la même racine que le verbe.", "Orthographe", 2);
        insertLesson(db, "L'Accord du Participe Passé (Avoir)", "Cas du COD placé avant", "[RULE] Avec l'auxiliaire 'avoir', le participe passé ne s'accorde pas avec le sujet.\n[EXAMPLE] Elles ont ____ (manger) -> [mangé]\n[RULE] Il s'accorde avec le COD s'il est placé AVANT le verbe.\n[EXAMPLE] La pomme que j'ai ____ (manger) -> [mangée]\n[EXAMPLE] Les fleurs qu'il a ____ (offrir) -> [offertes]\n[EXCEPTION] Pas d'accord avec le pronom 'en'.", "Orthographe", 3);
        insertLesson(db, "L'Accord du Participe Passé (Être)", "Accord avec le sujet", "[RULE] Avec l'auxiliaire 'être', le participe passé s'accorde toujours avec le sujet.\n[EXAMPLE] Elle est ____ (partir) -> [partie]\n[EXAMPLE] Ils sont ____ (arriver) -> [arrivés]\n[RULE] S'accorde en genre et en nombre.\n[EXAMPLE] Les filles sont ____ (aller) au cinéma -> [allées]\n[EXCEPTION] Cas particulier des verbes pronominaux (plus complexe).", "Orthographe", 2);

        // --- NEW 30 RICH LESSONS ---

        // 1. Les Adjectifs Possessifs
        insertLesson(db, "Les Adjectifs Possessifs", "Mon, ton, son...", 
            "[RULE] Ils indiquent à qui appartient l'objet.\n" +
            "[RULE] Masculin : mon, ton, son, notre, votre, leur.\n" +
            "[RULE] Féminin : ma, ta, sa, notre, votre, leur.\n" +
            "[RULE] Pluriel : mes, tes, ses, nos, vos, leurs.\n" +
            "[EXAMPLE] ____ livre (mon) -> [Mon]\n" +
            "[EXAMPLE] ____ mère (ma) -> [Ma]\n" +
            "[EXCEPTION] Devant une voyelle, on utilise 'mon, ton, son' même pour le féminin.\n" +
            "[EXAMPLE] ____ amie (ma) -> [mon]", "Grammaire", 1);

        // 2. Les Adjectifs Démonstratifs
        insertLesson(db, "Les Adjectifs Démonstratifs", "Ce, cette, ces...", 
            "[RULE] Ils servent à montrer quelque chose ou quelqu'un.\n" +
            "[RULE] Masculin : ce.\n" +
            "[RULE] Féminin : cette.\n" +
            "[RULE] Pluriel : ces.\n" +
            "[EXAMPLE] ____ chat (ce) -> [Ce]\n" +
            "[EXAMPLE] ____ fleur (cette) -> [Cette]\n" +
            "[EXAMPLE] ____ enfants (ces) -> [Ces]\n" +
            "[EXCEPTION] Masculin + voyelle/h muet = cet.\n" +
            "[EXAMPLE] ____ homme (ce) -> [cet]", "Grammaire", 1);

        // 3. Le Féminin des Noms
        insertLesson(db, "Le Féminin des Noms", "Règles de formation", 
            "[RULE] En général, on ajoute un 'e' au masculin.\n" +
            "[EXAMPLE] Un ami -> une ____ -> [amie]\n" +
            "[RULE] Noms en -er -> -ère.\n" +
            "[EXAMPLE] Un boulanger -> une ____ -> [boulangère]\n" +
            "[RULE] Noms en -eur -> -euse.\n" +
            "[EXAMPLE] Un chanteur -> une ____ -> [chanteuse]\n" +
            "[RULE] Noms en -teur -> -trice.\n" +
            "[EXAMPLE] Un acteur -> une ____ -> [actrice]\n" +
            "[EXCEPTION] Certains noms sont identiques au masculin et féminin.\n" +
            "[EXAMPLE] Un touriste -> une ____ -> [touriste]", "Grammaire", 2);

        // 4. Le Pluriel des Noms
        insertLesson(db, "Le Pluriel des Noms", "Règles générales", 
            "[RULE] En général, on ajoute un 's'.\n" +
            "[EXAMPLE] Un livre -> des ____ -> [livres]\n" +
            "[RULE] Noms en -al -> -aux.\n" +
            "[EXAMPLE] Un journal -> des ____ -> [journaux]\n" +
            "[RULE] Noms en -au, -eau, -eu -> -x.\n" +
            "[EXAMPLE] Un bateau -> des ____ -> [bateaux]\n" +
            "[RULE] Noms en -s, -x, -z -> Invariables.\n" +
            "[EXAMPLE] Un nez -> des ____ -> [nez]\n" +
            "[EXCEPTION] Les exceptions en -ou (bijou, caillou...).\n" +
            "[EXAMPLE] Un bijou -> des ____ -> [bijoux]", "Grammaire", 2);

        // 5. Les Prépositions de Lieu (1)
        insertLesson(db, "Les Prépositions de Lieu (1)", "Dans, sur, sous...", 
            "[RULE] 'Dans' indique l'intérieur.\n" +
            "[EXAMPLE] Le chat est ____ la boîte -> [dans]\n" +
            "[RULE] 'Sur' indique la position au-dessus avec contact.\n" +
            "[EXAMPLE] Le livre est ____ la table -> [sur]\n" +
            "[RULE] 'Sous' indique la position au-dessous.\n" +
            "[EXAMPLE] Le chien dort ____ le lit -> [sous]\n" +
            "[RULE] 'Devant' et 'Derrière'.\n" +
            "[EXAMPLE] La voiture est ____ la maison -> [devant]\n" +
            "[EXCEPTION] 'Entre' s'utilise pour deux objets ou plus.", "Grammaire", 1);

        // 6. Les Prépositions de Lieu (2)
        insertLesson(db, "Les Prépositions de Lieu (2)", "À, en, au...", 
            "[RULE] 'À' + ville.\n" +
            "[EXAMPLE] J'habite ____ Paris -> [à]\n" +
            "[RULE] 'En' + pays féminin ou voyelle.\n" +
            "[EXAMPLE] Je vais ____ France -> [en]\n" +
            "[RULE] 'Au' + pays masculin.\n" +
            "[EXAMPLE] Il voyage ____ Japon -> [au]\n" +
            "[RULE] 'Aux' + pays pluriel.\n" +
            "[EXAMPLE] Elle vit ____ États-Unis -> [aux]\n" +
            "[EXCEPTION] Certains pays masculins commencent par une voyelle -> 'En Iran'.", "Grammaire", 2);

        // 7. Le Présent - Verbes du 1er groupe
        insertLesson(db, "Le Présent - 1er groupe", "Verbes en -er", 
            "[RULE] Les terminaisons : -e, -es, -e, -ons, -ez, -ent.\n" +
            "[EXAMPLE] Je ____ (parler) -> [parle]\n" +
            "[EXAMPLE] Tu ____ (chanter) -> [chantes]\n" +
            "[EXAMPLE] Il ____ (manger) -> [mange]\n" +
            "[EXAMPLE] Nous ____ (aimer) -> [aimons]\n" +
            "[RULE] Verbes en -ger : on ajoute un 'e' avant 'ons'.\n" +
            "[EXAMPLE] Nous ____ (manger) -> [mangeons]\n" +
            "[EXCEPTION] Le verbe ALLER est irrégulier.", "Verbe", 1);

        // 8. Le Présent - Verbes du 2ème groupe
        insertLesson(db, "Le Présent - 2ème groupe", "Verbes en -ir (-issant)", 
            "[RULE] Les terminaisons : -is, -is, -it, -issons, -issez, -issent.\n" +
            "[EXAMPLE] Je ____ (finir) -> [finis]\n" +
            "[EXAMPLE] Tu ____ (choisir) -> [choisis]\n" +
            "[EXAMPLE] Elle ____ (réussir) -> [réussit]\n" +
            "[EXAMPLE] Nous ____ (finir) -> [finissons]\n" +
            "[EXAMPLE] Vous ____ (grandir) -> [grandissez]\n" +
            "[EXCEPTION] Tous les verbes en -ir ne sont pas du 2ème groupe (ex: Partir).", "Verbe", 2);

        // 9. Le Présent - Verbes du 3ème groupe
        insertLesson(db, "Le Présent - 3ème groupe", "Verbes irréguliers", 
            "[RULE] Verbes en -re, -oir, et -ir.\n" +
            "[RULE] Être : je suis, tu es, il est, nous sommes, vous êtes, ils sont.\n" +
            "[RULE] Avoir : j'ai, tu as, il a, nous avons, vous avez, ils ont.\n" +
            "[RULE] Faire : je fais, tu fais, il fait, nous faisons, vous faites, ils font.\n" +
            "[EXAMPLE] Nous ____ (être) contents -> [sommes]\n" +
            "[EXAMPLE] Ils ____ (avoir) faim -> [ont]\n" +
            "[EXCEPTION] Le radical change souvent.", "Verbe", 2);

        // 10. L'Imparfait
        insertLesson(db, "L'Imparfait", "Habitudes et descriptions", 
            "[RULE] Formation : Radical de 'nous' au présent + -ais, -ais, -ait, -ions, -iez, -aient.\n" +
            "[EXAMPLE] Je ____ (parler) -> [parlais]\n" +
            "[EXAMPLE] Tu ____ (finir) -> [finissais]\n" +
            "[EXAMPLE] Il ____ (être) -> [était]\n" +
            "[RULE] Utilisé pour une action continue ou répétée dans le passé.\n" +
            "[EXAMPLE] Quand j'____ (être) petit, je jouais -> [étais]\n" +
            "[EXCEPTION] Seul le verbe Être a un radical irrégulier (ét-).", "Verbe", 3);

        // 11. Le Passé Composé avec Avoir
        insertLesson(db, "Le Passé Composé (Avoir)", "Actions terminées", 
            "[RULE] Formation : Avoir au présent + Participe Passé.\n" +
            "[RULE] Participe passé -er -> -é.\n" +
            "[EXAMPLE] J'ai ____ (parler) -> [parlé]\n" +
            "[RULE] Participe passé -ir -> -i.\n" +
            "[EXAMPLE] Tu as ____ (fini) -> [fini]\n" +
            "[RULE] Participe passé -re -> -u.\n" +
            "[EXAMPLE] Il a ____ (vendre) -> [vendu]\n" +
            "[EXCEPTION] Beaucoup de participes passés sont irréguliers (fait, pris, dit).", "Verbe", 3);

        // 12. Le Passé Composé avec Être
        insertLesson(db, "Le Passé Composé (Être)", "Verbes de mouvement", 
            "[RULE] Liste des 14 verbes (naître, mourir, aller, venir...).\n" +
            "[RULE] L'accord avec le SUJET est obligatoire.\n" +
            "[EXAMPLE] Elle est ____ (partir) -> [partie]\n" +
            "[EXAMPLE] Ils sont ____ (aller) -> [allés]\n" +
            "[RULE] Les verbes pronominaux utilisent aussi ÊTRE.\n" +
            "[EXAMPLE] Je me suis ____ (lever) -> [levé]\n" +
            "[EXCEPTION] 'Passer' utilise avoir s'il y a un COD.", "Verbe", 3);

        // 13. Le Futur Proche
        insertLesson(db, "Le Futur Proche", "Action imminente", 
            "[RULE] Formation : Aller au présent + Infinitif.\n" +
            "[EXAMPLE] Je ____ (aller) manger -> [vais]\n" +
            "[EXAMPLE] Tu ____ (aller) partir -> [vas]\n" +
            "[EXAMPLE] Nous ____ (aller) voir -> [allons]\n" +
            "[RULE] Exprime une action qui va se passer très bientôt.\n" +
            "[EXAMPLE] Il ____ pleuvoir -> [va]\n" +
            "[EXCEPTION] On n'utilise pas ce temps pour des projets lointains.", "Verbe", 2);

        // 14. Le Futur Simple
        insertLesson(db, "Le Futur Simple", "Projets et prévisions", 
            "[RULE] Infinitif + terminaisons : -ai, -as, -a, -ons, -ez, -ont.\n" +
            "[EXAMPLE] Je ____ (parler) -> [parlerai]\n" +
            "[EXAMPLE] Tu ____ (finir) -> [finiras]\n" +
            "[RULE] Verbes en -re : on enlève le 'e'.\n" +
            "[EXAMPLE] Il ____ (vendre) -> [vendra]\n" +
            "[RULE] Irreguliers : Être (ser-), Avoir (aur-), Aller (ir-).\n" +
            "[EXAMPLE] Nous ____ (être) là -> [serons]\n" +
            "[EXCEPTION] Faire devient fer-.", "Verbe", 3);

        // 15. Le Conditionnel Présent
        insertLesson(db, "Le Conditionnel Présent", "Politesse et souhait", 
            "[RULE] Formation : Radical du futur + terminaisons de l'imparfait.\n" +
            "[EXAMPLE] Je ____ (vouloir) -> [voudrais]\n" +
            "[EXAMPLE] Tu ____ (aimer) -> [aimerais]\n" +
            "[RULE] Exprime un souhait ou une hypothèse.\n" +
            "[EXAMPLE] Si je gagnais, je ____ (partir) -> [partirais]\n" +
            "[RULE] Utilisé pour la politesse.\n" +
            "[EXAMPLE] ____-vous m'aider ? (Pouvoir) -> [Pourriez]\n" +
            "[EXCEPTION] Les mêmes radicaux irréguliers qu'au futur.", "Verbe", 3);

        // 16. L'Impératif Présent
        insertLesson(db, "L'Impératif Présent", "Ordres et conseils", 
            "[RULE] Utilisé pour donner un ordre.\n" +
            "[RULE] Seulement 3 personnes : tu, nous, vous. Pas de sujet.\n" +
            "[EXAMPLE] (Parler) ! -> [Parle]\n" +
            "[EXAMPLE] (Finir) ! -> [Finissez]\n" +
            "[RULE] Verbes en -er : pas de 's' à la fin de 'tu'.\n" +
            "[EXAMPLE] ____ tes légumes ! (Manger) -> [Mange]\n" +
            "[EXCEPTION] Être (sois, soyons, soyez), Avoir (aie, ayons, ayez).", "Verbe", 2);

        // 17. Les Pronoms COD
        insertLesson(db, "Les Pronoms COD", "Le, la, l', les", 
            "[RULE] Ils remplacent un nom pour éviter la répétition.\n" +
            "[RULE] Me, te, le/la, nous, vous, les.\n" +
            "[EXAMPLE] Je regarde le film -> Je ____ regarde -> [le]\n" +
            "[EXAMPLE] Il aime Marie -> Il ____ aime -> [l']\n" +
            "[RULE] Se placent avant le verbe.\n" +
            "[EXAMPLE] Nous ____ voyons (les amis) -> [les]\n" +
            "[EXCEPTION] Impératif affirmatif : place après le verbe (Regarde-le !).", "Grammaire", 2);

        // 18. Les Pronoms COI
        insertLesson(db, "Les Pronoms COI", "Lui, leur", 
            "[RULE] Ils remplacent un nom précédé de 'à'.\n" +
            "[RULE] Me, te, lui, nous, vous, leur.\n" +
            "[EXAMPLE] Je parle à Pierre -> Je ____ parle -> [lui]\n" +
            "[EXAMPLE] Il écrit aux parents -> Il ____ écrit -> [leur]\n" +
            "[RULE] Ne changent pas selon le genre (Lui = masculin ou féminin).\n" +
            "[EXAMPLE] Elle téléphone à sa sœur -> Elle ____ téléphone -> [lui]\n" +
            "[EXCEPTION] Ne pas confondre leur (pronom) et leur(s) (adjectif).", "Grammaire", 3);

        // 19. Le Pronom 'En'
        insertLesson(db, "Le Pronom 'En'", "Quantité et de", 
            "[RULE] Remplace un nom précédé de 'du, de la, de l', des'.\n" +
            "[EXAMPLE] Je veux du pain -> J'____ veux -> [en]\n" +
            "[RULE] Utilisé pour exprimer une quantité.\n" +
            "[EXAMPLE] J'ai trois chats -> J'____ ai trois -> [en]\n" +
            "[RULE] Remplace un lieu précédé de 'de'.\n" +
            "[EXAMPLE] Il vient de Paris -> Il ____ vient -> [en]\n" +
            "[EXCEPTION] Ne pas utiliser 'en' pour les personnes.", "Grammaire", 3);

        // 20. Le Pronom 'Y'
        insertLesson(db, "Le Pronom 'Y'", "Lieu et à", 
            "[RULE] Remplace un lieu précédé de 'à, dans, sur...'.\n" +
            "[EXAMPLE] Je vais à l'école -> J'____ vais -> [y]\n" +
            "[RULE] Remplace un nom précédé de 'à' (choses uniquement).\n" +
            "[EXAMPLE] Je pense à mes vacances -> J'____ pense -> [y]\n" +
            "[EXAMPLE] Il habite en France -> Il ____ habite -> [y]\n" +
            "[EXCEPTION] On ne remplace pas une personne par 'y'.", "Grammaire", 3);

        // 21. La Comparaison
        insertLesson(db, "La Comparaison", "Plus, moins, aussi", 
            "[RULE] Supériorité : plus + adjectif + que.\n" +
            "[EXAMPLE] Paul est ____ grand que Jean -> [plus]\n" +
            "[RULE] Infériorité : moins + adjectif + que.\n" +
            "[EXAMPLE] La pomme est ____ chère que l'orange -> [moins]\n" +
            "[RULE] Égalité : aussi + adjectif + que.\n" +
            "[EXAMPLE] Il est ____ intelligent que toi -> [aussi]\n" +
            "[EXCEPTION] Bon -> Meilleur. Bien -> Mieux.", "Grammaire", 2);

        // 22. Le Superlatif
        insertLesson(db, "Le Superlatif", "Le plus, le moins", 
            "[RULE] Formé avec l'article défini (le, la, les).\n" +
            "[EXAMPLE] C'est le ____ grand bâtiment -> [plus]\n" +
            "[RULE] Superlatif d'infériorité.\n" +
            "[EXAMPLE] C'est la ____ belle robe -> [moins]\n" +
            "[RULE] S'accorde en genre et en nombre.\n" +
            "[EXAMPLE] Ce sont les ____ rapides (plus) -> [plus]\n" +
            "[EXCEPTION] Le meilleur, la meilleure.", "Grammaire", 2);

        // 23. Les Adverbes de Fréquence
        insertLesson(db, "Les Adverbes de Fréquence", "Toujours, jamais...", 
            "[RULE] 'Toujours' (100%).\n" +
            "[EXAMPLE] Je mange ____ à midi -> [toujours]\n" +
            "[RULE] 'Souvent' et 'Parfois'.\n" +
            "[EXAMPLE] Il va ____ au cinéma -> [souvent]\n" +
            "[RULE] 'Rarement'.\n" +
            "[EXAMPLE] Elle sort ____ le soir -> [rarement]\n" +
            "[RULE] 'Jamais' (0%). Utilisé avec 'ne'.\n" +
            "[EXAMPLE] Je ____ bois ____ de café -> [ne jamais]\n" +
            "[EXCEPTION] 'Jamais' se place après le verbe.", "Vocabulaire", 1);

        // 24. Les Nombres (1-100)
        insertLesson(db, "Les Nombres (1-100)", "Apprendre à compter", 
            "[RULE] 1-20 : un, deux, trois... vingt.\n" +
            "[RULE] 20, 30, 40, 50, 60.\n" +
            "[RULE] 70 : soixante-dix.\n" +
            "[EXAMPLE] 71 -> soixante et ____ -> [onze]\n" +
            "[RULE] 80 : quatre-vingts.\n" +
            "[RULE] 90 : quatre-vingt-dix.\n" +
            "[EXAMPLE] 99 -> quatre-vingt-____ -> [dix-neuf]\n" +
            "[EXCEPTION] Vingt prend un 's' seulement s'il est multiplié et finit le nombre.", "Vocabulaire", 1);

        // 25. Les Couleurs
        insertLesson(db, "Les Couleurs", "Adjectifs de couleur", 
            "[RULE] S'accordent en genre et en nombre.\n" +
            "[EXAMPLE] Un sac vert -> une robe ____ -> [verte]\n" +
            "[EXAMPLE] Des stylos bleus -> des fleurs ____ -> [bleues]\n" +
            "[RULE] Les couleurs finissant par 'e' sont invariables au féminin.\n" +
            "[EXAMPLE] Un livre rouge -> une voiture ____ -> [rouge]\n" +
            "[EXCEPTION] Marron et Orange sont toujours invariables.\n" +
            "[EXAMPLE] Des chaussures ____ -> [marron]", "Vocabulaire", 1);

        // 26. Les Vêtements
        insertLesson(db, "Les Vêtements", "La mode au quotidien", 
            "[RULE] Haut : un pull, une chemise, un t-shirt.\n" +
            "[EXAMPLE] En hiver, je porte un ____ -> [pull]\n" +
            "[RULE] Bas : un pantalon, une jupe, un short.\n" +
            "[EXAMPLE] Les filles portent souvent une ____ -> [jupe]\n" +
            "[RULE] Accessoires : des chaussures, un chapeau.\n" +
            "[EXAMPLE] Je mets mes ____ pour marcher -> [chaussures]\n" +
            "[EXCEPTION] 'Des vêtements' est toujours pluriel.", "Vocabulaire", 1);

        // 27. La Famille
        insertLesson(db, "La Famille", "Membres de la famille", 
            "[RULE] Parents : le père, la mère.\n" +
            "[RULE] Enfants : le fils, la fille.\n" +
            "[RULE] Frères et sœurs.\n" +
            "[EXAMPLE] Le fils de ma tante est mon ____ -> [cousin]\n" +
            "[RULE] Grands-parents : le grand-père, la grand-mère.\n" +
            "[EXAMPLE] La mère de mon père est ma ____ -> [grand-mère]\n" +
            "[EXCEPTION] Mon 'oncle' et ma 'tante'.", "Vocabulaire", 1);

        // 28. Le Temps et la Météo
        insertLesson(db, "La Météo", "Quel temps fait-il ?", 
            "[RULE] 'Il fait' + adjectif.\n" +
            "[EXAMPLE] ____ beau aujourd'hui -> [Il fait]\n" +
            "[RULE] 'Il pleut' et 'Il neige'.\n" +
            "[EXAMPLE] Prends ton parapluie, ____ -> [il pleut]\n" +
            "[RULE] 'Il y a' + nom.\n" +
            "[EXAMPLE] ____ du soleil -> [Il y a]\n" +
            "[EXAMPLE] ____ du vent -> [Il y a]\n" +
            "[EXCEPTION] On utilise 'Il' (impersonnel).", "Vocabulaire", 1);

        // 29. L'Heure
        insertLesson(db, "L'Heure", "Quelle heure est-il ?", 
            "[RULE] 'Il est' + nombre + 'heure(s)'.\n" +
            "[EXAMPLE] 8h00 -> Il est huit ____ -> [heures]\n" +
            "[RULE] 12h00 -> midi. 00h00 -> minuit.\n" +
            "[RULE] Les minutes : et quart, et demi, moins le quart.\n" +
            "[EXAMPLE] 10h15 -> dix heures et ____ -> [quart]\n" +
            "[EXAMPLE] 10h30 -> dix heures et ____ -> [demie]\n" +
            "[EXCEPTION] 'Demie' s'accorde au féminin avec heure.", "Vocabulaire", 2);

        // 30. La Négation - Formes complexes
        insertLesson(db, "La Négation Complexe", "Plus, jamais, rien...", 
            "[RULE] Ne... plus (arrêt d'une action).\n" +
            "[EXAMPLE] Je ____ fume ____ (ne/plus) -> [ne plus]\n" +
            "[RULE] Ne... jamais.\n" +
            "[EXAMPLE] Il ____ ment ____ -> [ne jamais]\n" +
            "[RULE] Ne... rien (objet).\n" +
            "[EXAMPLE] Je ____ vois ____ -> [ne rien]\n" +
            "[RULE] Ne... personne (personne).\n" +
            "[EXAMPLE] Je ____ connais ____ -> [ne personne]\n" +
            "[EXCEPTION] À l'oral, on oublie souvent le 'ne'.", "Grammaire", 3);

        // --- CONJUGAISON (5) ---
        insertLesson(db, "Le Subjonctif Présent (1)", "Souhait, doute, émotion", "[RULE] Exprime le doute, le souhait, l'émotion ou la nécessité.\n[RULE] Formation : Radical de 'ils' au présent + -e, -es, -e, -ions, -iez, -ent.\n[EXAMPLE] Il faut que je ____ (partir) -> [parte]\n[EXAMPLE] Je veux que tu ____ (venir) -> [viennes]\n[RULE] Toujours précédé de la conjonction 'que'.\n[EXCEPTION] Certains verbes sont irréguliers (être, avoir, aller, faire).", "Conjugaison", 3);
        insertLesson(db, "Le Subjonctif Présent (2)", "Verbes irréguliers au subjonctif", "[RULE] Verbes irréguliers : Être (sois, soit...), Avoir (aie, ait...).\n[EXAMPLE] Je souhaite que tu ____ heureux -> [sois]\n[EXAMPLE] Il faut qu'il ____ du courage -> [ait]\n[RULE] Verbe Aller : aille, ailles, aille, allions, alliez, aillent.\n[EXAMPLE] Il faut que nous ____ au marché -> [allions]\n[EXCEPTION] 'Faire' devient 'fasse'.", "Conjugaison", 3);
        insertLesson(db, "Le Plus-que-parfait", "L'antériorité dans le passé", "[RULE] Exprime une action passée avant une autre action passée.\n[RULE] Formation : Auxiliaire (imparfait) + Participe Passé.\n[EXAMPLE] J'____ (manger) quand il est arrivé -> [avais mangé]\n[EXAMPLE] Elle ____ (partir) avant mon retour -> [était partie]\n[RULE] On utilise l'imparfait de l'auxiliaire.\n[EXCEPTION] Les règles d'accord sont les mêmes qu'au passé composé.", "Conjugaison", 3);
        insertLesson(db, "Le Futur Antérieur", "L'antériorité dans le futur", "[RULE] Exprime une action qui sera terminée avant une autre action future.\n[RULE] Formation : Auxiliaire (futur simple) + Participe Passé.\n[EXAMPLE] Quand j'____ (finir), je sortirai -> [aurai fini]\n[EXAMPLE] Elle ____ (arriver) avant nous -> [sera arrivée]\n[RULE] S'utilise souvent avec 'quand' ou 'dès que'.\n[EXCEPTION] Attention aux verbes pronominaux avec l'auxiliaire être.", "Conjugaison", 3);
        insertLesson(db, "Le Passé Simple (1)", "Temps du récit", "[RULE] Temps du récit écrit, remplace le passé composé.\n[RULE] Verbes en -er : -ai, -as, -a, -âmes, -âtes, -èrent.\n[EXAMPLE] Il ____ (parler) longtemps -> [parla]\n[EXAMPLE] Elles ____ (marcher) vers la mer -> [marchèrent]\n[RULE] S'utilise pour des actions brèves et terminées.\n[EXCEPTION] Très rare à l'oral aujourd'hui.", "Conjugaison", 3);

        // --- CULTURE (4) ---
        insertLesson(db, "La Gastronomie", "Spécialités françaises", "[RULE] La cuisine française est célèbre pour ses fromages et son pain. [EXAMPLE] Pain long et croustillant -> [la baguette] [EXAMPLE] Dessert aux pommes -> [la tarte Tatin] [EXCEPTION] Chaque région a sa spécialité.", "Culture", 1);
        insertLesson(db, "Les Fêtes Françaises", "Traditions et calendrier", "[RULE] Le 14 juillet est la fête nationale française.\n[EXAMPLE] On célèbre la prise de la ____ -> [Bastille]\n[RULE] Noël et Pâques sont des fêtes importantes.\n[EXAMPLE] En mai, il y a beaucoup de jours ____ -> [fériés]", "Culture", 1);
        insertLesson(db, "Géographie de la France", "Régions et paysages", "[RULE] La France est divisée en plusieurs régions administratives.\n[EXAMPLE] La capitale est ____ -> [Paris]\n[RULE] Les montagnes principales sont les Alpes et les Pyrénées.\n[EXAMPLE] Le plus haut sommet est le Mont ____ -> [Blanc]", "Culture", 1);
        insertLesson(db, "Molière", "Le maître de la comédie", "[RULE] Molière est l'auteur français le plus célèbre au monde. [EXAMPLE] L'Avare, Le Bourgeois Gentilhomme... -> [Molière] [EXAMPLE] Quel est son vrai nom ? -> [Jean-Baptiste Poquelin] [EXCEPTION] Il est mort sur scène.", "Culture", 1);

        // --- EXPRESSIONS (5) ---
        insertLesson(db, "L'Interrogation", "Poser des questions", "[RULE] Il y a trois niveaux de langue pour poser une question.\n[EXAMPLE] Langue familière (intonation) : Tu viens ?\n[EXAMPLE] Langue courante (Est-ce que) : Est-ce que tu viens ?\n[EXAMPLE] Langue soutenue (inversion) : Viens-tu ?", "Expressions", 2);
        insertLesson(db, "Expressions de Temps", "Depuis, pendant, il y a", "[RULE] 'Depuis' indique une action qui continue dans le présent.\n[EXAMPLE] J'habite ici ____ 3 ans -> [depuis]\n[RULE] 'Pendant' indique une durée limitée dans le passé ou futur.\n[EXAMPLE] J'ai dormi ____ 8 heures -> [pendant]\n[EXAMPLE] 'Il y a' indique un moment précis du passé.\n[EXCEPTION] 'En' indique le temps nécessaire pour faire une action.", "Expressions", 2);
        insertLesson(db, "Les Connecteurs Logiques", "Lier les idées", "[RULE] Servent à lier les idées dans un texte.\n[RULE] Cause : parce que, car, puisque.\n[EXAMPLE] Je ne sors pas ____ il pleut -> [parce qu']\n[RULE] Conséquence : donc, alors, par conséquent.\n[EXAMPLE] Il a faim, ____ il mange -> [donc]\n[EXCEPTION] 'Mais' exprime l'opposition ou la restriction.", "Expressions", 2);
        insertLesson(db, "Mise en Relief (C'est... qui/que)", "Insister on an élément", "[RULE] Permet d'insister sur un élément de la phrase.\n[RULE] C'est [Sujet] qui [Verbe].\n[EXAMPLE] Paul a gagné -> C'est Paul ____ a gagné -> [qui]\n[RULE] C'est [Objet] que [Sujet] [Verbe].\n[EXAMPLE] J'aime ce livre -> C'est ce livre ____ j'aime -> [que]\n[EXCEPTION] Utiliser 'Ce sont' pour le pluriel (Soutenu).", "Expressions", 1);
        insertLesson(db, "Expressions Idiomatiques", "Sens figurés", "[RULE] Les expressions idiomatiques ne se traduisent pas littéralement. [EXAMPLE] Être une poule mouillée -> [Être peureux] [EXAMPLE] Poser un lapin -> [Ne pas venir au rendez-vous] [EXCEPTION] Leur sens est figuré.", "Expressions", 2);

        // --- PHONÉTIQUE (4) ---
        insertLesson(db, "Les Sons du Français", "Les voyelles nasales", "[RULE] Le français possède des sons nasaux uniques (an, in, on, un). [EXAMPLE] Un ____ (petit animal) -> [lapin] [EXAMPLE] Le ____ (chiffre 1) -> [un] [EXCEPTION] On ne prononce pas le 'n' à la fin.", "Phonétique", 2);
        insertLesson(db, "L'Alphabet Phonétique", "Prononciation correcte", "[RULE] Chaque son correspond à un symbole spécifique.\n[EXAMPLE] Le son [u] correspond au 'ou' de ____ -> [rouge]\n[RULE] Les voyelles peuvent être ouvertes ou fermées.\n[EXAMPLE] Le 'é' est une voyelle ____ -> [fermée]", "Phonétique", 2);
        insertLesson(db, "La Liaison", "Lier les mots", "[RULE] On prononce la consonne finale muette devant une voyelle.\n[EXAMPLE] Les amis se prononce [le-z-ami] -> [Liaison]\n[RULE] Certaines liaisons sont obligatoires, d'autres interdites.\n[EXAMPLE] Un ____ homme -> [bel]", "Phonétique", 2);
        insertLesson(db, "L'Intonation", "Mélodie de la phrase", "[RULE] La voix monte à la fin d'une question.\n[EXAMPLE] Tu viens ? (Voix qui ____) -> [monte]\n[RULE] La voix descend à la fin d'une affirmation.\n[EXAMPLE] Il fait beau. (Voix qui ____) -> [descend]", "Phonétique", 2);

        // --- LITTÉRATURE (4) ---
        insertLesson(db, "Victor Hugo", "Le géant du Romantisme", "[RULE] Victor Hugo est l'auteur des Misérables et de Notre-Dame de Paris.\n[EXAMPLE] Il a écrit des poèmes dans Les ____ -> [Contemplations]\n[RULE] Il était aussi un homme politique engagé.\n[EXAMPLE] Il s'est opposé à Napoléon ____ -> [III]", "Littérature", 3);
        insertLesson(db, "Le Classicisme", "Ordre et raison", "[RULE] Mouvement du XVIIe siècle prônant la clarté.\n[EXAMPLE] Auteur de tragédies : Jean ____ -> [Racine]\n[RULE] On cherche à imiter les Anciens.\n[EXAMPLE] La règle des trois ____ au théâtre -> [unités]", "Littérature", 3);
        insertLesson(db, "Le Surréalisme", "Rêve et inconscient", "[RULE] Mouvement du XXe siècle libérant l'imagination.\n[EXAMPLE] Chef de file : André ____ -> [Breton]\n[RULE] On utilise l'écriture automatique.\n[EXAMPLE] Peintre célèbre lié au mouvement : Salvador ____ -> [Dalí]", "Littérature", 3);
        insertLesson(db, "La Poésie Française", "Vers et rimes", "[RULE] Un vers de 12 syllabes s'appelle un alexandrin.\n[EXAMPLE] 'Demain, dès l'aube...' est un vers de ____ -> [Victor Hugo]\n[RULE] Les rimes peuvent être suivies, croisées ou embrassées.\n[EXAMPLE] AABB sont des rimes ____ -> [suivies]", "Littérature", 3);

        // --- QUOTIDIEN (4) ---
        insertLesson(db, "Au Restaurant", "Commander un repas", "[RULE] Phrases utiles pour commander au restaurant. [EXAMPLE] Je ____ (vouloir) une carafe d'eau -> [voudrais] [EXAMPLE] ____, s'il vous plaît ! (pour payer) -> [L'addition] [EXCEPTION] Le pourboire n'est pas obligatoire.", "Quotidien", 2);
        insertLesson(db, "Faire les Courses", "Au marché et au magasin", "[RULE] Vocabulaire des aliments et des quantités.\n[EXAMPLE] Je voudrais un ____ de pommes -> [kilo]\n[RULE] Demander le prix.\n[EXAMPLE] ____ ça coûte ? -> [Combien]", "Quotidien", 1);
        insertLesson(db, "Chez le Médecin", "Exprimer une douleur", "[RULE] Dire où on a mal.\n[EXAMPLE] J'ai mal ____ la tête -> [à]\n[RULE] Décrire les symptômes.\n[EXAMPLE] J'ai de la ____ (température élevée) -> [fièvre]", "Quotidien", 2);
        insertLesson(db, "Les Transports", "Prendre le train ou le bus", "[RULE] Vocabulaire de la gare et des billets.\n[EXAMPLE] Un aller-____ pour Paris -> [retour]\n[RULE] Demander l'heure de départ.\n[EXAMPLE] À quelle ____ part le train ? -> [heure]", "Quotidien", 1);

        // --- TCF/DELF (4) ---
        insertLesson(db, "Compréhension Orale", "Stratégies pour l'examen", "[RULE] Bien écouter les mots-clés et le ton de l'interlocuteur. [EXAMPLE] Question de type : Quel est l'objectif de l'appel ? -> [Informer] [EXCEPTION] Attention aux distracteurs dans les choix.", "TCF/DELF", 3);
        insertLesson(db, "Compréhension Écrite", "Analyser des documents", "[RULE] Identifier la nature du document (mail, article, annonce).\n[EXAMPLE] Un texte qui vend un objet est une ____ -> [annonce]\n[RULE] Repérer les informations essentielles (qui, quoi, où, quand).", "TCF/DELF", 3);
        insertLesson(db, "Expression Écrite", "Rédiger un message", "[RULE] Respecter la consigne et le nombre de mots.\n[EXAMPLE] Formule de politesse : Bien ____ -> [cordialement]\n[RULE] Organiser ses idées avec des connecteurs.", "TCF/DELF", 3);
        insertLesson(db, "La Syntaxe à l'Examen", "Éviter les erreurs communes", "[RULE] Attention à l'ordre des mots dans la phrase.\n[EXAMPLE] Place de l'adverbe : Il parle ____ (bien) français -> [bien]\n[RULE] Accord des adjectifs et des verbes.", "TCF/DELF", 3);

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
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROG_LESSON_ID, lessonId);
        values.put(COLUMN_PROG_STEP_INDEX, stepIndex);
        values.put(COLUMN_PROG_IS_COMPLETED, isCompleted ? 1 : 0);
        
        db.insertWithOnConflict(TABLE_PROGRESS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void setLessonMastered(int lessonId, boolean isMastered) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROG_LESSON_ID, lessonId);
        values.put(COLUMN_PROG_IS_MASTERED, isMastered ? 1 : 0);
        db.insertWithOnConflict(TABLE_PROGRESS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void setCardBookmarked(int lessonId, int cardIndex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROG_LESSON_ID, lessonId);
        values.put(COLUMN_PROG_BOOKMARKED_CARD, cardIndex);
        db.insertWithOnConflict(TABLE_PROGRESS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
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
}
