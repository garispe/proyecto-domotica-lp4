package ar.edu.untref.lp4.proyectodomotica.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.BuildConfig;
import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.adapters.ListViewArtefactosAdapter;
import ar.edu.untref.lp4.proyectodomotica.baseDatos.BaseDatos;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBaseDatos;
import ar.edu.untref.lp4.proyectodomotica.modelos.Artefacto;
import ar.edu.untref.lp4.proyectodomotica.modelos.Habitacion;
import ar.edu.untref.lp4.proyectodomotica.utils.Constantes;
import ar.edu.untref.lp4.proyectodomotica.utils.MenuFlotante;

public class ArtefactosActivity extends Activity {

    private static final String TAG = ArtefactosActivity.class.getSimpleName();
    private static final String ID_HABITACION = "id_habitacion";
    public static final String NOMBRE_HABITACION = "nombre_habitacion";

    private List<Artefacto> artefactos;
    private ListViewArtefactosAdapter artefactosAdapter;
    private MenuFlotante menu;
    private FloatingActionButton botonAgregarHabitacion;
    public static String nombreHabitacion;
    private int idHabitacion;
    private BaseDatos bd;
    private ControladorBaseDatos controladorBaseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artefactos);

        Logger.init(TAG);

        nombreHabitacion = getIntent().getExtras().getString(NOMBRE_HABITACION);

        idHabitacion = getIntent().getExtras().getInt(ID_HABITACION);

        TextView habitacion = (TextView) findViewById(R.id.nombre_habitacion);
        habitacion.setText(nombreHabitacion);

        inicializarBotonAgregar();
        inicializarListaArtefactosPorHabitacion();
        inicializarListViewArtefactos();
        inicializarMenu();
        bd = new BaseDatos(this, Constantes.NOMBRE_BD, null, Constantes.VERSION_BD);
        controladorBaseDatos = new ControladorBaseDatos(bd);
    }

    private void inicializarBotonAgregar() {

        botonAgregarHabitacion = (FloatingActionButton) findViewById(R.id.agregar_artefacto_boton);
        botonAgregarHabitacion.setSize(FloatingActionButton.SIZE_NORMAL);
        botonAgregarHabitacion.setColorNormalResId(R.color.gris);
        botonAgregarHabitacion.setColorPressedResId(R.color.azul);
        botonAgregarHabitacion.setIcon(R.drawable.icono_agregar);
        botonAgregarHabitacion.setOnClickListener(agregarArtefactoListener);
    }

    /**
     * Agrega los artefactos correspondientes de acuerdo a la habitacion seleccionada
     */
    private void inicializarListaArtefactosPorHabitacion() {

        this.artefactos = new ArrayList<>();
        this.artefactos.addAll(ControladorBaseDatos.getArtefactosPorHabitacion(idHabitacion));
    }

    /**
     * Crea el ListView con la lista de artefactos previamente creada
     */
    private void inicializarListViewArtefactos() {

        final ListView listView = (ListView) findViewById(R.id.lista_artefactos);

        artefactosAdapter = new ListViewArtefactosAdapter(this, artefactos);
        listView.setAdapter(artefactosAdapter);
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(ArtefactosActivity.this,"probando",Toast.LENGTH_SHORT).show();
                //borrarArtefacto((Artefacto) listView.getItemAtPosition(position));
                return true;
            }
        });


    }

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
                AlertDialog.Builder builder = new AlertDialog.Builder(ArtefactosActivity.this);
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
                Intent intent = new Intent(ArtefactosActivity.this, EstadisticasActivity.class);
                startActivity(intent);
            }
        });

        //BOTON CONFIGURACION
        menu.configuracion.setClickable(true);
        menu.configuracion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.botonAccionMenu.close(true);
                Intent intent = new Intent(ArtefactosActivity.this, ConfiguracionActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * En caso de que el menú este cerrado, vuelve a la pantalla anterior.
     * En caso de que el menú esté abierto, lo cierra.
     */
    @Override
    public void onBackPressed() {

        if (this.menu.botonAccionMenu.isOpen()) {

            this.menu.botonAccionMenu.close(true);

        } else {

            finish();
        }
    }

    private View.OnClickListener agregarArtefactoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            escribirNombreArtefacto();
        }
    };

    private void borrarArtefacto(final Artefacto artefacto) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Eliminacion");
        alertDialog.setMessage("¿Desea borrar el artefacto?");


        alertDialog.setPositiveButton(getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

//                        controladorBaseDatos.eliminarArtefacto(artefacto);


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

    private void escribirNombreArtefacto() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.nuevo_artefacto);
        alertDialog.setMessage(R.string.nombre_artefacto);

        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton(getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String nombreArtefacto = editText.getText().toString();

                        if (!nombreArtefacto.isEmpty()) {

                            Artefacto artefacto = new Artefacto(nombreArtefacto);
                            ControladorBaseDatos.agregarArtefacto(idHabitacion, artefacto);

                            inicializarListaArtefactosPorHabitacion();
                            inicializarListViewArtefactos();

                        } else {

                            escribirNombreArtefacto();
                            Toast.makeText(ArtefactosActivity.this, getString(R.string.nombre_vacio), Toast.LENGTH_SHORT).show();
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
}
