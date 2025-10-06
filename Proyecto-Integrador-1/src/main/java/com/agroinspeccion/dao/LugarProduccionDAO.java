package com.agroinspeccion.dao;

import com.agroinspeccion.modelo.LugarProduccion;
import com.agroinspeccion.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para lugares de producción.
 */
public class LugarProduccionDAO implements CrudDAO<LugarProduccion> {

    private static final String INSERT_SQL = "INSERT INTO lugares_produccion " +
            "(nombre, numero_registro_ica, productor_id) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT id, nombre, numero_registro_ica, productor_id FROM lugares_produccion WHERE id = ?";
    private static final String SELECT_ALL =
            "SELECT id, nombre, numero_registro_ica, productor_id FROM lugares_produccion";
    private static final String UPDATE_SQL = "UPDATE lugares_produccion SET nombre = ?, numero_registro_ica = ?, " +
            "productor_id = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM lugares_produccion WHERE id = ?";

    @Override
    public int crear(LugarProduccion lugar) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, lugar.getNombre());
            ps.setString(2, lugar.getNumeroRegistroIca());
            ps.setInt(3, lugar.getProductorId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    lugar.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    @Override
    public LugarProduccion obtenerPorId(int id) throws SQLException {
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
    public List<LugarProduccion> listarTodos() throws SQLException {
        List<LugarProduccion> lugares = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lugares.add(mapear(rs));
            }
        }
        return lugares;
    }

    @Override
    public void actualizar(LugarProduccion lugar) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, lugar.getNombre());
            ps.setString(2, lugar.getNumeroRegistroIca());
            ps.setInt(3, lugar.getProductorId());
            ps.setInt(4, lugar.getId());
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

    private LugarProduccion mapear(ResultSet rs) throws SQLException {
        return new LugarProduccion(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("numero_registro_ica"),
                rs.getInt("productor_id"));
    }
}
