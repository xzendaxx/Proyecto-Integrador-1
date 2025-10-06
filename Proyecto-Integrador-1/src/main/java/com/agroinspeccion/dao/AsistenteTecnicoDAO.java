package com.agroinspeccion.dao;

import com.agroinspeccion.modelo.AsistenteTecnico;
import com.agroinspeccion.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar asistentes técnicos autorizados.
 */
public class AsistenteTecnicoDAO implements CrudDAO<AsistenteTecnico> {

    private static final String INSERT_SQL = "INSERT INTO asistentes_tecnicos " +
            "(identificacion, nombre, telefono, correo, tarjeta_profesional) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT id, identificacion, nombre, telefono, correo, tarjeta_profesional FROM asistentes_tecnicos WHERE id = ?";
    private static final String SELECT_ALL =
            "SELECT id, identificacion, nombre, telefono, correo, tarjeta_profesional FROM asistentes_tecnicos";
    private static final String UPDATE_SQL = "UPDATE asistentes_tecnicos SET identificacion = ?, nombre = ?, " +
            "telefono = ?, correo = ?, tarjeta_profesional = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM asistentes_tecnicos WHERE id = ?";

    @Override
    public int crear(AsistenteTecnico asistente) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, asistente.getIdentificacion());
            ps.setString(2, asistente.getNombre());
            ps.setString(3, asistente.getTelefono());
            ps.setString(4, asistente.getCorreo());
            ps.setString(5, asistente.getTarjetaProfesional());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    asistente.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    @Override
    public AsistenteTecnico obtenerPorId(int id) throws SQLException {
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
    public List<AsistenteTecnico> listarTodos() throws SQLException {
        List<AsistenteTecnico> asistentes = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                asistentes.add(mapear(rs));
            }
        }
        return asistentes;
    }

    @Override
    public void actualizar(AsistenteTecnico asistente) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, asistente.getIdentificacion());
            ps.setString(2, asistente.getNombre());
            ps.setString(3, asistente.getTelefono());
            ps.setString(4, asistente.getCorreo());
            ps.setString(5, asistente.getTarjetaProfesional());
            ps.setInt(6, asistente.getId());
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

    private AsistenteTecnico mapear(ResultSet rs) throws SQLException {
        return new AsistenteTecnico(
                rs.getInt("id"),
                rs.getString("identificacion"),
                rs.getString("nombre"),
                rs.getString("telefono"),
                rs.getString("correo"),
                rs.getString("tarjeta_profesional"));
    }
}
