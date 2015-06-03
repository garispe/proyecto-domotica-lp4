package ar.edu.untref.lp4.proyectodomotica.baseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.modelos.Habitacion;

public class BaseDatos extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Nombre de la Base de Datos
    private static final String NOMBRE_BD = "DOMUNTREF";

    // Columnas de la tabla
    private static final String ID = "id";
    private static final String NOMBRE = "nombre";
    private static final String ARTEFACTOS = "artefactos";

    // Nombre de la tabla
    private static final String TABLA_HABITACIONES = "habitaciones";

    private SQLiteDatabase db;

    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    // Se crea la tabla
    public void onCreate(SQLiteDatabase db) {

        String CREAR_TABLA_HABITACION = "CREATE TABLE " + TABLA_HABITACIONES + "("
                + ID + " INTEGER PRIMARY KEY," + NOMBRE + " TEXT," + ")";

        db.execSQL(CREAR_TABLA_HABITACION);
    }

    @Override
    // Recicla la tabla
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLA_HABITACIONES);
        onCreate(db);

    }

    // Agrega la habitacion indicada
    public void agregarHabitacion(Habitacion habitacion){

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOMBRE, habitacion.getNombre());

        // Inserta una fila
        db.insert(TABLA_HABITACIONES, null, values);
        db.close();

    }

    // Devuelve la habitacion correspodiente al id
    public Habitacion getHabitacion(int id) {

        db = this.getReadableDatabase();
        String[] valores =  { ID, NOMBRE };

        Cursor cursor = db.query(TABLA_HABITACIONES, valores, ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null) {

            cursor.moveToFirst();
        }

        Habitacion habitacion = new Habitacion(cursor.getString(1));

        return habitacion;

    }

    // Devuelve todas las habitaciones almacenadas
    public List<Habitacion> getTodasHabitaciones() {

        List<Habitacion> listaHabitaciones = new ArrayList<Habitacion>();

        String selectQuery = "SELECT  * FROM " + TABLA_HABITACIONES;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Habitacion habitacion = new Habitacion();
                habitacion.setId(Integer.parseInt(cursor.getString(0)));
                habitacion.setNombre(cursor.getString(1));

                listaHabitaciones.add(habitacion);

            } while (cursor.moveToNext());
        }

        return listaHabitaciones;
    }

    // Elimina la habitacion que se pasa por parametro
    public void eliminarHabitacion(Habitacion habitacion) {

        db = this.getWritableDatabase();

        db.delete(TABLA_HABITACIONES, ID + " = ?",
                new String[]{String.valueOf(habitacion.getId())});

        db.close();
    }

    // Limpia la tabla
    public void limpiarBD() {

        db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLA_HABITACIONES);

        db.close();
    }
}
