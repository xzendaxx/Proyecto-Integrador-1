package com.agroinspeccion.dao;

import com.agroinspeccion.modelo.InformeProduccion;
import com.agroinspeccion.util.ConexionBD;
import com.agroinspeccion.util.FechaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para informes de producción.
 */
public class InformeProduccionDAO implements CrudDAO<InformeProduccion> {

    private static final String INSERT_SQL = "INSERT INTO informes_produccion " +
            "(lote_id, fecha, cantidad_cosechada, observaciones) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT id, lote_id, fecha, cantidad_cosechada, observaciones FROM informes_produccion WHERE id = ?";
    private static final String SELECT_ALL =
            "SELECT id, lote_id, fecha, cantidad_cosechada, observaciones FROM informes_produccion";
    private static final String UPDATE_SQL = "UPDATE informes_produccion SET lote_id = ?, fecha = ?, " +
            "cantidad_cosechada = ?, observaciones = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM informes_produccion WHERE id = ?";

    @Override
    public int crear(InformeProduccion informe) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, informe.getLoteId());
            ps.setDate(2, FechaUtil.toSqlDate(informe.getFecha()));
            ps.setDouble(3, informe.getCantidadCosechada());
            ps.setString(4, informe.getObservaciones());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    informe.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    @Override
    public InformeProduccion obtenerPorId(int id) throws SQLException {
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
    public List<InformeProduccion> listarTodos() throws SQLException {
        List<InformeProduccion> informes = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                informes.add(mapear(rs));
            }
        }
        return informes;
    }

    @Override
    public void actualizar(InformeProduccion informe) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setInt(1, informe.getLoteId());
            ps.setDate(2, FechaUtil.toSqlDate(informe.getFecha()));
            ps.setDouble(3, informe.getCantidadCosechada());
            ps.setString(4, informe.getObservaciones());
            ps.setInt(5, informe.getId());
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

    private InformeProduccion mapear(ResultSet rs) throws SQLException {
        return new InformeProduccion(
                rs.getInt("id"),
                rs.getInt("lote_id"),
                rs.getDate("fecha").toLocalDate(),
                rs.getDouble("cantidad_cosechada"),
                rs.getString("observaciones"));
    }
}
