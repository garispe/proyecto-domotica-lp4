package ar.edu.untref.lp4.proyectodomotica.tasks;

import android.os.AsyncTask;

import ar.edu.untref.lp4.proyectodomotica.activities.HabitacionesActivity;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBluetooth;

public class ConexionTask extends AsyncTask <Void, Void, Void>{

    private ControladorBluetooth controladorBluetooth;
    private HabitacionesActivity activity;

    public ConexionTask(ControladorBluetooth controladorBluetooth, HabitacionesActivity activity){

        this.controladorBluetooth = controladorBluetooth;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {

        this.controladorBluetooth.conectar();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        activity.inicializarListaHabitaciones();
        activity.inicializarGridView();
    }
}
