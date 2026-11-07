package com.umg.edu.taller.servicio;

import com.umg.edu.taller.dao.clienteDao;
import com.umg.edu.taller.modelo.cliente;
import java.util.List;

public class clienteServicio {
    private final clienteDao clienteDao = new clienteDao();

    public List<cliente> listarClientes() {
        return clienteDao.listartodos();
    }

    public cliente buscarCliente(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return clienteDao.buscarporid(id);
    }

    public boolean guardarCliente(cliente cliente) {
        if (cliente == null || cliente.getid_cliente() == null
                || cliente.getid_cliente().isBlank()
                || cliente.getnombre() == null || cliente.getnombre().isBlank()) {
            return false;
        }

        if (clienteDao.buscarporid(cliente.getid_cliente()) == null) {
            return clienteDao.insertar(cliente);
        }
        return clienteDao.actualizar(cliente);
    }

    public boolean eliminarCliente(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return clienteDao.eliminar(id);
    }
}
