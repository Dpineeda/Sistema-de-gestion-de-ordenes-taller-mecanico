package com.umg.edu.taller.servicio;

import com.umg.edu.taller.dao.comprobanteDao;
import com.umg.edu.taller.modelo.comprobante;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class comprobanteServicio {
    private final comprobanteDao comprobanteDao = new comprobanteDao();

    public List<comprobante> listarComprobantes() {
        return comprobanteDao.listartodos();
    }

    public comprobante buscarComprobante(int id) {
        return id <= 0 ? null : comprobanteDao.buscarporid(id);
    }

    public BigDecimal ingresosEntre(LocalDateTime inicio, LocalDateTime fin) throws SQLException {
        return comprobanteDao.ingresosEntre(inicio, fin);
    }
}
