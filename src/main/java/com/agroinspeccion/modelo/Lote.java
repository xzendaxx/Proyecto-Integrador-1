package com.agroinspeccion.modelo;

/**
 * Representa un lote asociado a un cultivo específico.
 */
public class Lote {
    private int id;
    private String numero;
    private String descripcion;
    private double area;
    private int cultivoId;

    public Lote() {
    }

    public Lote(int id, String numero, String descripcion, double area, int cultivoId) {
        this.id = id;
        this.numero = numero;
        this.descripcion = descripcion;
        this.area = area;
        this.cultivoId = cultivoId;
    }

    public Lote(String numero, String descripcion, double area, int cultivoId) {
        this(0, numero, descripcion, area, cultivoId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getCultivoId() {
        return cultivoId;
    }

    public void setCultivoId(int cultivoId) {
        this.cultivoId = cultivoId;
    }

    @Override
    public String toString() {
        return "Lote " + numero;
    }
}
