package ar.edu.untref.lp4.proyectodomotica.modelos;

import java.util.LinkedList;
import java.util.List;

public class Habitacion {

    private int id;
    private String nombre;
    private List<Artefacto> listaArtefactos;

    public Habitacion(String nombre) {

        //ver si hace falta setear id
        this.nombre = nombre;
        listaArtefactos = new LinkedList<Artefacto>();

    }

    public int getId() {

        return this.id;

    }

    public String getNombre() {

        return nombre;

    }

    public void setNombre(String nombre) {

        this.nombre = nombre;

    }

    public List<Artefacto> getArtefactos() {

        return this.listaArtefactos;

    }

    public void agregarArtefacto(Artefacto artefactoAAgregar) {

        this.listaArtefactos.add(artefactoAAgregar);

    }
}
