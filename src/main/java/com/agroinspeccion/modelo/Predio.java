package com.agroinspeccion.modelo;

/**
 * Representa un predio agrícola asociado a un lugar de producción.
 */
public class Predio {
    private int id;
    private String nombre;
    private String departamento;
    private String municipio;
    private String vereda;
    private double areaHectareas;
    private int propietarioId;
    private int lugarProduccionId;

    public Predio() {
    }

    public Predio(int id, String nombre, String departamento, String municipio, String vereda,
                  double areaHectareas, int propietarioId, int lugarProduccionId) {
        this.id = id;
        this.nombre = nombre;
        this.departamento = departamento;
        this.municipio = municipio;
        this.vereda = vereda;
        this.areaHectareas = areaHectareas;
        this.propietarioId = propietarioId;
        this.lugarProduccionId = lugarProduccionId;
    }

    public Predio(String nombre, String departamento, String municipio, String vereda,
                  double areaHectareas, int propietarioId, int lugarProduccionId) {
        this(0, nombre, departamento, municipio, vereda, areaHectareas, propietarioId, lugarProduccionId);
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

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getVereda() {
        return vereda;
    }

    public void setVereda(String vereda) {
        this.vereda = vereda;
    }

    public double getAreaHectareas() {
        return areaHectareas;
    }

    public void setAreaHectareas(double areaHectareas) {
        this.areaHectareas = areaHectareas;
    }

    public int getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(int propietarioId) {
        this.propietarioId = propietarioId;
    }

    public int getLugarProduccionId() {
        return lugarProduccionId;
    }

    public void setLugarProduccionId(int lugarProduccionId) {
        this.lugarProduccionId = lugarProduccionId;
    }

    @Override
    public String toString() {
        return nombre + " - " + municipio + ", " + departamento;
    }
}
