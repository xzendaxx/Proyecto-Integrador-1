package com.agroinspeccion.dao;

import com.agroinspeccion.modelo.Lote;
import com.agroinspeccion.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Lote.
 */
public class LoteDAO implements CrudDAO<Lote> {

    private static final String INSERT_SQL =
            "INSERT INTO lotes (numero, descripcion, area, cultivo_id) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT id, numero, descripcion, area, cultivo_id FROM lotes WHERE id = ?";
    private static final String SELECT_ALL =
            "SELECT id, numero, descripcion, area, cultivo_id FROM lotes";
    private static final String UPDATE_SQL =
            "UPDATE lotes SET numero = ?, descripcion = ?, area = ?, cultivo_id = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM lotes WHERE id = ?";

    @Override
    public int crear(Lote lote) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, lote.getNumero());
            ps.setString(2, lote.getDescripcion());
            ps.setDouble(3, lote.getArea());
            ps.setInt(4, lote.getCultivoId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    lote.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    @Override
    public Lote obtenerPorId(int id) throws SQLException {
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
    public List<Lote> listarTodos() throws SQLException {
        List<Lote> lotes = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lotes.add(mapear(rs));
            }
        }
        return lotes;
    }

    @Override
    public void actualizar(Lote lote) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, lote.getNumero());
            ps.setString(2, lote.getDescripcion());
            ps.setDouble(3, lote.getArea());
            ps.setInt(4, lote.getCultivoId());
            ps.setInt(5, lote.getId());
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

    private Lote mapear(ResultSet rs) throws SQLException {
        return new Lote(
                rs.getInt("id"),
                rs.getString("numero"),
                rs.getString("descripcion"),
                rs.getDouble("area"),
                rs.getInt("cultivo_id"));
    }
}
