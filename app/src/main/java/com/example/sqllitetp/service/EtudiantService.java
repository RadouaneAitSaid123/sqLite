package com.example.sqllitetp.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sqllitetp.entities.Etudiant;
import com.example.sqllitetp.util.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class EtudiantService {

    private static final String TABLE_NAME = "etudiant";
    private static final String COL_ID = "id";
    private static final String COL_NOM = "nom";
    private static final String COL_IMAGE = "image";
    private static final String COL_DATE_NAISSANCE = "dateNaissance";
    private static final String COL_PRENOM = "prenom";
    private static String[] COLUMNS = {COL_ID, COL_NOM, COL_PRENOM, COL_IMAGE, COL_DATE_NAISSANCE};
    private MySQLiteHelper mySQLiteHelper;

    public EtudiantService(Context context) {
        this.mySQLiteHelper = new MySQLiteHelper(context);
    }

    public void create(Etudiant etudiant) {
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOM, etudiant.getNom());
        values.put(COL_PRENOM, etudiant.getPrenom());
        values.put(COL_IMAGE, etudiant.getImage());
        values.put(COL_DATE_NAISSANCE, etudiant.getDateNaissance());
        db.insert(TABLE_NAME, null, values);
        Log.d("insert", etudiant.getNom());
        db.close();
    }

    public void update(Etudiant etudiant) {
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOM, etudiant.getNom());
        values.put(COL_PRENOM, etudiant.getPrenom());
        values.put(COL_IMAGE, etudiant.getImage());
        values.put(COL_DATE_NAISSANCE, etudiant.getDateNaissance());
        db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(etudiant.getId())});
        db.close();
    }

    public void delete(Etudiant etudiant) {
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(etudiant.getId())});
        db.close();
    }

    public Etudiant findById(int id) {
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
        Etudiant etudiant = null;
        Cursor cursor;
        cursor = db.query(TABLE_NAME, COLUMNS, COL_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            etudiant = new Etudiant();
            etudiant.setId(cursor.getInt(0));
            etudiant.setNom(cursor.getString(1));
            etudiant.setPrenom(cursor.getString(2));
            etudiant.setImage(cursor.getString(3));
            etudiant.setDateNaissance(cursor.getString(4));
        }
        db.close();
        return etudiant;
    }

    public List<Etudiant> findAll() {
        List<Etudiant> etudiants = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Etudiant etudiant = null;
        if (cursor.moveToFirst()) {
            do {
                etudiant = new Etudiant();
                etudiant.setId(cursor.getInt(0));
                etudiant.setNom(cursor.getString(1));
                etudiant.setPrenom(cursor.getString(2));
                etudiant.setImage(cursor.getString(3));
                etudiant.setDateNaissance(cursor.getString(4));
                etudiants.add(etudiant);
            } while (cursor.moveToNext());
        }
        return etudiants;
    }

}
