package ar.edu.untref.lp4.proyectodomotica.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.BuildConfig;
import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.adapters.GridHabitacionesAdapter;
import ar.edu.untref.lp4.proyectodomotica.baseDatos.BaseDatos;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBaseDatos;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBluetooth;
import ar.edu.untref.lp4.proyectodomotica.modelos.Habitacion;
import ar.edu.untref.lp4.proyectodomotica.tasks.ConexionTask;
import ar.edu.untref.lp4.proyectodomotica.utils.Constantes;
import ar.edu.untref.lp4.proyectodomotica.utils.MenuFlotante;

public class HabitacionesActivity extends Activity {

    private static final String TAG = HabitacionesActivity.class.getSimpleName();
    private static final String ID_HABITACION = "id_habitacion";
    private static final String NOMBRE_HABITACION = "nombre_habitacion";
    private ControladorBluetooth controladorBluetooth = ControladorBluetooth.getInstance();
    private long backPressed;

    private ProgressDialog progressDialog;
    private TextView textoConexion;
    private FloatingActionButton botonAgregarHabitacion;

    private GridHabitacionesAdapter adapter;
    private GridView gridview;

    private BaseDatos bd;
    private ControladorBaseDatos controladorBaseDatos;
    private List<Habitacion> habitaciones;
    private MenuFlotante menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitaciones);

        textoConexion = (TextView) findViewById(R.id.texto_conexion);
        textoConexion.setVisibility(View.GONE);

        botonAgregarHabitacion = (FloatingActionButton) findViewById(R.id.agregar_habitacion_boton);
        botonAgregarHabitacion.setVisibility(View.INVISIBLE);

        Logger.init(TAG);
        Logger.i("onCreate");

        bd = new BaseDatos(this, Constantes.NOMBRE_BD, null, Constantes.VERSION_BD);
        controladorBaseDatos = new ControladorBaseDatos(bd);

        realizarConexion();
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
     * Crea el boton para agregar habitacion
     */
    public void inicializarBotonAgregarHabitacion() {

        botonAgregarHabitacion.setVisibility(View.VISIBLE);
        botonAgregarHabitacion.setSize(FloatingActionButton.SIZE_NORMAL);
        botonAgregarHabitacion.setColorNormalResId(R.color.gris);
        botonAgregarHabitacion.setColorPressedResId(R.color.azul);
        botonAgregarHabitacion.setIcon(R.drawable.icono_agregar);
        botonAgregarHabitacion.setOnClickListener(agregarHabitacionListener);
    }

    /**
     * Crea el Menu con sus diferentes opciones
     */
    public void inicializarMenu() {

        menu = new MenuFlotante(this);

        //BOTON LOGOFF
        menu.logoff.setClickable(true);
        menu.logoff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.botonAccionMenu.close(true);
                Toast.makeText(getApplicationContext(), getString(R.string.proximamente), Toast.LENGTH_SHORT).show();
            }
        });


        //BOTON ACERCA DE
        menu.about.setClickable(true);
        menu.about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.botonAccionMenu.close(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(HabitacionesActivity.this);
                builder.setMessage(BuildConfig.VERSION_NAME)
                        .setTitle(getString(R.string.app_name))
                        .setCancelable(false)
                        .setNeutralButton(getString(R.string.entendido),
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
                menu.botonAccionMenu.close(true);
                Intent intent = new Intent(HabitacionesActivity.this, EstadisticasActivity.class);
                startActivity(intent);
            }
        });

        //BOTON CONFIGURACION
        menu.configuracion.setClickable(true);
        menu.configuracion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.botonAccionMenu.close(true);
                Intent intent = new Intent(HabitacionesActivity.this, ConfiguracionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void mostrarProgressBarConexion() {

        progressDialog = ProgressDialog.show(this, "", getString(R.string.estableciendo_conexion), true);
    }

    public void quitarProgressBarConexion() {

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
                finish();
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

        this.habitaciones.addAll(controladorBaseDatos.getTodasHabitaciones());
    }

    /**
     * Crea el GridView con las habitaciones que contenga la lista de habitaciones
     */
    public void inicializarGridViewHabitaciones() {

        adapter = new GridHabitacionesAdapter(this, habitaciones);

        gridview = (GridView) findViewById(R.id.grid_view_habitaciones);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                abrirArtefactosActivity(habitaciones.get(position));
            }
        });
    }

    private void abrirArtefactosActivity(Habitacion habitacion) {

        Intent intent = new Intent(HabitacionesActivity.this, ArtefactosActivity.class);
        intent.putExtra(ID_HABITACION, habitacion.getId());
        intent.putExtra(NOMBRE_HABITACION, habitacion.getNombre());
        startActivity(intent);
    }

    public void mostrarTextoConexion(boolean mostrar) {

        if (mostrar) {

            textoConexion.setVisibility(View.VISIBLE);

        } else {

            textoConexion.setVisibility(View.GONE);
        }
    }

    /**
     * En caso de que el menú este cerado, vuelve a la pantalla anterior.
     * En caso de que el menú esté abierto, lo cierra.
     */
    @Override
    public void onBackPressed() {

        Logger.i("onBackPressed");

        if (this.menu.botonAccionMenu.isOpen()) {

            this.menu.botonAccionMenu.close(true);

        } else {

            if (backPressed + 2000 > System.currentTimeMillis()) {

                mostrarDialogoBackPressed();

            } else {

                Toast.makeText(this, getString(R.string.presione_nuevamente), Toast.LENGTH_SHORT).show();
                backPressed = System.currentTimeMillis();
            }
        }
    }

    private View.OnClickListener agregarHabitacionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            escribirNombreHabitacion();

//            for (Habitacion hb : bd.getTodasHabitaciones()) {
//
//                String log = "Id: " + hb.getId() + " ,Nombre: " + hb.getNombre();
//                Logger.e("Nombre: " + log);
//            }
        }
    };

    private void escribirNombreHabitacion() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.nueva_habitacion);
        alertDialog.setMessage(R.string.nombre_habitacion);

        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton(getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String nombreHabitacion = editText.getText().toString();

                        if (!nombreHabitacion.isEmpty()) {

                            Habitacion habitacion = new Habitacion(nombreHabitacion);

                            controladorBaseDatos.agregarHabitacion(habitacion);

                            inicializarListaHabitaciones();
                            inicializarGridViewHabitaciones();

//                        if (nombreHabitacionDisponible(nombreHabitacion)) {
//
//                            Habitacion habitacion = new Habitacion(nombreHabitacion);
//
//                            controladorBaseDatos.agregarHabitacion(habitacion);
//
//                            inicializarListaHabitaciones();
//                            inicializarGridViewHabitaciones();
//
//                        } else {
//
//                            Toast.makeText(HabitacionesActivity.this, getString(R.string.habitacion_existente), Toast.LENGTH_SHORT).show();
//                        }

                        } else {

                            escribirNombreHabitacion();
                            Toast.makeText(HabitacionesActivity.this, getString(R.string.nombre_vacio), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        alertDialog.setNegativeButton(getString(R.string.cancelar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private boolean nombreHabitacionDisponible(String nombre) {

        boolean nombreDisponible = false;

        for (Habitacion habitacion : habitaciones) {

            if (!nombre.equals(habitacion.getNombre())) {

                nombreDisponible = true;
            }
        }

        return nombreDisponible;
    }
}

