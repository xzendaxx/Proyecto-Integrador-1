package com.agroinspeccion.dao;

import com.agroinspeccion.modelo.Cultivo;
import com.agroinspeccion.util.ConexionBD;
import com.agroinspeccion.util.FechaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones sobre cultivos.
 */
public class CultivoDAO implements CrudDAO<Cultivo> {

    private static final String INSERT_SQL = "INSERT INTO cultivos " +
            "(especie, variedad, fecha_siembra, predio_id) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT id, especie, variedad, fecha_siembra, predio_id FROM cultivos WHERE id = ?";
    private static final String SELECT_ALL =
            "SELECT id, especie, variedad, fecha_siembra, predio_id FROM cultivos";
    private static final String UPDATE_SQL = "UPDATE cultivos SET especie = ?, variedad = ?, fecha_siembra = ?, " +
            "predio_id = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM cultivos WHERE id = ?";

    @Override
    public int crear(Cultivo cultivo) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cultivo.getEspecie());
            ps.setString(2, cultivo.getVariedad());
            ps.setDate(3, FechaUtil.toSqlDate(cultivo.getFechaSiembra()));
            ps.setInt(4, cultivo.getPredioId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    cultivo.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    @Override
    public Cultivo obtenerPorId(int id) throws SQLException {
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
    public List<Cultivo> listarTodos() throws SQLException {
        List<Cultivo> cultivos = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cultivos.add(mapear(rs));
            }
        }
        return cultivos;
    }

    @Override
    public void actualizar(Cultivo cultivo) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, cultivo.getEspecie());
            ps.setString(2, cultivo.getVariedad());
            ps.setDate(3, FechaUtil.toSqlDate(cultivo.getFechaSiembra()));
            ps.setInt(4, cultivo.getPredioId());
            ps.setInt(5, cultivo.getId());
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

    private Cultivo mapear(ResultSet rs) throws SQLException {
        LocalDate fecha = rs.getDate("fecha_siembra") != null ? rs.getDate("fecha_siembra").toLocalDate() : null;
        return new Cultivo(
                rs.getInt("id"),
                rs.getString("especie"),
                rs.getString("variedad"),
                fecha,
                rs.getInt("predio_id"));
    }
}
