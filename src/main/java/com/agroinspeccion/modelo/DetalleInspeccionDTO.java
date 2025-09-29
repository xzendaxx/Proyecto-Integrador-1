package com.agroinspeccion.modelo;

import java.time.LocalDate;

/**
 * DTO utilizado para los reportes PDF de inspecciones fitosanitarias.
 */
public class DetalleInspeccionDTO {
    private final String numeroLote;
    private final String especie;
    private final LocalDate fecha;
    private final int plantasTotales;
    private final int plantasAfectadas;
    private final double porcentajeIncidencia;
    private final String tecnico;

    public DetalleInspeccionDTO(String numeroLote, String especie, LocalDate fecha, int plantasTotales,
                                int plantasAfectadas, double porcentajeIncidencia, String tecnico) {
        this.numeroLote = numeroLote;
        this.especie = especie;
        this.fecha = fecha;
        this.plantasTotales = plantasTotales;
        this.plantasAfectadas = plantasAfectadas;
        this.porcentajeIncidencia = porcentajeIncidencia;
        this.tecnico = tecnico;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public String getEspecie() {
        return especie;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getPlantasTotales() {
        return plantasTotales;
    }

    public int getPlantasAfectadas() {
        return plantasAfectadas;
    }

    public double getPorcentajeIncidencia() {
        return porcentajeIncidencia;
    }

    public String getTecnico() {
        return tecnico;
    }
}
