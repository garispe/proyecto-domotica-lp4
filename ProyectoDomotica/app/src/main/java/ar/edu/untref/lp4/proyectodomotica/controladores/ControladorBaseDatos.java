package ar.edu.untref.lp4.proyectodomotica.controladores;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.baseDatos.BaseDatos;
import ar.edu.untref.lp4.proyectodomotica.modelos.Artefacto;
import ar.edu.untref.lp4.proyectodomotica.modelos.Habitacion;
import ar.edu.untref.lp4.proyectodomotica.utils.Constantes;

public class ControladorBaseDatos {

    private static SQLiteDatabase db;
    private static BaseDatos baseDatos;

    public ControladorBaseDatos(BaseDatos baseDatos) {

        this.baseDatos = baseDatos;
    }

    /**
     * Agrega la habitacion indicada.
     */
    public static void agregarHabitacion(Habitacion habitacion) {

        db = baseDatos.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constantes.NOMBRE_HABITACION, habitacion.getNombre());

        db.insert(Constantes.TABLA_HABITACIONES, null, values);
        db.close();
    }

    /**
     * Devuelve la habitacion correspodiente al id.
     */
    public static Habitacion getHabitacion(int id) {

        db = baseDatos.getReadableDatabase();
        String[] valores = {Constantes.ID_HABITACION, Constantes.NOMBRE_HABITACION};

        Cursor cursor = db.query(Constantes.TABLA_HABITACIONES, valores, Constantes.ID_HABITACION + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Habitacion habitacion = null;

        if (cursor != null) {

            cursor.moveToFirst();
            habitacion = new Habitacion(cursor.getString(1));
        }

        // CORREGIR, ESTA DEVOLVIENDO MAL LA HABITACION


        cursor.close();

        return habitacion;
    }

    /**
     * Devuelve todas las habitaciones almacenadas.
     */
    public static List<Habitacion> getTodasHabitaciones() {

        List<Habitacion> listaHabitaciones = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Constantes.TABLA_HABITACIONES;

        db = baseDatos.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Habitacion habitacion = new Habitacion();
                habitacion.setId(cursor.getInt(0));
                habitacion.setNombre(cursor.getString(1));

                listaHabitaciones.add(habitacion);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return listaHabitaciones;
    }

    /**
     * Elimina la habitacion que se pasa por parametro.
     */
    public static void eliminarHabitacion(Habitacion habitacion) {

        db = baseDatos.getWritableDatabase();

        db.delete(Constantes.TABLA_HABITACIONES, Constantes.ID_HABITACION + " = ?",
                new String[]{String.valueOf(habitacion.getId())});

        db.close();
    }

    /**
     * Elimina el artefacto que se pasa por parametro.
     */
    public static void eliminarArtefacto(Artefacto artefacto) {

        db = baseDatos.getWritableDatabase();

        db.delete(Constantes.TABLA_ARTEFACTOS, Constantes.ID_ARTEFACTO + " = ?",
                new String[]{String.valueOf(artefacto.getId())});

        db.close();
    }

    /**
     * Devuelve los artefactos correspodiente a la habitacion.
     */
    public static List<Artefacto> getArtefactosPorHabitacion(int idHabitacion) {

        List<Artefacto> listaArtefactos = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Constantes.TABLA_ARTEFACTOS
                + " WHERE " + Constantes.FK_HABITACION + "=" + idHabitacion;

        db = baseDatos.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {
                Artefacto artefacto = new Artefacto();
                artefacto.setId(cursor.getInt(0));
                artefacto.setNombre(cursor.getString(1));

                if (cursor.getInt(2) == 1) {

                    artefacto.setActivo(true);

                } else {

                    artefacto.setActivo(false);
                }

                listaArtefactos.add(artefacto);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return listaArtefactos;
    }


    /**
     * Agrega un artefacto a la tabla "artefactos". Si ya existe sólo actualiza su estado.
     */
    public static void agregarArtefacto(int idHabitacion, Artefacto artefacto) {

        db = baseDatos.getWritableDatabase();

        ContentValues values = new ContentValues();

        if (artefacto.isActivo()) {

            values.put(Constantes.ESTADO, 1);

        } else {

            values.put(Constantes.ESTADO, 0);
        }

        values.put(Constantes.NOMBRE_ARTEFACTO, artefacto.getNombre());
        values.put(Constantes.FK_HABITACION, idHabitacion);

        if (getArtefactosPorHabitacion(idHabitacion).size() > 0) {

            values.put(Constantes.ID_PIN, getUltimoPin() + 1);

        }

        db.insert(Constantes.TABLA_ARTEFACTOS, null, values);
        db.close();
    }

    private static int getUltimoPin() {

        int pin = 0;
        int max = 0;

        Cursor c = db.rawQuery("SELECT " + Constantes.ID_PIN + " FROM "
                + Constantes.TABLA_ARTEFACTOS, null);

        if (c.moveToFirst()) {

            while (c.moveToNext()) {

                pin = c.getInt(c.getColumnIndex(Constantes.ID_PIN));

                if (pin > max) {

                    max = pin;
                }
            }
        }

        c.close();

        return max;
    }

    /**
     * Si el artefacto existe en la base de datos retorna su ID, de lo contrario, retorna -1.
     */
    private static int getArtefactoId(Artefacto artefacto) {

        Cursor c = db.rawQuery("SELECT " + Constantes.ID_ARTEFACTO + " FROM " + Constantes.TABLA_ARTEFACTOS
                + " WHERE " + Constantes.ID_ARTEFACTO + " = " + artefacto.getId(), null);

        if (c.moveToFirst()) {

            return c.getInt(c.getColumnIndex(Constantes.ID_ARTEFACTO));
        }

        return -1;
    }

    public static void actualizarEstadoArtefacto(Artefacto artefacto) {

        db = baseDatos.getWritableDatabase();
        ContentValues values = new ContentValues();

        int idArtefacto = getArtefactoId(artefacto);

        if (artefacto.isActivo()) {

            values.put(Constantes.ESTADO, 1);

        } else {

            values.put(Constantes.ESTADO, 0);
        }

        if (idArtefacto != -1) {

            String where = Constantes.ID_ARTEFACTO + "=" + idArtefacto;

            db.update(Constantes.TABLA_ARTEFACTOS, values, where, null);
        }

        db.close();
    }

    public static void actualizarEstadoHabitacion(Habitacion habitacion) {

        db = baseDatos.getWritableDatabase();
        ContentValues values = new ContentValues();

        int idHabitacion = getHabitacionId(habitacion);

        values.put(Constantes.NOMBRE_HABITACION, habitacion.getNombre());

        if (idHabitacion != -1) {

            String where = Constantes.ID_HABITACION + "=" + idHabitacion;

            db.update(Constantes.TABLA_HABITACIONES, values, where, null);
        }

        db.close();

    }

    private static int getHabitacionId(Habitacion habitacion) {

        Cursor c = db.rawQuery("SELECT " + Constantes.ID_HABITACION + " FROM " + Constantes.TABLA_HABITACIONES
                + " WHERE " + Constantes.ID_HABITACION + " = " + habitacion.getId(), null);

        if (c.moveToFirst()) {

            return c.getInt(c.getColumnIndex(Constantes.ID_ARTEFACTO));
        }

        return -1;
    }

    /**
     * Limpia las tablas "habitaciones" y "artefactos".
     */
    public static void limpiarBD() {

        db = baseDatos.getWritableDatabase();

        db.execSQL("DELETE FROM " + Constantes.TABLA_HABITACIONES);
        db.execSQL("DELETE FROM " + Constantes.TABLA_ARTEFACTOS);

        db.close();
    }

    public static boolean getEstadoArtefacto(Artefacto artefacto) {

        db = baseDatos.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT " + Constantes.ESTADO + " FROM " + Constantes.TABLA_ARTEFACTOS
                + " WHERE " + Constantes.ID_ARTEFACTO + " = " + artefacto.getId(), null);

        int estado;

        if (c.moveToFirst()) {

            estado = c.getInt(c.getColumnIndex(Constantes.ESTADO));

            if (estado == 1) {

                return true;
            }
        }
        return false;
    }
}
