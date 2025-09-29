package com.agroinspeccion.modelo;

import java.time.LocalDate;

/**
 * Representa un informe de producción por lote.
 */
public class InformeProduccion {
    private int id;
    private int loteId;
    private LocalDate fecha;
    private double cantidadCosechada;
    private String observaciones;

    public InformeProduccion() {
    }

    public InformeProduccion(int id, int loteId, LocalDate fecha, double cantidadCosechada, String observaciones) {
        this.id = id;
        this.loteId = loteId;
        this.fecha = fecha;
        this.cantidadCosechada = cantidadCosechada;
        this.observaciones = observaciones;
    }

    public InformeProduccion(int loteId, LocalDate fecha, double cantidadCosechada, String observaciones) {
        this(0, loteId, fecha, cantidadCosechada, observaciones);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoteId() {
        return loteId;
    }

    public void setLoteId(int loteId) {
        this.loteId = loteId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getCantidadCosechada() {
        return cantidadCosechada;
    }

    public void setCantidadCosechada(double cantidadCosechada) {
        this.cantidadCosechada = cantidadCosechada;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
