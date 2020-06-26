package com.pandalab.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CarroSQLiteHelper extends SQLiteOpenHelper {
    private static final String CREAR_TABLA = "CREATE TABLE Carro(id INTEGER PRIMARY KEY, serie INTEGER, nombre TEXT, color TEXT)";
    private static final String DROPEAR_TABLA = "DROP TABLE IF EXISTS Carro;";

    CarroSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version ) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROPEAR_TABLA);
        db.execSQL(CREAR_TABLA);
    }
}
