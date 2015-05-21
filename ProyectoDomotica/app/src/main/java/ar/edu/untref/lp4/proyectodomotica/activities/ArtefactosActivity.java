package ar.edu.untref.lp4.proyectodomotica.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBluetooth;
import ar.edu.untref.lp4.proyectodomotica.modelos.Artefacto;

public class ArtefactosActivity extends Activity {

    private static final String TAG = ArtefactosActivity.class.getSimpleName();
    private static final String NOMBRE_HABITACION = "nombre_habitacion";

    private List<Artefacto> artefactos;
    private ControladorBluetooth controladorBluetooth = ControladorBluetooth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artefactos);

        Logger.init(TAG);

        String nombreHabitacion = getIntent().getExtras().getString(NOMBRE_HABITACION);

        TextView habitacion = (TextView) findViewById(R.id.nombre_habitacion);
        habitacion.setText(nombreHabitacion);

        inicializarListaArtefactosPorHabitacion(nombreHabitacion);
        inicializarListViewArtefactos();
    }

    /**
     * Agrega los artefactos correspondientes de acuerdo a la habitacion seleccionada
     */
    private void inicializarListaArtefactosPorHabitacion(String habitacion) {

        this.artefactos = new ArrayList<>();

        /*
        Los artefactos van almacenarse y leerse desde donde esten almacenados
        Por ejemplo, de una base de datos: this.artefactos.addAll(baseDatos.getArtefactosPorHabitacion("Cocina"));
        */

        switch (habitacion) {

            case "Cocina":

                this.artefactos.add(new Artefacto("Lampara"));
                this.artefactos.add(new Artefacto("Cafetera"));
                this.artefactos.add(new Artefacto("Licuadora"));

                break;

            case "Habitacion":

                this.artefactos.add(new Artefacto("Lampara"));
                this.artefactos.add(new Artefacto("Ventilador"));
                this.artefactos.add(new Artefacto("Persiana"));

                break;

            case "Living":

                this.artefactos.add(new Artefacto("Lampara"));
                this.artefactos.add(new Artefacto("Aire Acondicionado"));
                this.artefactos.add(new Artefacto("Televisor"));
                this.artefactos.add(new Artefacto("Equipo de Audio"));

                break;

            case "Baño":

                this.artefactos.add(new Artefacto("Lampara"));
                this.artefactos.add(new Artefacto("Ventilacion"));

                break;

            case "Garage":

                this.artefactos.add(new Artefacto("Lampara"));
                this.artefactos.add(new Artefacto("Porton"));

                break;
        }
    }

    /**
     * Crea el ListView con la lista de artefactos previamente creada
     */
    private void inicializarListViewArtefactos() {

        ListView listView = (ListView) findViewById(R.id.lista_artefactos);

        String[] artefactosArray = new String[artefactos.size()];

        for (int i = 0; i < artefactos.size(); i++) {

            artefactosArray[i] = artefactos.get(i).getNombre();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, artefactosArray);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(onItemClickListener);
    }


    /**
     * Setea el comportamiento al clickear un artefacto
     */

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(controladorBluetooth.estaConectado()) {

                if (artefactos.get(position).getNombre().equals("Lampara")) {

                    controladorBluetooth.enviarDato("1");

                } else {

                    controladorBluetooth.enviarDato("0");
                }

            } else {

                Toast.makeText(getApplicationContext(), getString(R.string.verificar_conexion), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
