package com.agroinspeccion.dao;

import com.agroinspeccion.modelo.Productor;
import com.agroinspeccion.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de productores.
 */
public class ProductorDAO implements CrudDAO<Productor> {

    private static final String INSERT_SQL =
            "INSERT INTO productores (identificacion, nombre, telefono, correo) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT id, identificacion, nombre, telefono, correo FROM productores WHERE id = ?";
    private static final String SELECT_ALL =
            "SELECT id, identificacion, nombre, telefono, correo FROM productores";
    private static final String UPDATE_SQL =
            "UPDATE productores SET identificacion = ?, nombre = ?, telefono = ?, correo = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM productores WHERE id = ?";

    @Override
    public int crear(Productor productor) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, productor.getIdentificacion());
            ps.setString(2, productor.getNombre());
            ps.setString(3, productor.getTelefono());
            ps.setString(4, productor.getCorreo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    productor.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    @Override
    public Productor obtenerPorId(int id) throws SQLException {
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
    public List<Productor> listarTodos() throws SQLException {
        List<Productor> productores = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                productores.add(mapear(rs));
            }
        }
        return productores;
    }

    @Override
    public void actualizar(Productor productor) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, productor.getIdentificacion());
            ps.setString(2, productor.getNombre());
            ps.setString(3, productor.getTelefono());
            ps.setString(4, productor.getCorreo());
            ps.setInt(5, productor.getId());
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

    private Productor mapear(ResultSet rs) throws SQLException {
        return new Productor(
                rs.getInt("id"),
                rs.getString("identificacion"),
                rs.getString("nombre"),
                rs.getString("telefono"),
                rs.getString("correo"));
    }
}
