package ar.edu.untref.lp4.proyectodomotica.controladores;


import android.app.Activity;
import android.widget.Toast;

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
    private HabitacionesActivity habitacionActivity;
    private String palabra1, palabra2, palabra3, orden;
    private int cantidadPalabras;


    /**
     * Guarda la habitacionActivity desde donde es llamado
     */
    public ControladorOrdenesDeVoz(HabitacionesActivity habitacionActivity) {

        this.habitacionActivity = habitacionActivity;
        this.palabra1 = this.palabra2 = this.palabra3 = "";
    }

    private void noHayOrdenes() {
        Toast.makeText(this.habitacionActivity, R.string.no_orden, Toast.LENGTH_SHORT).show();
    }

    /**
     * mensajes que se muestran cuando se registra una sola palabra
     */
    private void unaPalabra() {
        Toast.makeText(this.habitacionActivity, R.string.faltan_palabras, Toast.LENGTH_SHORT).show();
    }

    /**
     * mensajes que se muestran cuando se registran tres palabras
     */
    private void tresPalabras() {
        Toast.makeText(this.habitacionActivity, R.string.faltan_o_sobran, Toast.LENGTH_SHORT).show();
    }

    /**
     * Separa la orden obtenida en palabras
     */
    private List<String> obtenerPalabras(String cadena) {

        //Lista a devolver.
        List<String> palabras = new LinkedList<String>();

        cadena = cadena.toLowerCase();

        //borra espacios al principio y final de la cadena
        cadena = cadena.trim();

        //indexOf devuelve la posicion del espacio, si no existe devuelve -1
        while (cadena.indexOf(' ') != -1) {

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
     */
    private void cargarPalabras(List<String> palabras) {

        Iterator<String> iterador = palabras.iterator();
        String cadena;
        int i = 0;
        this.cantidadPalabras = palabras.size();

        while (iterador.hasNext()) {

            cadena = iterador.next();

            switch (i) {

                case 0:
                    this.palabra1 = cadena;
                    break;

                case 1:
                    this.palabra2 = cadena;
                    break;

                case 2:
                    this.palabra3 = cadena;
                    break;

                default:
                    break;
            }

            i++;
        }
    }

    /**
     * Con los metodos anteriores, carga las cuatro palabras en las variables
     */
    public void guardarOrden(String cadena) {
        cargarPalabras(obtenerPalabras(cadena));
    }

    public void guardarOrden2(String cadena) {
        orden = cadena;
    }

    private Habitacion obtenerHabitacion(List<Habitacion> habitaciones, String nombre) {

        Habitacion lugar = null;

        if (habitaciones.size() > 0) {

            if (this.habitacionActivity.nombreHabitacionDisponible(nombre)) {

                Toast.makeText(this.habitacionActivity, R.string.no_existe_habitacion, Toast.LENGTH_SHORT).show();

            } else {

                for (Habitacion habitacion : habitaciones) {

                    if (nombre.equals(habitacion.getNombre())) {

                        lugar = habitacion;
                    }
                }
            }

        } else {

            Toast.makeText(this.habitacionActivity, R.string.habitaciones_vacias, Toast.LENGTH_SHORT).show();
        }

        return lugar;
    }

    /**
     * La palabra que se pasa por parametro es el nombre de la habitacion
     * Siempre sera la segunda palabra.
     */
    private void abrirHabitacion(List<Habitacion> habitaciones, String nombre) {

        Habitacion habitacion = obtenerHabitacion(habitaciones, nombre);

        if (habitacion != null) {

            this.habitacionActivity.abrirArtefactosActivity(habitacion);
        }
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

    /**
     * La palabra que se pasa por parametro es el nombre del artefacto
     * Si la orden es de dos palabras, sera la segunda. Si es de cuatro, la cuarta
     */
    private void encenderArtefacto(List<Habitacion> habitaciones, String lugar, String nombreArtefacto) {

        if (controladorBluetooth.estaConectado()) {

            List<Artefacto> lista = obtenerListadoArtefactos(habitaciones, lugar);

            if (lista.size() > 0) {

                for (Artefacto artefacto : lista) {

                    if (nombreArtefacto.equals(artefacto.getNombre())) {

                        if (!artefacto.isActivo()) {

                            Integer pin = ControladorBaseDatos.getPinArtefacto(artefacto);

                            String dato = pin.toString();

                            artefacto.setActivo(true);

                            ControladorBaseDatos.actualizarEstadoArtefacto(artefacto);
                            controladorBluetooth.enviarDato(dato + "1");

                            Toast.makeText(this.habitacionActivity, R.string.se_encendio, Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(this.habitacionActivity, R.string.estaba_encendido, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            } else {

                Toast.makeText(this.habitacionActivity, R.string.no_hay_artefactos, Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText(this.habitacionActivity, R.string.no_conectado, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * La palabra que se pasa por parametro es el nombre del artefacto
     * Si la orden es de dos palabras, sera la segunda. Si es de cuatro, la cuarta
     */
    private void apagarArtefacto(List<Habitacion> habitaciones, String lugar, String artefacto) {

        if (controladorBluetooth.estaConectado()) {

            List<Artefacto> lista = obtenerListadoArtefactos(habitaciones, lugar);

            if (lista.size() > 0) {

                for (Artefacto aparato : lista) {

                    if (artefacto.equals(aparato.getNombre())) {

                        if (aparato.isActivo()) {

                            Integer pin = ControladorBaseDatos.getPinArtefacto(aparato);
                            String dato = pin.toString();
                            aparato.setActivo(false);

                            ControladorBaseDatos.actualizarEstadoArtefacto(aparato);
                            controladorBluetooth.enviarDato(dato + "0");

                            Toast.makeText(this.habitacionActivity, R.string.se_apago, Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(this.habitacionActivity, R.string.estaba_apagado, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            } else {

                Toast.makeText(this.habitacionActivity, R.string.no_hay_artefactos, Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText(this.habitacionActivity, R.string.no_conectado, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sale del programa o de la habitacion dependiendo de la segunda palabra
     */
    private void salir(String orden) {

        switch (orden) { //para futuras opciones

            case "programa":

                this.habitacionActivity.finish();
                break;

            default:
                Toast.makeText(this.habitacionActivity, R.string.comando_invalido, Toast.LENGTH_SHORT).show();
        }
    }

    private void encenderTodoHabitacion(List<Habitacion> habitaciones, String nombre) {

        Habitacion habitacion = obtenerHabitacion(habitaciones, nombre);

        if (nombre.equals(habitacion.getNombre())) {

            this.habitacionActivity.prenderTodo(habitacion);

        } else {

            Toast.makeText(this.habitacionActivity, R.string.no_existe_habitacion, Toast.LENGTH_SHORT).show();
        }
    }

    private void encenderTodoCasa(List<Habitacion> habitaciones) {

        if (habitaciones.size() > 0) {

            for (Habitacion habitacion : habitaciones) {

                this.habitacionActivity.prenderTodo(habitacion);
            }

        } else {

            Toast.makeText(this.habitacionActivity, R.string.habitaciones_vacias, Toast.LENGTH_SHORT).show();
        }
    }

    private void apagarTodoCasa(List<Habitacion> habitaciones) {

        if (habitaciones.size() > 0) {

            for (Habitacion habitacion : habitaciones) {


                this.habitacionActivity.apagarTodo(habitacion);
            }

        } else {

            Toast.makeText(this.habitacionActivity, R.string.habitaciones_vacias, Toast.LENGTH_SHORT).show();
        }
    }

    private void apagarTodoHabitacion(List<Habitacion> habitaciones, String nombre) {

        Habitacion habitacion = obtenerHabitacion(habitaciones, nombre);

        if (nombre.equals(habitacion.getNombre())) {

            this.habitacionActivity.apagarTodo(habitacion);

        } else {

            Toast.makeText(this.habitacionActivity, R.string.no_existe_habitacion, Toast.LENGTH_SHORT).show();
        }
    }

    private void realizarOrdenDosPalabras(List<Habitacion> habitaciones, String palabra1, String palabra2) {

        switch (palabra1) {

            case Constantes.ABRIR:
                abrirHabitacion(habitaciones, palabra2);
                break;

            case Constantes.ENCENDER: {

                if (palabra2.equals(Constantes.TODO)) {
                    encenderTodoCasa(habitaciones);
                } else {
                    Toast.makeText(this.habitacionActivity, R.string.reiterar_comando, Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case Constantes.APAGAR: {

                if (palabra2.equals(Constantes.TODO)) {
                    apagarTodoCasa(habitaciones);
                } else {
                    Toast.makeText(this.habitacionActivity, R.string.reiterar_comando, Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case Constantes.SALIR:
                salir(palabra2);
                break;

            default:
                Toast.makeText(this.habitacionActivity, R.string.reiterar_comando, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Enciende un artefacto de una habitacion
     */
    private void realizarOrdenTresPalabras(List<Habitacion> habitaciones, String palabra1, String palabra2, String palabra3) {

        switch (palabra1) {

            case Constantes.ENCENDER: {
                if (palabra2.equals(Constantes.TODO)) {
                    encenderTodoHabitacion(habitaciones, palabra3);
                } else {
                    encenderArtefacto(habitaciones, palabra3, palabra2);
                }
            }
            break;

            case Constantes.APAGAR: {

                if (palabra2.equals(Constantes.TODO)) {
                    apagarTodoHabitacion(habitaciones, palabra3);
                } else {
                    apagarArtefacto(habitaciones, palabra3, palabra2);
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

            case 1:
                unaPalabra();
                break;

            case 2:
                realizarOrdenDosPalabras(habitaciones, this.palabra1, this.palabra2);
                break;

            case 3:
                realizarOrdenTresPalabras(habitaciones, this.palabra1, this.palabra2, this.palabra3);
                break;

            default:
                Toast.makeText(this.habitacionActivity, R.string.muchas_palabras, Toast.LENGTH_SHORT).show();
                break;
        }
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
