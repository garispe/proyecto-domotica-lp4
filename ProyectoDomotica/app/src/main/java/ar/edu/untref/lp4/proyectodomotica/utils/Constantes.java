package ar.edu.untref.lp4.proyectodomotica.utils;

public class Constantes {

    // Nombre de la Base de Datos
    public static final String NOMBRE_BD = "DOMUNTREF";
    public static final int VERSION_BD = 4;


    // HABITACIONES

    public static final String ID_HABITACION = "id";
    public static final String NOMBRE_HABITACION = "nombre";

    public static final String TABLA_HABITACIONES = "habitaciones";

    // ---------------------------------- //

    // ARTEFACTOS

    public static final String ID_ARTEFACTO = "id";
    public static final String NOMBRE_ARTEFACTO = "nombre";
    public static final String ESTADO = "estado";
    public static final String FK_HABITACION = "id_habitacion";
    public static final String ID_PIN = "id_pin";

    public static final String TABLA_ARTEFACTOS = "artefactos";

    //CONTROLADOR COMANDO DE VOZ

    public static final String ABRIR = "abrir";
    public static final String TODO = "todo";
    public static final String APAGAR = "apagar";
    public static final String CASA = "casa";
    public static final String ENCENDER = "encender";
    public static final String SALIR = "salir";
}
