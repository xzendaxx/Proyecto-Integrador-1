package com.agroinspeccion.modelo;

/**
 * Representa a un asistente técnico encargado de las inspecciones.
 */
public class AsistenteTecnico {
    private int id;
    private String identificacion;
    private String nombre;
    private String telefono;
    private String correo;
    private String tarjetaProfesional;

    public AsistenteTecnico() {
    }

    public AsistenteTecnico(int id, String identificacion, String nombre, String telefono, String correo, String tarjetaProfesional) {
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.tarjetaProfesional = tarjetaProfesional;
    }

    public AsistenteTecnico(String identificacion, String nombre, String telefono, String correo, String tarjetaProfesional) {
        this(0, identificacion, nombre, telefono, correo, tarjetaProfesional);
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

    public String getTarjetaProfesional() {
        return tarjetaProfesional;
    }

    public void setTarjetaProfesional(String tarjetaProfesional) {
        this.tarjetaProfesional = tarjetaProfesional;
    }

    @Override
    public String toString() {
        return nombre + " (" + tarjetaProfesional + ")";
    }
}
