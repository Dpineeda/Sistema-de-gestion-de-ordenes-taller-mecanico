package com.umg.edu.taller.dao;

import java.util.List;

public interface CrudDao <T> {
    boolean insertar(T objeto);
    boolean actualizar(T objeto);
    boolean eliminar(int id);
    T buscarporid(int id);
    List<T> listartodos();
}
