package com.agroinspeccion.modelo;

/**
 * Representa una plaga monitoreada en un cultivo.
 */
public class Plaga {
    private int id;
    private String nombreComun;
    private String nombreCientifico;

    public Plaga() {
    }

    public Plaga(int id, String nombreComun, String nombreCientifico) {
        this.id = id;
        this.nombreComun = nombreComun;
        this.nombreCientifico = nombreCientifico;
    }

    public Plaga(String nombreComun, String nombreCientifico) {
        this(0, nombreComun, nombreCientifico);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreComun() {
        return nombreComun;
    }

    public void setNombreComun(String nombreComun) {
        this.nombreComun = nombreComun;
    }

    public String getNombreCientifico() {
        return nombreCientifico;
    }

    public void setNombreCientifico(String nombreCientifico) {
        this.nombreCientifico = nombreCientifico;
    }

    @Override
    public String toString() {
        return nombreComun + (nombreCientifico != null && !nombreCientifico.isBlank() ? " (" + nombreCientifico + ")" : "");
    }
}
