package ar.edu.untref.lp4.proyectodomotica.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.adapters.ListViewArtefactosAdapter;
import ar.edu.untref.lp4.proyectodomotica.baseDatos.BaseDatos;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBaseDatos;
import ar.edu.untref.lp4.proyectodomotica.modelos.Artefacto;
import ar.edu.untref.lp4.proyectodomotica.utils.Constantes;

public class ArtefactosActivity extends Activity {

    private static final String TAG = ArtefactosActivity.class.getSimpleName();

    private List<Artefacto> artefactos;
    private ListViewArtefactosAdapter artefactosAdapter;

    private FloatingActionButton botonAgregarHabitacion;
    private FloatingActionButton botonHablar;
    public static String nombreHabitacion;
    private int idHabitacion;

    private BaseDatos bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artefactos);

        Logger.init(TAG);
        setNombreHabitacion();
        setIdHabitacion();

        TextView habitacion = (TextView) findViewById(R.id.nombre_habitacion);
        habitacion.setText(nombreHabitacion);

        inicializarBotonAgregar();
        inicializarBotonHablar();
        inicializarListaArtefactosPorHabitacion();
        inicializarListViewArtefactos();
    }

    public void setNombreHabitacion () {
        nombreHabitacion = getIntent().getExtras().getString(Constantes.NOMBRE_HABITACION);
    }

    public String getNombreHabitacion () {
        return nombreHabitacion;
    }

    public void setIdHabitacion () {
        idHabitacion = getIntent().getExtras().getInt(Constantes.ID_HABITACION);
    }

    public int getIdHabitacion () {
        return idHabitacion;
    }

    /**
     * Actualiza la lista de artefactos
     */
    public void refresh() {

        inicializarListaArtefactosPorHabitacion();
        inicializarListViewArtefactos();
    }

    /**
     * Inicializa solo el boton de agregar
     */
    private void inicializarBotonAgregar() {

        botonAgregarHabitacion = (FloatingActionButton) findViewById(R.id.agregar_artefacto_boton);
        botonAgregarHabitacion.setSize(FloatingActionButton.SIZE_NORMAL);
        botonAgregarHabitacion.setIcon(R.drawable.cruz);
        botonAgregarHabitacion.setOnClickListener(agregarArtefactoListener);
    }

    private void inicializarBotonHablar() {

        botonHablar = (FloatingActionButton) findViewById(R.id.boton_hablar);
        botonHablar.setSize(FloatingActionButton.SIZE_NORMAL);
        botonHablar.setIcon(R.drawable.microfono);
        botonHablar.setOnClickListener(hablar);
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
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private View.OnClickListener agregarArtefactoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            escribirNombreArtefacto();
        }
    };

    public void escribirNombreArtefacto() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.nuevo_artefacto);
        alertDialog.setMessage(R.string.nombre_artefacto);

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

                        String nombreArtefacto = editText.getText().toString();

                        if (nombreArtefacto.isEmpty()) {

                            escribirNombreArtefacto();
                            Toast.makeText(ArtefactosActivity.this, getString(R.string.nombre_vacio), Toast.LENGTH_SHORT).show();

                        } else {

                            if (nombreArtefactoDisponible(nombreArtefacto)) {

                                Artefacto artefacto = new Artefacto(nombreArtefacto);

                                ControladorBaseDatos.agregarArtefacto(idHabitacion, artefacto);

                                refresh();

                            } else {


                                Toast.makeText(ArtefactosActivity.this, getString(R.string.artefacto_existente), Toast.LENGTH_SHORT).show();
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

    public boolean nombreArtefactoDisponible(String nombre) {

        boolean nombreDisponible = true;

        if (artefactos.size() > 0) {

            for (Artefacto artefacto : artefactos) {

                if (nombre.equals(artefacto.getNombre())) {

                    nombreDisponible = false;
                }
            }
        }

        return nombreDisponible;
    }

    public void borrarArtefacto(final Artefacto artefacto) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.eliminar_artefacto_titulo));
        alertDialog.setMessage(getString(R.string.eliminar_artefacto_mensaje));


        alertDialog.setPositiveButton(getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ControladorBaseDatos.eliminarArtefacto(artefacto);
                        refresh();
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

    public void editarArtefacto(final Artefacto artefacto) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.editar);
        alertDialog.setMessage(R.string.nuevo_nombre_artefacto);

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

                        String nombreArtefacto = editText.getText().toString();

                        if (nombreArtefactoDisponible(nombreArtefacto)) {

                            artefacto.setNombre(nombreArtefacto);

                            ControladorBaseDatos.actualizarNombreArtefacto(artefacto);

                            refresh();

                        } else {

                            escribirNombreArtefacto();
                            Toast.makeText(ArtefactosActivity.this, getString(R.string.artefacto_existente), Toast.LENGTH_SHORT).show();
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

    public boolean verificarExisteReconocimientoVoz () {
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private View.OnClickListener hablar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (verificarExisteReconocimientoVoz()) {

            } else {
                Toast.makeText(ArtefactosActivity.this, getString(R.string.no_esta_presente), Toast.LENGTH_SHORT).show();
            }
        }
    };


}
