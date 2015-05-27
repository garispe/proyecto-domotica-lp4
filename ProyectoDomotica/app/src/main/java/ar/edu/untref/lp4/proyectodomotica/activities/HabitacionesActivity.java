package ar.edu.untref.lp4.proyectodomotica.activities;


import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.adapters.GridHabitacionesAdapter;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBluetooth;
import ar.edu.untref.lp4.proyectodomotica.modelos.Habitacion;
import ar.edu.untref.lp4.proyectodomotica.tasks.ConexionTask;
import ar.edu.untref.lp4.proyectodomotica.utils.MenuFlotante;

public class HabitacionesActivity extends Activity {

    private static final String TAG = HabitacionesActivity.class.getSimpleName();
    private static final String NOMBRE_HABITACION = "nombre_habitacion";
    private long backPressed;

    private TextView textoConexion;

    private List<Habitacion> habitaciones;
    private ControladorBluetooth controladorBluetooth = ControladorBluetooth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitaciones);

        textoConexion = (TextView) findViewById(R.id.texto_conexion);
        textoConexion.setVisibility(View.GONE);

        Logger.init(TAG);
        Logger.i("onCreate");

        realizarConexion();
        inicializarMenu();
    }

    /**
     * Lanza la tarea que realiza la conexion Bluetooth
     */
    private void realizarConexion() {

        if (!controladorBluetooth.getBluetoothAdapter().isEnabled()) {

            mostrarDialogoEncendidoBluetooth();

        } else {

            mostrarProgressBarConexion();
            ConexionTask conexionTask = new ConexionTask(controladorBluetooth, HabitacionesActivity.this);
            conexionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    /**
     * Crea el Menu con sus diferentes opciones
     */
    public void inicializarMenu(){

        MenuFlotante menu = new MenuFlotante(this);

        //BOTON LOGOFF
        //Implementar!     <-----
        menu.logoff.setClickable(true);
        menu.logoff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Implementar", Toast.LENGTH_SHORT).show();
            }
        });



        //BOTON ACERCA DE
        menu.about.setClickable(true);
        menu.about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HabitacionesActivity.this);
                builder.setMessage("Version 1.0.0")
                        .setTitle("DomUntref")
                        .setCancelable(false)
                        .setNeutralButton("Entendido",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //BOTON ESTADISTICAS
        menu.estadisticas.setClickable(true);
        menu.estadisticas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitacionesActivity.this, EstadisticasActivity.class);
                startActivity(intent);
            }
        });

        //BOTON CONFIGURACION
        menu.configuracion.setClickable(true);
        menu.configuracion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitacionesActivity.this, ConfiguracionActivity.class);
                startActivity(intent);
            }
        });

    }

    private ProgressDialog progressDialog;

    private void mostrarProgressBarConexion(){

        progressDialog = ProgressDialog.show(this, "", getString(R.string.estableciendo_conexion), true);
    }

    public void quitarProgressBarConexion(){

        progressDialog.dismiss();
    }
    /**
     * Muestra un dialogo al ingresar a la aplicacion, solicitando el encendido del Bluetooth
     */
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
            }
        });

        dialogo.setPositiveButton(R.string.activar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mostrarProgressBarConexion();
                ConexionTask conexionTask = new ConexionTask(controladorBluetooth, HabitacionesActivity.this);
                conexionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        dialogo.show();
    }

    /**
     * Muestra un dialogo para que al salir, el usuario decida si quiere mantener la conexion Bluetooth
     */
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

    /**
     * Agrega las habitaciones a la lista de habitaciones
     */
    public void inicializarListaHabitaciones() {

        this.habitaciones = new ArrayList<>();

        /*
        Las habitaciones van almacenarse y leerse desde donde esten almacenados
        Por ejemplo, de una base de datos: this.habitaciones.addAll(baseDatos.getHabitaciones());
        */
        this.habitaciones.add(new Habitacion("Cocina"));
        this.habitaciones.add(new Habitacion("Habitacion"));
        this.habitaciones.add(new Habitacion("Living"));
        this.habitaciones.add(new Habitacion("Baño"));
        this.habitaciones.add(new Habitacion("Garage"));
    }

    /**
     * Crea el GridView con las habitaciones que contenga la lista de habitaciones
     */
    public void inicializarGridViewHabitaciones() {

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

        Logger.i("onBackPressed");

        if (backPressed + 2000 > System.currentTimeMillis()) {
            mostrarDialogoBackPressed();

        } else {

            Toast.makeText(this, "Presione de nuevo para salir", Toast.LENGTH_SHORT).show();
            backPressed = System.currentTimeMillis();
        }
    }

    public void mostrarTextoConexion(boolean mostrar){

        if(mostrar) {

            textoConexion.setVisibility(View.VISIBLE);

        } else {

            textoConexion.setVisibility(View.GONE);
        }
    }

}
