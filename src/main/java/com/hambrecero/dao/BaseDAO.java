package com.hambrecero.dao;

import java.util.List;

public interface BaseDAO<T, ID> {
    T crear(T entity);
    T obtenerPorId(ID id);
    List<T> obtenerTodos();
    T actualizar(T entity);
    boolean eliminar(ID id);
}