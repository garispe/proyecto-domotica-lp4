package ar.edu.untref.lp4.proyectodomotica.controladores;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.Constantes;
import ar.edu.untref.lp4.proyectodomotica.baseDatos.BaseDatos;
import ar.edu.untref.lp4.proyectodomotica.modelos.Habitacion;

public class ControladorBaseDatos {

    private SQLiteDatabase db;
    private BaseDatos baseDatos;

    public ControladorBaseDatos(BaseDatos baseDatos) {

        this.baseDatos = baseDatos;
    }

    // Agrega la habitacion indicada
    public void agregarHabitacion(Habitacion habitacion) {

        db = baseDatos.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constantes.NOMBRE, habitacion.getNombre());

        // Inserta una fila
        db.insert(Constantes.TABLA_HABITACIONES, null, values);
        db.close();
    }

    // Devuelve la habitacion correspodiente al id
    public Habitacion getHabitacion(int id) {

        db = baseDatos.getReadableDatabase();
        String[] valores = {Constantes.ID, Constantes.NOMBRE};

        Cursor cursor = db.query(Constantes.TABLA_HABITACIONES, valores, Constantes.ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {

            cursor.moveToFirst();
        }

        Habitacion habitacion = new Habitacion(cursor.getString(1));

        return habitacion;
    }

    // Devuelve todas las habitaciones almacenadas
    public List<Habitacion> getTodasHabitaciones() {

        List<Habitacion> listaHabitaciones = new ArrayList<Habitacion>();

        String selectQuery = "SELECT  * FROM " + Constantes.TABLA_HABITACIONES;

        db = baseDatos.getWritableDatabase();
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

        db = baseDatos.getWritableDatabase();

        db.delete(Constantes.TABLA_HABITACIONES, Constantes.ID + " = ?",
                new String[]{String.valueOf(habitacion.getId())});

        db.close();
    }

    // Limpia la tabla
    public void limpiarBD() {

        db = baseDatos.getWritableDatabase();

        db.execSQL("DELETE FROM " + Constantes.TABLA_HABITACIONES);

        db.close();
    }

}
