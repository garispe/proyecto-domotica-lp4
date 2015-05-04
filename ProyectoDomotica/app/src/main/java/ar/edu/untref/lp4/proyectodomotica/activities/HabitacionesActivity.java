package ar.edu.untref.lp4.proyectodomotica.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.adapters.GridHabitacionesAdapter;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBluetooth;
import ar.edu.untref.lp4.proyectodomotica.modelos.Habitacion;
import ar.edu.untref.lp4.proyectodomotica.tasks.ConexionTask;

public class HabitacionesActivity extends ActionBarActivity {

    private static final String TAG = "HabitacionesActivity --->";
    private static final String NOMBRE_HABITACION = "nombre_habitacion";
    private long backPressed;

    private ControladorBluetooth controladorBluetooth;

    private List<Habitacion> habitaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitaciones);

        Log.e(TAG, "onCreate");

        this.controladorBluetooth = new ControladorBluetooth();

        if(!this.controladorBluetooth.getBluetoothAdapter().isEnabled()) {

            mostrarDialogoEncendidoBluetooth();

        } else {

            ConexionTask conexionTask = new ConexionTask(this.controladorBluetooth, HabitacionesActivity.this);
            conexionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void mostrarDialogoEncendidoBluetooth() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);

        dialogo.setTitle(R.string.titulo_dialogo_encendido);
        dialogo.setMessage(R.string.texto_dialogo_encendido);
        dialogo.setIcon(R.drawable.icono_bluetooth);
        dialogo.setCancelable(false);

        dialogo.setNegativeButton(R.string.salir, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controladorBluetooth.desconectar();
                finish();
            }
        });

        dialogo.setPositiveButton(R.string.activar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ConexionTask conexionTask = new ConexionTask(controladorBluetooth, HabitacionesActivity.this);
                conexionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        dialogo.show();
    }

    private void mostrarDialogoBackPressed() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);

        dialogo.setTitle(R.string.titulo_dialogo_salir);
        dialogo.setMessage(R.string.texto_dialogo_salir);
        dialogo.setIcon(R.drawable.icono_salir);
        dialogo.setCancelable(false);

        dialogo.setNegativeButton(R.string.salir, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        dialogo.setPositiveButton(R.string.salir_y_desactivar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controladorBluetooth.desconectar();
                finish();
            }
        });

        dialogo.show();
    }

    public void inicializarListaHabitaciones() {

        this.habitaciones = new ArrayList<>();

        /*

        Los nombres e imagenes van almacenarse y leerse desde donde esten almacenados

           Por ejemplo, de una base de datos:

           this.habitaciones.addAll(baseDatos.getHabitaciones());

         */

        this.habitaciones.add(new Habitacion("Cocina"));
        this.habitaciones.add(new Habitacion("Habitacion"));
        this.habitaciones.add(new Habitacion("Living"));
        this.habitaciones.add(new Habitacion("Baño"));
        this.habitaciones.add(new Habitacion("Garage"));
    }

    public void inicializarGridView() {

        GridHabitacionesAdapter adapter = new GridHabitacionesAdapter(this, habitaciones);

        GridView gridview = (GridView) findViewById(R.id.grid_view_habitaciones);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                abrirArtefactosActivity(habitaciones.get(position).getNombre());
            }
        });
    }

    private void abrirArtefactosActivity(String habitacion) {

        Intent intent = new Intent(HabitacionesActivity.this, ArtefactosActivity.class);
        intent.putExtra(NOMBRE_HABITACION, habitacion);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        Log.e(TAG, "onBackPressed");

        if(backPressed + 2000 > System.currentTimeMillis()){
            mostrarDialogoBackPressed();

        } else {

            Toast.makeText(this, "Presione de nuevo para salir", Toast.LENGTH_SHORT).show();
            backPressed = System.currentTimeMillis();
        }
    }
}
