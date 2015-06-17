package ar.edu.untref.lp4.proyectodomotica.controladores;


import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.activities.HabitacionesActivity;
import ar.edu.untref.lp4.proyectodomotica.modelos.Artefacto;
import ar.edu.untref.lp4.proyectodomotica.modelos.Habitacion;
import ar.edu.untref.lp4.proyectodomotica.utils.Constantes;

public class ControladorOrdenesDeVoz {

    private ControladorBluetooth controladorBluetooth = ControladorBluetooth.getInstance();
    private ControladorBaseDatos baseDatos;
    private HabitacionesActivity habitacionActivity;
    private Activity prueba;
    private String palabra1, palabra2, palabra3;
    private int cantidadPalabras;


    /**
     * Guarda la habitacionActivity desde donde es llamado
     * @param habitacionActivity
     */
    public ControladorOrdenesDeVoz(HabitacionesActivity habitacionActivity, ControladorBaseDatos baseDatos) {
        this.habitacionActivity = habitacionActivity;
        this.baseDatos = baseDatos;
        this.palabra1 = this.palabra2 = this.palabra3 = "";
    }

    private void noHayOrdenes() {
        Toast.makeText(this.habitacionActivity, R.string.no_orden, Toast.LENGTH_LONG).show();
        Toast.makeText(this.habitacionActivity, R.string.reiterar_comando, Toast.LENGTH_LONG).show();
    }

    /**
     * mensajes que se muestran cuando se registra una sola palabra
     */
    private void unaPalabra () {
        Toast.makeText(this.habitacionActivity, R.string.faltan_palabras, Toast.LENGTH_LONG).show();
        Toast.makeText(this.habitacionActivity, R.string.reiterar_comando, Toast.LENGTH_LONG).show();
    }

    /**
     * mensajes que se muestran cuando se registran tres palabras
     */
    private void tresPalabras () {
        Toast.makeText(this.habitacionActivity, R.string.faltan_o_sobran, Toast.LENGTH_LONG).show();
        Toast.makeText(this.habitacionActivity, R.string.reiterar_comando, Toast.LENGTH_LONG).show();
    }

    /**
     * Separa la orden obtenida en palabras
     * @param cadena
     * @return
     */
    private List<String> obtenerPalabras(String cadena){
        //Lista a devolver.
        List<String> palabras = new LinkedList<String>();
        cadena = cadena.toLowerCase();
        //borra espacios al principio y final de la cadena
        cadena = cadena.trim();
        //indexOf devuelve la posicion del espacio, si no existe devuelve -1
        while(cadena.indexOf(' ') != -1) {
            String palabraAlmacenar = cadena.substring(0, cadena.indexOf(' '));
            palabras.add(palabraAlmacenar);
            cadena = cadena.substring(cadena.indexOf(' '), cadena.length());
            cadena = cadena.trim();
        }
        palabras.add(cadena);
        return palabras;
    }

    /**
     * Carga las palabras separadas en las variables
     * @param palabras
     */
    private void cargarPalabras (List<String> palabras) {
        Iterator<String> iterador= palabras.iterator();
        String cadena;
        int i= 0;
        this.cantidadPalabras = palabras.size();
        while (iterador.hasNext()) {
            cadena= iterador.next();
            switch (i) {
                case 0: this.palabra1 = cadena;
                    break;
                case 1: this.palabra2 = cadena;
                    break;
                case 2: this.palabra3 = cadena;
                    break;
                default: //No hago nada
                    break;
            }
            i++;
        }
    }

    /**
     * Con los metodos anteriores, carga las cuatro palabras en las variables
     * @param cadena
     */
    public void guardarOrden (String cadena){
        cargarPalabras(obtenerPalabras(cadena));
    }

