package com.agroinspeccion.modelo;

import java.time.LocalDate;

/**
 * Representa un cultivo registrado en un predio.
 */
public class Cultivo {
    private int id;
    private String especie;
    private String variedad;
    private LocalDate fechaSiembra;
    private int predioId;

    public Cultivo() {
    }

    public Cultivo(int id, String especie, String variedad, LocalDate fechaSiembra, int predioId) {
        this.id = id;
        this.especie = especie;
        this.variedad = variedad;
        this.fechaSiembra = fechaSiembra;
        this.predioId = predioId;
    }

    public Cultivo(String especie, String variedad, LocalDate fechaSiembra, int predioId) {
        this(0, especie, variedad, fechaSiembra, predioId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getVariedad() {
        return variedad;
    }

    public void setVariedad(String variedad) {
        this.variedad = variedad;
    }

    public LocalDate getFechaSiembra() {
        return fechaSiembra;
    }

    public void setFechaSiembra(LocalDate fechaSiembra) {
        this.fechaSiembra = fechaSiembra;
    }

    public int getPredioId() {
        return predioId;
    }

    public void setPredioId(int predioId) {
        this.predioId = predioId;
    }

    @Override
    public String toString() {
        return especie + (variedad != null && !variedad.isBlank() ? " - " + variedad : "");
    }
}
