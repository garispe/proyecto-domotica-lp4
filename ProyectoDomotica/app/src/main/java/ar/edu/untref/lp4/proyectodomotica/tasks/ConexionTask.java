package ar.edu.untref.lp4.proyectodomotica.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.activities.HabitacionesActivity;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBluetooth;

/**
 * Tarea que realiza la conexión bluetooth en background, utilizando el ControladorBluetooth.
 */
public class ConexionTask extends AsyncTask<Void, Void, Void> {

    private ControladorBluetooth controladorBluetooth;
    private HabitacionesActivity activity;

    private boolean estaConectado = false;

    public ConexionTask(ControladorBluetooth controladorBluetooth, HabitacionesActivity activity) {

        this.controladorBluetooth = controladorBluetooth;
        this.activity = activity;
    }

    /**
     * Realiza la conexión con el módulo bluetooth en background.
     */
    @Override
    protected Void doInBackground(Void... params) {

        this.estaConectado = this.controladorBluetooth.conectar();

        return null;
    }

    /**
     * Se ejecuta luego de que se haya ejecutado el método doInBackground.
     * Inicializa la lista de habitaciones y crea el GridView con ella.
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (!estaConectado) {

            activity.mostrarTextoConexion(true);
            Toast.makeText(activity, activity.getString(R.string.conexion_no_establecida), Toast.LENGTH_SHORT).show();

        } else {

            activity.mostrarTextoConexion(false);
        }

        activity.inicializarMenu();
        activity.inicializarBotonAgregarHabitacion();
        activity.inicializarBotonHablar();
        activity.inicializarListaHabitaciones();
        activity.quitarProgressBarConexion();
        activity.inicializarGridViewHabitaciones();

        SharedPreferences preferences = activity.getSharedPreferences("du_preferences", Context.MODE_PRIVATE);
        boolean mostrarTutorial = preferences.getBoolean("mostrar_tutorial", true);

        if(mostrarTutorial) {
            activity.mostrarShowcaseInicial();
        }
    }
}
