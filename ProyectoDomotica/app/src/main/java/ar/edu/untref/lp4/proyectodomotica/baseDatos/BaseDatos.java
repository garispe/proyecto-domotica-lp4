package ar.edu.untref.lp4.proyectodomotica.baseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.Constantes;
import ar.edu.untref.lp4.proyectodomotica.modelos.Habitacion;

public class BaseDatos extends SQLiteOpenHelper {

    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    // Se crea la tabla
    public void onCreate(SQLiteDatabase db) {

        String CREAR_TABLA_HABITACION = "CREATE TABLE " + Constantes.TABLA_HABITACIONES + "("
                + Constantes.ID + " INTEGER PRIMARY KEY," + Constantes.NOMBRE + " TEXT" + ")";

        db.execSQL(CREAR_TABLA_HABITACION);
    }

    @Override
    // Recicla la tabla
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constantes.TABLA_HABITACIONES);
        onCreate(db);
    }
}
