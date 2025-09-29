package com.agroinspeccion.modelo;

import java.time.LocalDate;

/**
 * Representa un informe fitosanitario generado tras una inspección.
 */
public class InformeFitosanitario {
    private int id;
    private int loteId;
    private int plagaId;
    private int asistenteId;
    private LocalDate fecha;
    private int plantasTotales;
    private int plantasAfectadas;
    private double porcentajeIncidencia;
    private String observaciones;

    public InformeFitosanitario() {
    }

    public InformeFitosanitario(int id, int loteId, int plagaId, int asistenteId, LocalDate fecha,
                                int plantasTotales, int plantasAfectadas, double porcentajeIncidencia,
                                String observaciones) {
        this.id = id;
        this.loteId = loteId;
        this.plagaId = plagaId;
        this.asistenteId = asistenteId;
        this.fecha = fecha;
        this.plantasTotales = plantasTotales;
        this.plantasAfectadas = plantasAfectadas;
        this.porcentajeIncidencia = porcentajeIncidencia;
        this.observaciones = observaciones;
    }

    public InformeFitosanitario(int loteId, int plagaId, int asistenteId, LocalDate fecha,
                                int plantasTotales, int plantasAfectadas, double porcentajeIncidencia,
                                String observaciones) {
        this(0, loteId, plagaId, asistenteId, fecha, plantasTotales, plantasAfectadas, porcentajeIncidencia, observaciones);
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

    public int getPlagaId() {
        return plagaId;
    }

    public void setPlagaId(int plagaId) {
        this.plagaId = plagaId;
    }

    public int getAsistenteId() {
        return asistenteId;
    }

    public void setAsistenteId(int asistenteId) {
        this.asistenteId = asistenteId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getPlantasTotales() {
        return plantasTotales;
    }

    public void setPlantasTotales(int plantasTotales) {
        this.plantasTotales = plantasTotales;
    }

    public int getPlantasAfectadas() {
        return plantasAfectadas;
    }

    public void setPlantasAfectadas(int plantasAfectadas) {
        this.plantasAfectadas = plantasAfectadas;
    }

    public double getPorcentajeIncidencia() {
        return porcentajeIncidencia;
    }

    public void setPorcentajeIncidencia(double porcentajeIncidencia) {
        this.porcentajeIncidencia = porcentajeIncidencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
