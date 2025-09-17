package com.agroinspeccion.dao;

import com.agroinspeccion.modelo.Plaga;
import com.agroinspeccion.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Plaga.
 */
public class PlagaDAO implements CrudDAO<Plaga> {

    private static final String INSERT_SQL =
            "INSERT INTO plagas (nombre_comun, nombre_cientifico) VALUES (?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT id, nombre_comun, nombre_cientifico FROM plagas WHERE id = ?";
    private static final String SELECT_ALL =
            "SELECT id, nombre_comun, nombre_cientifico FROM plagas";
    private static final String UPDATE_SQL =
            "UPDATE plagas SET nombre_comun = ?, nombre_cientifico = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM plagas WHERE id = ?";

    @Override
    public int crear(Plaga plaga) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, plaga.getNombreComun());
            ps.setString(2, plaga.getNombreCientifico());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    plaga.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    @Override
    public Plaga obtenerPorId(int id) throws SQLException {
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
    public List<Plaga> listarTodos() throws SQLException {
        List<Plaga> plagas = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                plagas.add(mapear(rs));
            }
        }
        return plagas;
    }

    @Override
    public void actualizar(Plaga plaga) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, plaga.getNombreComun());
            ps.setString(2, plaga.getNombreCientifico());
            ps.setInt(3, plaga.getId());
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

    private Plaga mapear(ResultSet rs) throws SQLException {
        return new Plaga(
                rs.getInt("id"),
                rs.getString("nombre_comun"),
                rs.getString("nombre_cientifico"));
    }
}
