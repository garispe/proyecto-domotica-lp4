package ar.edu.untref.lp4.proyectodomotica.baseDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ar.edu.untref.lp4.proyectodomotica.utils.Constantes;

public class BaseDatos extends SQLiteOpenHelper {

    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    // Se crea la tabla
    public void onCreate(SQLiteDatabase db) {

        String CREAR_TABLA_HABITACION = "CREATE TABLE " + Constantes.TABLA_HABITACIONES + "("
                + Constantes.ID_HABITACION + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constantes.NOMBRE_HABITACION + " TEXT" + ")";

        String CREAR_TABLA_ARTEFACTO = "CREATE TABLE " + Constantes.TABLA_ARTEFACTOS + "("
                + Constantes.ID_ARTEFACTO + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constantes.NOMBRE_ARTEFACTO
                + " TEXT, " + Constantes.ESTADO + " INTEGER, " + Constantes.FK_HABITACION + " INTEGER, "
                +  Constantes.ID_PIN + " INTEGER )";

        db.execSQL(CREAR_TABLA_HABITACION);
        db.execSQL(CREAR_TABLA_ARTEFACTO);
    }

    @Override
    // Recicla la tabla
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constantes.TABLA_HABITACIONES);
        db.execSQL("DROP TABLE IF EXISTS " + Constantes.TABLA_ARTEFACTOS);
        onCreate(db);
    }
}
