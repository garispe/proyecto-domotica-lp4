package ar.edu.untref.lp4.proyectodomotica.controladores;

import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.activities.ArtefactosActivity;
import ar.edu.untref.lp4.proyectodomotica.activities.HabitacionesActivity;
import ar.edu.untref.lp4.proyectodomotica.modelos.Artefacto;
import ar.edu.untref.lp4.proyectodomotica.modelos.Habitacion;

public class ControladorOrdenesDeVoz {

    private Activity activity;
    private String palabra1, palabra2, palabra3, palabra4;
    private int cantidadPalabras;
    private HabitacionesActivity habitacionesActivity;
    private ArtefactosActivity artefactosActivity;

    /**
     * Guarda la activity desde donde es llamado
     * @param activity
     */
    public ControladorOrdenesDeVoz(Activity activity) {
        this.activity = activity;
        palabra1 = palabra2 = palabra3 = palabra4 = "";
    }

    private void noHayOrdenes() {
        Toast.makeText(this.activity, "No hay ordenes que interpretar", Toast.LENGTH_LONG).show();
        Toast.makeText(this.activity, "Reiterar orden", Toast.LENGTH_LONG).show();
    }

    /**
     * mensajes que se muestran cuando se registra una sola palabra
     */
    private void unaPalabra () {
        Toast.makeText(this.activity, "Faltan palabras para interpretar", Toast.LENGTH_LONG).show();
        Toast.makeText(this.activity, "Reiterar orden", Toast.LENGTH_LONG).show();
    }

    /**
     * mensajes que se muestran cuando se registran tres palabras
     */
    private void tresPalabras () {
        Toast.makeText(this.activity, "Sobran o faltan palabras", Toast.LENGTH_LONG).show();
        Toast.makeText(this.activity, "Reiterar orden", Toast.LENGTH_LONG).show();
    }

