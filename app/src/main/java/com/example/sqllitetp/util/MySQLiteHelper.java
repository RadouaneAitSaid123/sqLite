package com.example.sqllitetp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "etudiantDB";
    private static final int DATABASE_VERSION = 2   ;
    private static final String CREATE_TABLE_ETUDIANT = "CREATE TABLE etudiant (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT, prenom TEXT, image TEXT, dateNaissance TEXT)";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ETUDIANT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE etudiant ADD COLUMN image TEXT");
            db.execSQL("ALTER TABLE etudiant ADD COLUMN dateNaissance TEXT");
        }

    }
}
