package com.agroinspeccion.modelo;

/**
 * Representa un productor agrícola registrado ante el ICA.
 */
public class Productor {
    private int id;
    private String identificacion;
    private String nombre;
    private String telefono;
    private String correo;

    public Productor() {
    }

    public Productor(int id, String identificacion, String nombre, String telefono, String correo) {
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }

    public Productor(String identificacion, String nombre, String telefono, String correo) {
        this(0, identificacion, nombre, telefono, correo);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return nombre + " (" + identificacion + ")";
    }
}
