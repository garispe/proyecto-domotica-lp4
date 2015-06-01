package ar.edu.untref.lp4.proyectodomotica.utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Clase que se encarga de administrar las preferencias de la aplicacion
 * (DomUntref Preferences)
 */
public class DUPreferences {

    public static final String PREFERENCIAS = "domuntref_preferences";

    public static void guardarBoolean(Context context, String clave, Boolean valor) {

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(clave, valor);
        editor.apply();
    }

    public static boolean existe(Context context, String clave) {

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        return preferences.contains(clave);
    }

    public static boolean getBoolean(Context context, String clave, boolean valorPorDefecto) {

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        return preferences.getBoolean(clave, valorPorDefecto);
    }
}
