package ar.edu.untref.lp4.proyectodomotica.controladores;

import android.widget.Toast;

import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.activities.HabitacionesActivity;
import ar.edu.untref.lp4.proyectodomotica.modelos.Artefacto;
import ar.edu.untref.lp4.proyectodomotica.modelos.Habitacion;
import ar.edu.untref.lp4.proyectodomotica.utils.Constantes;

/**
 * Created by HoracioAlejo on 18/06/2015.
 */
public class ControladorDeVozV2 {

    private ControladorBluetooth controladorBluetooth = ControladorBluetooth.getInstance();
    private HabitacionesActivity habitacionActivity;
    private String orden, habitacion, artefacto;
    private int cantidadPalabras;


    /**
     * Guarda la habitacionActivity desde donde es llamado
     */
    public ControladorDeVozV2(HabitacionesActivity habitacionActivity) {
        this.habitacionActivity = habitacionActivity;
        this.orden = this.habitacion = this.artefacto = null;
    }

    private List<Artefacto> obtenerListadoArtefactos(List<Habitacion> habitaciones, String nombre) {

        Habitacion habitacion = obtenerHabitacion(habitaciones, nombre);

        List<Artefacto> lista = this.habitacionActivity.getListaArtefactos(habitacion);

        if (lista.size() == 0) {

            Toast.makeText(this.habitacionActivity, R.string.no_hay_artefactos, Toast.LENGTH_SHORT).show();
            lista = null;
        }

        return lista;
    }

    public boolean encenderVerdadero (String cadena) {
        return cadena.contains(Constantes.ENCENDER);
    }

    public boolean apagarVerdadero (String cadena) {
        return cadena.contains(Constantes.APAGAR);
    }

    public boolean existeHabitacion (List<Habitacion> habitaciones, String cadena) {
        boolean encontrado = false;
        String nombre;
        for (Habitacion habitacion : habitaciones) {
            nombre = habitacion.getNombre().toLowerCase();
            if (cadena.contains(nombre)) {
                encontrado = true;
            }
        }
        return encontrado;
    }

    public boolean existeArtefacto (List<Habitacion> habitaciones, String cadena) {
        boolean encontrado = false;
        List<Artefacto> lista;
        String nombre;
        for (Habitacion habitacion : habitaciones) {
            lista = obtenerListadoArtefactos(habitaciones, habitacion.getNombre());
            for (Artefacto artefacto : lista) {
                nombre = artefacto.getNombre().toLowerCase();
                if (cadena.contains(nombre)) {
                    encontrado = true;
                }
            }
        }
        return encontrado;
    }

    public void analizarOrden2 (List<Habitacion> habitaciones, String cadena) {
        if (encenderVerdadero(cadena)) {
            if (existeHabitacion(habitaciones, cadena)){

            }
        }
    }
}
