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
    private String orden, nombreHabitacion, nombreArtefacto;

    public ControladorDeVozV2(HabitacionesActivity habitacionActivity) {
        this.habitacionActivity = habitacionActivity;
    }

    private void setOrden (String orden) {
        this.orden = orden;
    }

    private void setNombreHabitacion (String nombreHabitacion) {
        this.nombreHabitacion = nombreHabitacion;
    }

    private void setNombreArtefacto (String nombreArtefacto) {
        this.nombreArtefacto = nombreArtefacto;
    }

    private String getOrden () {
        return this.orden;
    }

    private String getNombreHabitacion () {
        return this.nombreHabitacion;
    }

    private String getNombreArtefacto () {
        return this.nombreArtefacto;
    }
    /**
     * Devuelve la habitacion que se pidio en caso de que exista.
     *
     * @param habitaciones Lista de las Habitaciones
     * @param nombre Nombre de la habotacion a ser buscada
     * @return La habitacion pedida
     */
    private Habitacion obtenerHabitacion(List<Habitacion> habitaciones, String nombre) {
        Habitacion lugar = null;
        if (habitaciones.size() > 0) {
            for (Habitacion habitacion : habitaciones) {
                if (nombre.equals(habitacion.getNombre())) {
                    lugar = habitacion;
                }
            }
        } else {
            Toast.makeText(this.habitacionActivity, R.string.habitaciones_vacias, Toast.LENGTH_SHORT).show();
        }
        return lugar;
    }

    /**
     * Deveulve una lista de Artefactos de la Habitacion pedida.
     *
     * @param habitaciones Lista de Habitaciones
     * @param nombre Nombre de la Habitacion
     * @return Listado de Artefactos
     */
    private List<Artefacto> obtenerListadoArtefactos(List<Habitacion> habitaciones, String nombre) {
        Habitacion habitacion = obtenerHabitacion(habitaciones, nombre);
        List<Artefacto> lista = this.habitacionActivity.getListaArtefactos(habitacion);
        return lista;
    }

    /**
     * Guarda en la variable privada la orden hecha por el usuario
     * sin importar donde se encuentre del String.
     *
     * @param cadena orden dada por el usuario
     */
    private void obtenerOrden (String cadena) {
        if (cadena.contains(Constantes.ABRIR)) {
            setOrden(Constantes.ABRIR);
        }
        if (cadena.contains(Constantes.ENCENDER)) {
            setOrden(Constantes.ENCENDER);
        }
        if (cadena.contains(Constantes.APAGAR)) {
            setOrden(Constantes.APAGAR);
        }
        if (cadena.contains(Constantes.SALIR)){
            setOrden(Constantes.SALIR);
        }
    }

    /**
     * Guarda en la variable privada el nombre de la habitacion
     * sin importar donde se encuentre del String.
     *
     * @param habitaciones Listado de Habitaciones
     * @param cadena Orden dada por el usuario
     */
    private void obtenerNombreHabitacion (List<Habitacion> habitaciones, String cadena) {
        String nombre;
        for (Habitacion habitacion : habitaciones) {
            nombre = habitacion.getNombre().toLowerCase();
            if (cadena.contains(nombre)) {
                setNombreHabitacion(habitacion.getNombre());
            }
        }
    }

    /**
     * Guarda en la variable privada el nombre del artefacto
     * sin importar donde se encuentre del String.
     *
     * @param habitaciones Listado de Habitaciones
     * @param cadena orden ingresada por el usuario
     */
    private void obtenerNombreArtefacto (List<Habitacion> habitaciones, String cadena) {
        String nombre;
        for (Habitacion habitacion : habitaciones) {
            List<Artefacto> lista = obtenerListadoArtefactos(habitaciones, habitacion.getNombre());
            if (lista.size() > 0) {
                for (Artefacto artefacto : lista) {
                    nombre = artefacto.getNombre().toLowerCase();
                    if (cadena.contains(nombre)) {
                        setNombreArtefacto(artefacto.getNombre());
                    }
                }
            }
        }
    }

    /**
     * Analiza la orden dada por el usuario y guarda la informacion en sus respectivas
     * variables.
     *
     * @param habitaciones Listado de Habitaciones
     * @param cadena orden ingresada por el usuario
     */
    private void analizarOrden (List<Habitacion> habitaciones, String cadena) {
        obtenerOrden(cadena);
        obtenerNombreHabitacion(habitaciones, cadena);
        obtenerNombreArtefacto(habitaciones, cadena);
    }

    /**
     * Cierra la aplicacion.
     */
    private void salirPrograma () {
        this.habitacionActivity.finish();
    }

    /**
     * Abre el activity de la habitacion mencionada.
     *
     * @param habitaciones Listado de Habitaciones
     * @param cadena orden ingresada por el usuario
     */
    private void abrirHabitacion(List<Habitacion> habitaciones, String cadena) {
        Habitacion habitacion;
        if (getNombreHabitacion().equals(Constantes.VACIO)) {
            Toast.makeText(this.habitacionActivity, R.string.faltan_palabras, Toast.LENGTH_SHORT).show();
        } else {
            habitacion = obtenerHabitacion(habitaciones, getNombreHabitacion());
            this.habitacionActivity.abrirArtefactosActivity(habitacion);
        }
    }

    /**
     * Enciende todos los artefactos de la habitacion especificada.
     *
     * @param habitaciones Listado de Habitaciones
     */
    private void encenderTodoHabitacion(List<Habitacion> habitaciones) {
        Habitacion habitacion = obtenerHabitacion(habitaciones, getNombreHabitacion());
        this.habitacionActivity.prenderTodo(habitacion);
    }

    /**
     * Enceinde los artefactos de toda la casa.
     *
     * @param habitaciones Listado de Habitaciones
     */
    private void encenderTodoCasa(List<Habitacion> habitaciones) {
        for (Habitacion habitacion : habitaciones) {
            this.habitacionActivity.prenderTodo(habitacion);
        }
    }

    /**
     * Enceinde el artefacto especificado.
     *
     * @param habitaciones Listado de Habitaciones
     */
    private void encenderArtefacto(List<Habitacion> habitaciones) {
        if (controladorBluetooth.estaConectado()) {
            List<Artefacto> lista = obtenerListadoArtefactos(habitaciones, getNombreHabitacion());
            if (lista.size() > 0) {
                for (Artefacto artefacto : lista) {
                    if (getNombreArtefacto().equals(artefacto.getNombre())) {
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
     * Metodo que evalua que orden de Encender se pidio.
     *
     * @param habitaciones
     * @param cadena
     */
    private void encender (List<Habitacion> habitaciones, String cadena) {
        if (getNombreArtefacto().equals(Constantes.VACIO) && getNombreHabitacion().equals(Constantes.VACIO) &&
                cadena.contains(Constantes.TODO)) {
            encenderTodoCasa(habitaciones);
        } else {
            if (getNombreArtefacto().equals(Constantes.VACIO) && cadena.contains(Constantes.TODO)) {
                encenderTodoHabitacion(habitaciones);
            } else {
                if (cadena.contains(getNombreArtefacto())) {
                    encenderArtefacto(habitaciones);
                }
            }
        }
    }

    /**
     * Apaga todos los artefactos de la casa.
     *
     * @param habitaciones Listado de Habitaciones
     */
    private void apagarTodoCasa(List<Habitacion> habitaciones) {
        for (Habitacion habitacion : habitaciones) {
            this.habitacionActivity.apagarTodo(habitacion);
        }
    }

    /**
     * Apaga todos los artefactos de la habitacion especificada.
     *
     * @param habitaciones Listado de Habitaciones
     */
    private void apagarTodoHabitacion(List<Habitacion> habitaciones) {
        Habitacion habitacion = obtenerHabitacion(habitaciones, getNombreHabitacion());
        this.habitacionActivity.apagarTodo(habitacion);
    }

    /**
     * Apaga el artefacto especificado.
     *
     * @param habitaciones Listado de Habitaciones
     */
    private void apagarArtefacto(List<Habitacion> habitaciones) {
        if (controladorBluetooth.estaConectado()) {
            List<Artefacto> lista = obtenerListadoArtefactos(habitaciones, getNombreHabitacion());
            if (lista.size() > 0) {
                for (Artefacto aparato : lista) {
                    if (getNombreArtefacto().equals(aparato.getNombre())) {
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
     * Metodo que evalua que orden de Apagar se pidio.
     *
     * @param habitaciones
     * @param cadena
     */
    private void apagar (List<Habitacion> habitaciones, String cadena) {
        if (getNombreHabitacion().equals(Constantes.VACIO) && cadena.contains(Constantes.TODO)) {
            apagarTodoCasa(habitaciones);
            Toast.makeText(this.habitacionActivity, "Prueba...", Toast.LENGTH_SHORT).show();
        } else {
            if (getNombreArtefacto().equals(Constantes.VACIO) && cadena.contains(Constantes.TODO)) {
                apagarTodoHabitacion(habitaciones);
                Toast.makeText(this.habitacionActivity, "Prueba1...", Toast.LENGTH_SHORT).show();
            } else {
                if (cadena.contains(getNombreArtefacto())) {
                    apagarArtefacto(habitaciones);
                    Toast.makeText(this.habitacionActivity, "Prueba2...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Analiza la orden del usuario y actua en consecuencia.
     *
     * @param habitaciones Listado de Habitaciones
     * @param cadena Orden ingresada por el usuario
     */
    public void ejecutarOrden (List<Habitacion> habitaciones, String cadena) {
        if (habitaciones.size() > 0) {
            setOrden(Constantes.VACIO);
            setNombreHabitacion(Constantes.VACIO);
            setNombreArtefacto(Constantes.VACIO);
            analizarOrden(habitaciones, cadena);
            switch (getOrden()) {
                case Constantes.ABRIR:
                    abrirHabitacion(habitaciones, cadena);
                    break;
                case Constantes.ENCENDER:
                    encender(habitaciones, cadena);
                break;
                case Constantes.APAGAR:
                    apagar(habitaciones, cadena);
                break;
                case Constantes.SALIR:
                    salirPrograma();
                break;
                default:
                    Toast.makeText(this.habitacionActivity, R.string.reiterar_comando, Toast.LENGTH_SHORT).show();
                break;
            }
        } else {
            Toast.makeText(this.habitacionActivity, R.string.habitaciones_vacias, Toast.LENGTH_SHORT).show();
        }
    }
}