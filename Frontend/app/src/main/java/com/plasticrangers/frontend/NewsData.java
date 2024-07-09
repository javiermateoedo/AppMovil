package com.plasticrangers.frontend;

public class NewsData {
    private String nombre;
    private String enlace;
    private int imagen;
    private String descripcion;

    public NewsData(String nombre, String enlace, int imagen, String descripcion) {
        this.nombre = nombre;
        this.enlace = enlace;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public int getImagen() {
        return imagen;
    }
    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public void SetDescripcion(String descripcion) {this.descripcion = descripcion;}
    public String getDescripcion() {return descripcion;}
}