    private Habitacion obtenerHabitacion (List<Habitacion> habitaciones, String nombre){
        Habitacion lugar = null;
        if (habitaciones.size() > 0) {
            if (this.habitacionActivity.nombreHabitacionDisponible(nombre)) {
                Toast.makeText(this.habitacionActivity, R.string.no_existe_habitacion, Toast.LENGTH_LONG).show();
            } else {
                for (Habitacion habitacion : habitaciones) {
                    if (nombre.equals(habitacion.getNombre())) {
                        lugar = habitacion;
                    }
                }
            }
        } else {
            Toast.makeText(this.habitacionActivity, R.string.habitaciones_vacias, Toast.LENGTH_LONG).show();
        }
        return lugar;
    }
    /**
     * La palabra que se pasa por parametro es el nombre de la habitacion
     * Siempre sera la segunda palabra.
     */
    private void abrirHabitacion (List<Habitacion> habitaciones, String nombre){
        Habitacion habitacion = obtenerHabitacion(habitaciones, nombre);
        if (habitacion != null) {
            this.habitacionActivity.abrirArtefactosActivity(habitacion);
        }
    }

    private List<Artefacto> obtenerListadoArtefactos (List<Habitacion> habitaciones, String nombre){
        Habitacion habitacion = obtenerHabitacion(habitaciones, nombre);
        List<Artefacto> lista= this.habitacionActivity.getListaArtefactos(habitacion);
        if (lista.size() == 0){
            Toast.makeText(this.habitacionActivity, R.string.no_hay_artefactos, Toast.LENGTH_LONG).show();
            lista = null;
        }
        return lista;
    }

