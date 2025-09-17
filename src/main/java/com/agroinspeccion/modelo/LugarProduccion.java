package com.agroinspeccion.modelo;

/**
 * Representa un lugar de producción registrado en el ICA.
 */
public class LugarProduccion {
    private int id;
    private String nombre;
    private String numeroRegistroIca;
    private int productorId;

    public LugarProduccion() {
    }

    public LugarProduccion(int id, String nombre, String numeroRegistroIca, int productorId) {
        this.id = id;
        this.nombre = nombre;
        this.numeroRegistroIca = numeroRegistroIca;
        this.productorId = productorId;
    }

    public LugarProduccion(String nombre, String numeroRegistroIca, int productorId) {
        this(0, nombre, numeroRegistroIca, productorId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroRegistroIca() {
        return numeroRegistroIca;
    }

    public void setNumeroRegistroIca(String numeroRegistroIca) {
        this.numeroRegistroIca = numeroRegistroIca;
    }

    public int getProductorId() {
        return productorId;
    }

    public void setProductorId(int productorId) {
        this.productorId = productorId;
    }

    @Override
    public String toString() {
        return nombre + " - Registro ICA " + numeroRegistroIca;
    }
}
