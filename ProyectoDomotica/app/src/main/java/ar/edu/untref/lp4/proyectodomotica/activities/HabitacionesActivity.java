package ar.edu.untref.lp4.proyectodomotica.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
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

    private ShowcaseView showcaseAgregarHabitacion;
    private ShowcaseView showcaseIngresarHabitacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitaciones);

        textoConexion = (TextView) findViewById(R.id.texto_conexion);
        textoConexion.setVisibility(View.GONE);

        botonAgregarHabitacion = (FloatingActionButton) findViewById(R.id.agregar_habitacion_boton);

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
        botonAgregarHabitacion.setIcon(R.drawable.cruz);
        botonAgregarHabitacion.setOnClickListener(agregarHabitacionListener);
    }

    /**
     * Crea el Menu con sus diferentes opciones
     */
    public void inicializarMenu() {

        menu = new MenuFlotante(this);

        //BOTON REFRESH
        menu.refresh.setClickable(true);
        menu.refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.botonAccionMenu.close(true);

                if (!controladorBluetooth.estaConectado()) {

                    realizarConexion();

                } else {

                    Toast.makeText(HabitacionesActivity.this, "Ya se encuentra conectado", Toast.LENGTH_SHORT).show();
                }
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
        dialogo.setCancelable(true);

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

                if (menu.botonAccionMenu.isOpen()) {

                    menu.botonAccionMenu.close(true);

                } else {

                    showcaseIngresarHabitacion.hide();
                    abrirArtefactosActivity(habitaciones.get(position));
                }
            }
        });

        gridview.setLongClickable(true);
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                showcaseIngresarHabitacion.hide();

                final int pos = position;

                new BottomSheet.Builder(HabitacionesActivity.this).title(R.string.opcion).sheet(R.menu.menu_opciones_habitacion).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case R.id.eliminar:
                                borrarHabitacion((Habitacion) gridview.getItemAtPosition(pos));
                                break;

                            case R.id.editar:
                                editarHabitacion((Habitacion) gridview.getItemAtPosition(pos));
                                break;

                            case R.id.apagar:
                                Toast.makeText(HabitacionesActivity.this, "Apaga todos los componentes", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).show();
                return true;
            }
        });
    }

    private void editarHabitacion(final Habitacion habitacion) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.editar);
        alertDialog.setMessage(R.string.nuevo_nombre_habitacion);

        final EditText editText = new EditText(this);
        editText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton(getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String nombreHabitacion = editText.getText().toString();

                        if (nombreHabitacionDisponible(nombreHabitacion)) {

                            habitacion.setNombre(nombreHabitacion);

                            controladorBaseDatos.actualizarNombreHabitacion(habitacion);

                            inicializarListaHabitaciones();
                            inicializarGridViewHabitaciones();

                        } else {

                            escribirNombreHabitacion();
                            Toast.makeText(HabitacionesActivity.this, getString(R.string.habitacion_existente), Toast.LENGTH_SHORT).show();
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

    private void borrarHabitacion(final Habitacion habitacion) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.eliminar_habitacion_titulo));
        alertDialog.setMessage(getString(R.string.eliminar_habitacion_mensaje));


        alertDialog.setPositiveButton(getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        controladorBaseDatos.eliminarHabitacion(habitacion);
                        inicializarListaHabitaciones();
                        inicializarGridViewHabitaciones();

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

    private void abrirArtefactosActivity(Habitacion habitacion) {

        Intent intent = new Intent(HabitacionesActivity.this, ArtefactosActivity.class);
        intent.putExtra(Constantes.ID_HABITACION, habitacion.getId());
        intent.putExtra(Constantes.NOMBRE_HABITACION, habitacion.getNombre());
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

            showcaseAgregarHabitacion.hide();
            escribirNombreHabitacion();
        }
    };

    private void escribirNombreHabitacion() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.nueva_habitacion);
        alertDialog.setMessage(R.string.nombre_habitacion);

        final EditText editText = new EditText(this);
        editText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton(getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String nombreHabitacion = editText.getText().toString();

                        if (nombreHabitacion.isEmpty()) {

                            escribirNombreHabitacion();
                            Toast.makeText(HabitacionesActivity.this, getString(R.string.nombre_vacio), Toast.LENGTH_SHORT).show();

                        } else {

                            if (nombreHabitacionDisponible(nombreHabitacion)) {

                                Habitacion habitacion = new Habitacion(nombreHabitacion);

                                controladorBaseDatos.agregarHabitacion(habitacion);

                                inicializarListaHabitaciones();
                                inicializarGridViewHabitaciones();

                                mostrarShowcaseHabitacion();


                            } else {

                                escribirNombreHabitacion();
                                Toast.makeText(HabitacionesActivity.this, getString(R.string.habitacion_existente), Toast.LENGTH_SHORT).show();
                            }
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

        boolean nombreDisponible = true;

        if (habitaciones.size() > 0) {

            for (Habitacion habitacion : habitaciones) {

                if (nombre.equals(habitacion.getNombre())) {

                    nombreDisponible = false;
                }
            }
        }

        return nombreDisponible;
    }

    @Override
    protected void onResume() {
        super.onResume();

        inicializarListaHabitaciones();
        inicializarGridViewHabitaciones();
    }

    public void mostrarShowcaseInicial() {

        RelativeLayout.LayoutParams paramsBotonAgregar = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsBotonAgregar.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsBotonAgregar.addRule(RelativeLayout.CENTER_VERTICAL);

        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();

        paramsBotonAgregar.setMargins(margin, margin, margin, margin);

        ViewTarget targetBotonAgregar = new ViewTarget(R.id.agregar_habitacion_boton, this);

        showcaseAgregarHabitacion = new ShowcaseView.Builder(this)
                .setTarget(targetBotonAgregar)
                .setContentTitle(getString(R.string.agregar_habitacion))
                .setContentText(getString(R.string.mensaje_agregar_habitacion_showcase))
                .build();

        showcaseAgregarHabitacion.setButtonPosition(paramsBotonAgregar);
    }

    private void mostrarShowcaseHabitacion() {

        RelativeLayout.LayoutParams paramsHabitacion = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsHabitacion.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsHabitacion.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();

        paramsHabitacion.setMargins(margin, margin, margin, margin);

        ViewTarget targetGridView = new ViewTarget(R.id.cuadro_showcase, HabitacionesActivity.this);

        showcaseIngresarHabitacion = new ShowcaseView.Builder(HabitacionesActivity.this)
                .setTarget(targetGridView)
                .setContentTitle(getString(R.string.habitacion))
                .setContentText(getString(R.string.mensaje_showcase_habitacion))
                .build();

        showcaseIngresarHabitacion.setButtonPosition(paramsHabitacion);

    }
}

