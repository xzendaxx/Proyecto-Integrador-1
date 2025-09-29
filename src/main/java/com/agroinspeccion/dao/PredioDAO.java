package com.agroinspeccion.dao;

import com.agroinspeccion.modelo.Predio;
import com.agroinspeccion.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la administración de predios.
 */
public class PredioDAO implements CrudDAO<Predio> {

    private static final String INSERT_SQL = "INSERT INTO predios " +
            "(nombre, departamento, municipio, vereda, area_hectareas, propietario_id, lugar_produccion_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT id, nombre, departamento, municipio, vereda, area_hectareas, propietario_id, lugar_produccion_id " +
                    "FROM predios WHERE id = ?";
    private static final String SELECT_ALL =
            "SELECT id, nombre, departamento, municipio, vereda, area_hectareas, propietario_id, lugar_produccion_id FROM predios";
    private static final String UPDATE_SQL = "UPDATE predios SET nombre = ?, departamento = ?, municipio = ?, " +
            "vereda = ?, area_hectareas = ?, propietario_id = ?, lugar_produccion_id = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM predios WHERE id = ?";

    @Override
    public int crear(Predio predio) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, predio.getNombre());
            ps.setString(2, predio.getDepartamento());
            ps.setString(3, predio.getMunicipio());
            ps.setString(4, predio.getVereda());
            ps.setDouble(5, predio.getAreaHectareas());
            ps.setInt(6, predio.getPropietarioId());
            ps.setInt(7, predio.getLugarProduccionId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    predio.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    @Override
    public Predio obtenerPorId(int id) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Predio> listarTodos() throws SQLException {
        List<Predio> predios = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                predios.add(mapear(rs));
            }
        }
        return predios;
    }

    @Override
    public void actualizar(Predio predio) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, predio.getNombre());
            ps.setString(2, predio.getDepartamento());
            ps.setString(3, predio.getMunicipio());
            ps.setString(4, predio.getVereda());
            ps.setDouble(5, predio.getAreaHectareas());
            ps.setInt(6, predio.getPropietarioId());
            ps.setInt(7, predio.getLugarProduccionId());
            ps.setInt(8, predio.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Predio mapear(ResultSet rs) throws SQLException {
        return new Predio(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("departamento"),
                rs.getString("municipio"),
                rs.getString("vereda"),
                rs.getDouble("area_hectareas"),
                rs.getInt("propietario_id"),
                rs.getInt("lugar_produccion_id"));
    }
}