    /**
     * La palabra que se pasa por parametro es el nombre del artefacto
     * Si la orden es de dos palabras, sera la segunda. Si es de cuatro, la cuarta
     */
    private void encenderArtefacto (List<Habitacion> habitaciones, String lugar, String artefacto){
        if (controladorBluetooth.estaConectado()) {
            List<Artefacto> lista = obtenerListadoArtefactos(habitaciones, lugar);
            if (lista.size() > 0) {
                for (Artefacto aparato : lista) {
                    if (artefacto.equals(aparato.getNombre())) {
                        if (!aparato.isActivo()) {
                            Integer pin = ControladorBaseDatos.getPinArtefacto(aparato);
                            String dato = pin.toString();
                            aparato.setActivo(true);
                            baseDatos.actualizarEstadoArtefacto(aparato);
                            controladorBluetooth.enviarDato(dato + "1");
                            Toast.makeText(this.habitacionActivity, R.string.artefactos_encendidos, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                Toast.makeText(this.habitacionActivity, R.string.no_hay_artefactos, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this.habitacionActivity, R.string.no_conectado, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * La palabra que se pasa por parametro es el nombre del artefacto
     * Si la orden es de dos palabras, sera la segunda. Si es de cuatro, la cuarta
     */
    private void apagarArtefacto (List<Habitacion> habitaciones, String lugar, String artefacto){
        if (controladorBluetooth.estaConectado()) {
            List<Artefacto> lista = obtenerListadoArtefactos(habitaciones, lugar);
            if (lista.size() > 0) {
                for (Artefacto aparato : lista) {
                    if (artefacto.equals(aparato.getNombre())) {
                        if (aparato.isActivo()) {
                            Integer pin = ControladorBaseDatos.getPinArtefacto(aparato);
                            String dato = pin.toString();
                            aparato.setActivo(false);
                            baseDatos.actualizarEstadoArtefacto(aparato);
                            controladorBluetooth.enviarDato(dato + "0");
                            Toast.makeText(this.habitacionActivity, R.string.artefactos_apagados, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                Toast.makeText(this.habitacionActivity, R.string.no_hay_artefactos, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this.habitacionActivity, R.string.no_conectado, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sale del programa o de la habitacion dependiendo de la segunda palabra
     */
    private void salir (String orden) {
        switch (orden){ //para futuras opciones
            case "programa" : this.habitacionActivity.finish();
                break;
            default: Toast.makeText(this.habitacionActivity, R.string.comando_invalido, Toast.LENGTH_LONG).show();
        }
    }

    private void encenderTodoHabitacion (List<Habitacion> habitaciones, String nombre) {
        Habitacion habitacion = obtenerHabitacion(habitaciones, nombre);
        if (nombre.equals(habitacion.getNombre())) {
            this.habitacionActivity.prenderTodo(habitacion);
        } else {
            Toast.makeText(this.habitacionActivity, R.string.no_existe_habitacion, Toast.LENGTH_LONG).show();
        }
    }

    private void encenderTodoCasa (List<Habitacion> habitaciones) {
        if (habitaciones.size() > 0) {
            for (Habitacion habitacion : habitaciones) {
                this.habitacionActivity.prenderTodo(habitacion);
            }
        } else {
            Toast.makeText(this.habitacionActivity, R.string.habitaciones_vacias, Toast.LENGTH_LONG).show();
        }
    }

    private void apagarTodoCasa (List<Habitacion> habitaciones) {
        if (habitaciones.size() > 0) {
            for (Habitacion habitacion : habitaciones) {
                this.habitacionActivity.apagarTodo(habitacion);
            }
        } else {
            Toast.makeText(this.habitacionActivity, R.string.habitaciones_vacias, Toast.LENGTH_LONG).show();
        }
    }

    private void apagarTodoHabitacion (List<Habitacion> habitaciones, String nombre) {
        Habitacion habitacion = obtenerHabitacion(habitaciones, nombre);
        if (nombre.equals(habitacion.getNombre())) {
            this.habitacionActivity.apagarTodo(habitacion);
        } else {
            Toast.makeText(this.habitacionActivity, R.string.no_existe_habitacion, Toast.LENGTH_LONG).show();
        }
    }

    private void realizarOrdenDosPalabras (List<Habitacion> habitaciones, String palabra1, String palabra2) {
        switch (palabra1){
            case Constantes.ABRIR: abrirHabitacion(habitaciones, palabra2);
                break;
            case Constantes.ENCENDER: {
                if (palabra2.equals(Constantes.TODO)){
                    encenderTodoCasa(habitaciones);
                } else {
                    Toast.makeText(this.habitacionActivity, R.string.reiterar_comando, Toast.LENGTH_LONG).show();
                }
            }
                break;
            case Constantes.APAGAR: {
                if (palabra2.equals(Constantes.TODO)) {
                    apagarTodoCasa(habitaciones);
                } else {
                    Toast.makeText(this.habitacionActivity, R.string.reiterar_comando, Toast.LENGTH_LONG).show();
                }
            }
                break;
            case Constantes.SALIR: salir(palabra2);
                break;
            default: Toast.makeText(this.habitacionActivity, R.string.reiterar_comando, Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * Enciende un artefacto de una habitacion
     *
     * @param habitaciones
     * @param palabra1 orden
     * @param palabra2 artefacto
     * @param palabra3 habitacion
     */
    private void realizarOrdenTresPalabras (List<Habitacion> habitaciones, String palabra1, String palabra2, String palabra3) {
        switch (palabra1) {
            case Constantes.ENCENDER: {
                if (palabra2.equals(Constantes.TODO)){
                    encenderTodoHabitacion(habitaciones, palabra3);
                } else {
                    encenderArtefacto(habitaciones,palabra3,palabra2);
                }
            }
                break;
            case Constantes.APAGAR: {
                if (palabra2.equals(Constantes.TODO)){
                    apagarTodoHabitacion(habitaciones, palabra3);
                } else {
                    apagarArtefacto(habitaciones,palabra3,palabra2);
                }
            }
                break;
            default:
                tresPalabras();
                break;
        }
    }

    public void analizarOrden(List<Habitacion> habitaciones) {
        switch (this.cantidadPalabras) {
            case 0:
                noHayOrdenes();
                break;
            case 1: unaPalabra();
                break;
            case 2: realizarOrdenDosPalabras(habitaciones, this.palabra1, this.palabra2);
                break;
            case 3: realizarOrdenTresPalabras(habitaciones,this.palabra1,this.palabra2,this.palabra3);
                break;
            default: Toast.makeText(this.habitacionActivity, R.string.muchas_palabras, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
