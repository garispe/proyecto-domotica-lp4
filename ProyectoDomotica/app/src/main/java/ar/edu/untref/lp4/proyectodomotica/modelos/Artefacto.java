package ar.edu.untref.lp4.proyectodomotica.modelos;

public class Artefacto {

    private int id;
    private String nombre;
    private boolean activo;

    public Artefacto(String nombre) {
        //ver si hace falta setear id
        this.nombre = nombre;
        this.activo = false;
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


}
