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


    String sqlCreate = "create table comic(id integer primary key, titulo text, fecha Date, imagen text)";
    String sqlSelect = "select * from comic";
    String dropTable = "drop table comic";
    String sqlComp = "select idComic from comic where idComic = ?";

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


    /*
    public ArrayList<Comic> mostrarComics() {
        com = new ComicDatabaseSqLiteOpenHelper(context);
        db = com.getWritableDatabase();
        listComic = new ArrayList<>();

        cursor = db.rawQuery("Select * from comic", null);

        if(cursor.moveToFirst()) {
            do{
                comic = new Comic();
                comic.setNum(cursor.getString(0));
                comic.setTitle(cursor.getString(1));
                comic.setDay(cursor.getString(2));

                listComic.add(comic);
            } while(cursor.moveToNext());
        }
        cursor.close();

        return listComic;
    }
    */

    public boolean consultarListaComics(String cod) {
        db = com.getReadableDatabase();
        comic = null;

        cursor = db.rawQuery(sqlComp+cod, null);

       while(cursor.moveToNext()){
           if(cursor.getString(0).equals(cod)) {
               return true;
           }
       }
       return false;
    }

    //Método que me devuelve un comic al pasarle su id
    /*
    public Comic dameComic(String cod) {
        if(consultarListaComics(cod)) {
            db = com.getReadableDatabase();

        }
    }
    */

    public Cursor consultarDatos() {
        db = this.getReadableDatabase();
        cursor = db.rawQuery(sqlSelect, null);
        return cursor;
    }


}
