package com.umg.edu.taller.dao;

import java.util.List;

public interface CrudDao <T, K> {
    boolean insertar(T objeto);
    boolean actualizar(T objeto);
    boolean eliminar(K id);
    T buscarporid(K id);
    List<T> listartodos();
}
