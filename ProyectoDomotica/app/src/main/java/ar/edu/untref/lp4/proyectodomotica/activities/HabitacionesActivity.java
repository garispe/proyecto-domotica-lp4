package ar.edu.untref.lp4.proyectodomotica.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
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

public class HabitacionesActivity extends ActionBarActivity {

    private static final String TAG = HabitacionesActivity.class.getSimpleName();
    private static final String NOMBRE_HABITACION = "nombre_habitacion";
    private long backPressed;

    private ControladorBluetooth controladorBluetooth;

    private List<Habitacion> habitaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitaciones);

        Logger.init(TAG);
        Logger.i("onCreate");

        this.controladorBluetooth = new ControladorBluetooth();

        realizarConexion();

        // Se crean las instancias del menu.
        final ImageView fabIconNew = new ImageView(this);
        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_settings));
        final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconNew)
                .setBackgroundDrawable(R.drawable.button_action_blue_selector)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_LEFT)
                .build();

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);

        ImageView rlIcon1 = new ImageView(this);
        ImageView rlIcon2 = new ImageView(this);
        ImageView rlIcon3 = new ImageView(this);
        ImageView rlIcon4 = new ImageView(this);

        rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_settings));
        rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_settings));
        rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_settings));
        rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_settings));

        // Build the menu with default options: light theme
        // Set 4 default SubActionButtons
        final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
                .setRadius(140) //Define el radio de despligue del menu
                .setStartAngle(0) //Define a donde arranca el menu que se despliega.
                .setEndAngle(-90) //Define para donde se va a abrir el abanico de opciones.
                .attachTo(rightLowerButton)
                .build();

        // Listen menu open and close events to animate the button content view
        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                fabIconNew.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                fabIconNew.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }
        });



    }

    /**
     * Lanza la tarea que realiza la conexion Bluetooth
     */
    private void realizarConexion() {

        if (!this.controladorBluetooth.getBluetoothAdapter().isEnabled()) {

            mostrarDialogoEncendidoBluetooth();

        } else {

            ConexionTask conexionTask = new ConexionTask(this.controladorBluetooth, HabitacionesActivity.this);
            conexionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
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
}
