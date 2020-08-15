package com.example.mongrammaire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class parametres extends AppCompatActivity {
//todo next version try to remove dependencies from other libraries!!!
    //todo but if you still need the dependency, change BOTH LIBRARY MODULS gradle file
    //todo from "Api" to either "implementation" or "compileOnly"

    private static final int REQUEST_CODE_ACTIVITY_SETTINGS = 1001;

    public static final String EXTRA_SETTINGS_LIST = "EXTRA_SETTINGS_LIST";
    public static final String DEFAULT_VALUE_EDIT_TEXT = "default value";

    private static final String LIST_DIALOG_ONE = "one";
    private static final String LIST_DIALOG_TWO = "two";
    private static final String LIST_DIALOG_THREE = "three";
    private static final String LIST_DIALOG_FOUR = "four";
    private static final String LIST_DIALOG_FIVE = "five";

    //todo remember that these keys must NOT be changed because they are being used as id's!!!
    public static final String SETTINGS_KEY_BASIC = "SETTINGS_KEY_BASIC";
    public static final String SETTINGS_KEY_RINGTONE = "SETTINGS_KEY_RINGTONE";
    public static final String SETTINGS_KEY_CHECKBOX = "SETTINGS_KEY_CHECKBOX";
    public static final String SETTINGS_KEY_SWITCH = "SETTINGS_KEY_SWITCH";
    public static final String SETTINGS_KEY_SEEKBAR = "SETTINGS_KEY_SEEKBAR";
    public static final String SETTINGS_KEY_EDIT_TEXT = "SETTINGS_KEY_EDIT_TEXT";
    public static final String SETTINGS_KEY_LIST_SINGLE_CHOICE = "SETTINGS_KEY_LIST_SINGLE_CHOICE";
    public static final String SETTINGS_KEY_CUSTOM_DIALOG = "SETTINGS_KEY_CUSTOM_DIALOG";
    public static final String SETTINGS_KEY_CUSTOM_LIST_DIALOG = "SETTINGS_KEY_CUSTOM_LIST_DIALOG";
    public static final String SETTINGS_KEY_LIST_MULTI_CHOICE = "SETTINGS_KEY_LIST_MULTI_CHOICE";
    public static final String SETTINGS_KEY_CUSTOM = "SETTINGS_KEY_CUSTOM";

    private ArrayList<SettingsObject> mySettingsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);
    }
}