    /**
     * Caraga las palabras del comando de voz en cuatro strings
     */
    /*
    public void cargarPalabras(String comando){
        int posicionActual=0;
        String [] cadena = new String[4];
        String palabraActual="";
        for (int i= 0; i<cadena.length; i++) { //hola la la la la la la
            cadena[i] = "";
        }
        for (int i = 0; i<comando.length() && posicionActual<cadena.length; i++) {
            char c = comando.charAt(i);
            if (c == ' ' ){
                cadena[posicionActual]=palabraActual;
                palabraActual="";
                posicionActual++;
            }
            else{
                palabraActual=palabraActual + c;
            }
        }
        //Cargo la cantidad de palaras que voy a tener cargadas
        cantidadPalabras = posicionActual++;
        for (int i=0; !cadena[i].equals("") && i<cadena.length; i++)
            switch (i) {
                case 0:
                    palabra1 = cadena[i];
                    break;
                case 1: palabra2 = cadena[i];
                    break;
                case 2: palabra3 = cadena[i];
                    break;
                case 3: palabra4 = cadena[i];
                    break;
                default:
                    break;
            }
    }
    */

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
        cantidadPalabras = palabras.size();
        while (iterador.hasNext()) {
            cadena= iterador.next();
            switch (i) {
                case 0:
                    palabra1 = cadena;
                    break;
                case 1: palabra2 = cadena;
                    break;
                case 2: palabra3 = cadena;
                    break;
                case 3: palabra4 = cadena;
                    break;
                default:
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
        String prueba = palabra1 + ' ' + palabra2 + ' ' +  palabra3 + ' ' +  palabra4;
        Toast.makeText(this.activity, prueba, Toast.LENGTH_LONG).show();
    }
    /**
     * La palabra que se pasa por parametro es el nombre de la habitacion
     * Siempre sera la segunda palabra.
     */
    private void abrirHabitacion (List<Habitacion> habitaciones, String palabra){
        if (habitaciones.size() > 0) {
            if (!habitacionesActivity.nombreHabitacionDisponible(palabra)) {
                for (Habitacion habitacion : habitaciones) {
                    habitacionesActivity.abrirArtefactosActivity(habitacion);
                }
            } else {
                Toast.makeText(this.activity, "No existe habitacion con dicho nombre", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this.activity, "No se cargo ninguna habitacion", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * La palabra que se pasa por parametro es el nombre del artefacto
     * Si la orden es de dos palabras, sera la segunda. Si es de cuatro, la cuarta
     */
    private void encenderArtefacto (List<Artefacto> artefactos, String palabra){
        if (artefactos.size() > 0) {
            if (!artefactosActivity.nombreArtefactoDisponible(palabra)){
                for (Artefacto artefacto : artefactos) {
                    //Deberia de llamar al metodo que enciende el artefacto
                    Toast.makeText(this.activity, this.activity.getString(R.string.proximamente), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this.activity, "No existe dicho artefacto", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this.activity, "No hay artefactos cargados", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * La palabra que se pasa por parametro es el nombre del artefacto
     * Si la orden es de dos palabras, sera la segunda. Si es de cuatro, la cuarta
     */
    private void apagarArtefacto (List<Artefacto> artefactos, String palabra2){
        if (artefactos.size() > 0) {
            if (!artefactosActivity.nombreArtefactoDisponible(palabra2)){
                for (Artefacto artefacto : artefactos) {
                    if (palabra2.equals(artefacto.getNombre())) {
                        Toast.makeText(this.activity, this.activity.getString(R.string.proximamente), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(this.activity, "No existe dicho artefacto", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this.activity, "No hay artefactos cargados", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Sale del programa o de la habitacion dependiendo de la segunda palabra
     */
    private void salir (String palabra) {
        if (palabra.equals(artefactosActivity.getNombreHabitacion())) {
            artefactosActivity.finish();
        } else {
            if (palabra.equals("programa")){
                try {
                    artefactosActivity.finish();
                } catch (Exception e) {
                    //No hago nada
                }
                habitacionesActivity.finish();
            } else {
                Toast.makeText(this.activity, "Comando invalido", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void encenderTodoHabitacion (List<Habitacion> habitaciones) {
        String nombre = artefactosActivity.getNombreHabitacion();
        if (habitaciones.size() > 0) {
            for (Habitacion habitacion : habitaciones) {
                if (nombre.equals(habitacion.getNombre())) {
                    habitacionesActivity.prenderTodo(habitacion);
                }
            }
        }
    }

    private void encenderTodoCasa (List<Habitacion> habitaciones) {
        if (habitaciones.size() > 0) {
            for (Habitacion habitacion : habitaciones) {
                habitacionesActivity.prenderTodo(habitacion);
            }
        } else {
            Toast.makeText(this.activity, "No hay habitaciones cargadas", Toast.LENGTH_LONG).show();
        }
    }

    private void apagarTodoCasa (List<Habitacion> habitaciones) {
        if (habitaciones.size() > 0) {
            for (Habitacion habitacion : habitaciones) {
                habitacionesActivity.apagarTodo(habitacion);
            }
        } else {
            Toast.makeText(this.activity, "No hay habitaciones cargadas", Toast.LENGTH_LONG).show();
        }
    }

    private void apagarTodoHabitacion (List<Habitacion> habitaciones) {
        String nombre = artefactosActivity.getNombreHabitacion();
        if (habitaciones.size() > 0) {
            for (Habitacion habitacion : habitaciones) {
                if (nombre.equals(habitacion.getNombre())) {
                    habitacionesActivity.apagarTodo(habitacion);
                }
            }
        }
    }

    private void realizarOrdenDosPalabras (List<Habitacion> habitaciones, List<Artefacto> artefactos, String palabra1, String palabra2) {
        switch (palabra1){
            case "abrir": abrirHabitacion(habitaciones, palabra2);
                break;
            case "encender": {
                if (palabra2.equals("todo")){
                    encenderTodoHabitacion(habitaciones);
                } else {
                    encenderArtefacto(artefactos, palabra2);
                }
            }
                break;
            case "apagar": {
                if (palabra2.equals("todo")) {
                    apagarTodoHabitacion(habitaciones);
                } else {
                    apagarArtefacto(artefactos, palabra2);
                }
            }
                break;
            case "salir": salir(palabra2);
                break;
        }
    }

    private void realizarOrdenTresPalabras (List<Habitacion> habitaciones, String palabra1, String palabra2, String palabra3) {
        switch (palabra1) {
            case "encender": {
                if (palabra2.equals("todo") && palabra3.equals("casa")){
                    encenderTodoCasa(habitaciones);
                } else {
                    tresPalabras();
                }
            }
                break;
            case "apagar": {
                if (palabra2.equals("todo") && palabra3.equals("casa")){
                    apagarTodoCasa(habitaciones);
                } else {
                    tresPalabras();
                }
            }
                break;
            default:
                tresPalabras();
                break;
        }
    }

    public void analizarOrden(List<Habitacion> habitaciones, List<Artefacto> artefactos) {
        switch (cantidadPalabras) {
            case 0:
                noHayOrdenes();
                break;
            case 1: unaPalabra();
                break;
            case 2: realizarOrdenDosPalabras(habitaciones, artefactos, palabra1, palabra2);
                break;
            case 3: realizarOrdenTresPalabras(habitaciones,palabra1,palabra2,palabra3);
                break;
            case 4:
                break;
            default: Toast.makeText(this.activity, "Muchas palabras, repita orden", Toast.LENGTH_LONG).show();
                break;
        }

    }
}
