package com.agroinspeccion.modelo;

/**
 * Representa el propietario de un predio agrícola.
 */
public class Propietario {
    private int id;
    private String identificacion;
    private String nombre;
    private String telefono;
    private String correo;

    public Propietario() {
    }

    public Propietario(int id, String identificacion, String nombre, String telefono, String correo) {
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }

    public Propietario(String identificacion, String nombre, String telefono, String correo) {
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
