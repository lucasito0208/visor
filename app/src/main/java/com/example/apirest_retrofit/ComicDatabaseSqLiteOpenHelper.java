package com.example.apirest_retrofit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.apirest_retrofit.models.Comic;

import java.util.ArrayList;
import java.util.Date;

public class ComicDatabaseSqLiteOpenHelper extends SQLiteOpenHelper {

    public ComicDatabaseSqLiteOpenHelper com;
    ArrayList<Comic> listComic;
    SQLiteDatabase db;
    Comic comic;
    Cursor cursor;
    Context context;

    public static final String DB = "comic_db";
    public static final int VERSION = 1;
    public static final String TABLE_COMIC = "comic";



    String sqlCreate = "create table comic(id integer primary key, titulo text, fecha Date, imagen text)";
    String sqlSelect = "select * from comic";
    String dropTable = "drop table comic";
    String sqlSelectById = "select id from comic where id = ";


    public ComicDatabaseSqLiteOpenHelper(Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(dropTable);
        db.execSQL(sqlCreate);
        onCreate(db);
    }

    public boolean consultarListaComics(String cod) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM "+TABLE_COMIC, null);

        while(cursor.moveToNext()){
            if(cursor.getString(0).equals(cod)) {
                return true;
            }
        }
       return false;
    }

    public Cursor dameComic(String cod) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_COMIC+" WHERE id = "+cod, null);
        return cursor;
    }

    public Cursor dameComicPorTitulo(String titulo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_COMIC+" WHERE titulo = "+titulo, null);
        return cursor;
    }

    public Cursor consultarDatos() {
        db = this.getReadableDatabase();
        cursor = db.rawQuery(sqlSelect, null);
        return cursor;
    }

}
