package com.umg.edu.taller.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class comprobante {
    private int id_comprobante;
    private LocalDateTime fecha_emision;
    private orden orden;
    private BigDecimal total_repuesto;
    private BigDecimal total;

    public comprobante(int id_comprobante, LocalDateTime fecha_emision, orden orden, BigDecimal total_repuesto, BigDecimal total) {
        this.id_comprobante = id_comprobante;
        this.fecha_emision = fecha_emision;
        this.orden = orden;
        this.total_repuesto = total_repuesto;
        this.total = total;
    }

    public int getId_comprobante() {
        return id_comprobante;
    }

    public void setId_comprobante(int id_comprobante) {
        this.id_comprobante = id_comprobante;
    }

    public LocalDateTime getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(LocalDateTime fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public orden getorden () {
        return orden;
    }

    public void setorden(orden orden) {
        this.orden = orden;
    }


    public BigDecimal getTotal_repuesto() {
        return total_repuesto;
    }

    public void setTotal_repuesto(BigDecimal total_repuesto) {
        this.total_repuesto = total_repuesto;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}
