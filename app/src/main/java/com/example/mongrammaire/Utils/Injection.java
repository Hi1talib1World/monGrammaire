package com.example.mongrammaire.Utils;


import com.example.mongrammaire.Data.Local.QuestionDataSource;
import com.example.mongrammaire.Data.Remote.FirebaseDatabaseHelper;
import com.example.mongrammaire.Data.Repository;

public class Injection {

    public static Repository provideRepository() {

        return Repository.getInstance(
                QuestionDataSource.getInstance(),
                FirebaseDatabaseHelper.getHelperInstance());
    }

    public static FirebaseAuthHelper providesAuthHelper() {

        return FirebaseAuthHelper.getClassInstance();
    }
}
