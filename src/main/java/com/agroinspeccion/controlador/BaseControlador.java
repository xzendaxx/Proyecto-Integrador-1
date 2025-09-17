package com.agroinspeccion.controlador;

import com.agroinspeccion.dao.CrudDAO;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador base que centraliza las operaciones CRUD de las vistas.
 *
 * @param <T> tipo de la entidad a gestionar
 */
public class BaseControlador<T> {

    private final CrudDAO<T> dao;

    public BaseControlador(CrudDAO<T> dao) {
        this.dao = dao;
    }

    public int crear(T entidad) throws SQLException {
        return dao.crear(entidad);
    }

    public void actualizar(T entidad) throws SQLException {
        dao.actualizar(entidad);
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

    public T obtenerPorId(int id) throws SQLException {
        return dao.obtenerPorId(id);
    }

    public List<T> listar() throws SQLException {
        return dao.listarTodos();
    }
}
