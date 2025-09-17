package com.agroinspeccion.dao;

import com.agroinspeccion.modelo.DetalleInspeccionDTO;
import com.agroinspeccion.modelo.InformeFitosanitario;
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
 * DAO para la entidad InformeFitosanitario.
 */
public class InformeFitosanitarioDAO implements CrudDAO<InformeFitosanitario> {

    private static final String INSERT_SQL = "INSERT INTO informes_fitosanitarios " +
            "(lote_id, plaga_id, asistente_id, fecha, plantas_totales, plantas_afectadas, porcentaje_incidente, observaciones) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT id, lote_id, plaga_id, asistente_id, fecha, plantas_totales, plantas_afectadas, porcentaje_incidente, observaciones " +
                    "FROM informes_fitosanitarios WHERE id = ?";
    private static final String SELECT_ALL =
            "SELECT id, lote_id, plaga_id, asistente_id, fecha, plantas_totales, plantas_afectadas, porcentaje_incidente, observaciones " +
                    "FROM informes_fitosanitarios";
    private static final String UPDATE_SQL = "UPDATE informes_fitosanitarios SET lote_id = ?, plaga_id = ?, asistente_id = ?, " +
            "fecha = ?, plantas_totales = ?, plantas_afectadas = ?, porcentaje_incidente = ?, observaciones = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM informes_fitosanitarios WHERE id = ?";
    private static final String SELECT_DETALLE =
            "SELECT l.numero AS numero_lote, c.especie, i.fecha, i.plantas_totales, i.plantas_afectadas, " +
                    "i.porcentaje_incidente, a.nombre AS tecnico " +
                    "FROM informes_fitosanitarios i " +
                    "JOIN lotes l ON i.lote_id = l.id " +
                    "JOIN cultivos c ON l.cultivo_id = c.id " +
                    "JOIN asistentes_tecnicos a ON i.asistente_id = a.id";

    @Override
    public int crear(InformeFitosanitario informe) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, informe.getLoteId());
            ps.setInt(2, informe.getPlagaId());
            ps.setInt(3, informe.getAsistenteId());
            ps.setDate(4, FechaUtil.toSqlDate(informe.getFecha()));
            ps.setInt(5, informe.getPlantasTotales());
            ps.setInt(6, informe.getPlantasAfectadas());
            ps.setDouble(7, informe.getPorcentajeIncidencia());
            ps.setString(8, informe.getObservaciones());
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
    public InformeFitosanitario obtenerPorId(int id) throws SQLException {
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
    public List<InformeFitosanitario> listarTodos() throws SQLException {
        List<InformeFitosanitario> informes = new ArrayList<>();
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
    public void actualizar(InformeFitosanitario informe) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setInt(1, informe.getLoteId());
            ps.setInt(2, informe.getPlagaId());
            ps.setInt(3, informe.getAsistenteId());
            ps.setDate(4, FechaUtil.toSqlDate(informe.getFecha()));
            ps.setInt(5, informe.getPlantasTotales());
            ps.setInt(6, informe.getPlantasAfectadas());
            ps.setDouble(7, informe.getPorcentajeIncidencia());
            ps.setString(8, informe.getObservaciones());
            ps.setInt(9, informe.getId());
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

    /**
     * Obtiene una lista con el detalle de las inspecciones para reportes.
     *
     * @return lista de detalles de inspección
     * @throws SQLException si ocurre un error de acceso a datos
     */
    public List<DetalleInspeccionDTO> listarDetalles() throws SQLException {
        List<DetalleInspeccionDTO> detalles = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SELECT_DETALLE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                detalles.add(new DetalleInspeccionDTO(
                        rs.getString("numero_lote"),
                        rs.getString("especie"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getInt("plantas_totales"),
                        rs.getInt("plantas_afectadas"),
                        rs.getDouble("porcentaje_incidente"),
                        rs.getString("tecnico")));
            }
        }
        return detalles;
    }

    private InformeFitosanitario mapear(ResultSet rs) throws SQLException {
        return new InformeFitosanitario(
                rs.getInt("id"),
                rs.getInt("lote_id"),
                rs.getInt("plaga_id"),
                rs.getInt("asistente_id"),
                rs.getDate("fecha").toLocalDate(),
                rs.getInt("plantas_totales"),
                rs.getInt("plantas_afectadas"),
                rs.getDouble("porcentaje_incidente"),
                rs.getString("observaciones"));
    }
}
