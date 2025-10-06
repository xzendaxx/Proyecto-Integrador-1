package com.agroinspeccion.dao;

import com.agroinspeccion.modelo.Propietario;
import com.agroinspeccion.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar propietarios de predios.
 */
public class PropietarioDAO implements CrudDAO<Propietario> {

    private static final String INSERT_SQL =
            "INSERT INTO propietarios (identificacion, nombre, telefono, correo) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT id, identificacion, nombre, telefono, correo FROM propietarios WHERE id = ?";
    private static final String SELECT_ALL =
            "SELECT id, identificacion, nombre, telefono, correo FROM propietarios";
    private static final String UPDATE_SQL =
            "UPDATE propietarios SET identificacion = ?, nombre = ?, telefono = ?, correo = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM propietarios WHERE id = ?";

    @Override
    public int crear(Propietario propietario) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, propietario.getIdentificacion());
            ps.setString(2, propietario.getNombre());
            ps.setString(3, propietario.getTelefono());
            ps.setString(4, propietario.getCorreo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    propietario.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    @Override
    public Propietario obtenerPorId(int id) throws SQLException {
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
    public List<Propietario> listarTodos() throws SQLException {
        List<Propietario> propietarios = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                propietarios.add(mapear(rs));
            }
        }
        return propietarios;
    }

    @Override
    public void actualizar(Propietario propietario) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, propietario.getIdentificacion());
            ps.setString(2, propietario.getNombre());
            ps.setString(3, propietario.getTelefono());
            ps.setString(4, propietario.getCorreo());
            ps.setInt(5, propietario.getId());
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

    private Propietario mapear(ResultSet rs) throws SQLException {
        return new Propietario(
                rs.getInt("id"),
                rs.getString("identificacion"),
                rs.getString("nombre"),
                rs.getString("telefono"),
                rs.getString("correo"));
    }
}
