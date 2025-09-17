package com.agroinspeccion.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz genérica para operaciones CRUD básicas.
 *
 * @param <T> tipo de entidad gestionada por el DAO
 */
public interface CrudDAO<T> {

    /**
     * Inserta una nueva entidad.
     *
     * @param entidad entidad a persistir
     * @return identificador generado
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    int crear(T entidad) throws SQLException;

    /**
     * Obtiene una entidad por su identificador.
     *
     * @param id identificador
     * @return entidad encontrada o {@code null}
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    T obtenerPorId(int id) throws SQLException;

    /**
     * Recupera todas las entidades registradas.
     *
     * @return lista de entidades
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    List<T> listarTodos() throws SQLException;

    /**
     * Actualiza una entidad existente.
     *
     * @param entidad entidad a actualizar
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    void actualizar(T entidad) throws SQLException;

    /**
     * Elimina una entidad por su identificador.
     *
     * @param id identificador
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    void eliminar(int id) throws SQLException;
}
