package ar.edu.untref.lp4.proyectodomotica.tasks;

import android.os.AsyncTask;

import ar.edu.untref.lp4.proyectodomotica.activities.HabitacionesActivity;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBluetooth;

/**
 * Tarea que realiza la conexión bluetooth en background, utilizando el ControladorBluetooth.
 */
public class ConexionTask extends AsyncTask <Void, Void, Void>{

    private ControladorBluetooth controladorBluetooth;
    private HabitacionesActivity activity;

    public ConexionTask(ControladorBluetooth controladorBluetooth, HabitacionesActivity activity){

        this.controladorBluetooth = controladorBluetooth;
        this.activity = activity;
    }

    /**
     * Realiza la conexión con el módulo bluetooth en background.
     */
    @Override
    protected Void doInBackground(Void... params) {

        this.controladorBluetooth.conectar();

        return null;
    }

    /**
     * Se ejecuta luego de que se haya ejecutado el método doInBackground.
     * Inicializa la lista de habitaciones y crea el GridView con ella.
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        activity.inicializarListaHabitaciones();
        activity.quitarProgressBarConexion();
        activity.inicializarGridViewHabitaciones();
        activity.inicializarMenu();
    }
}
