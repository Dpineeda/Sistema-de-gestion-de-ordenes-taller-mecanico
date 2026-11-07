package com.umg.edu.taller.modelo;

import java.math.BigDecimal;

public class orden_servicio {
    private int id_orden_servicio;
    private orden orden;
    private servicio servicio;
    private BigDecimal costo;

    public orden_servicio(int id_orden_servicio, orden orden, servicio servicio, BigDecimal costo) {
        this.id_orden_servicio = id_orden_servicio;
        this.orden = orden;
        this.servicio = servicio;
        this.costo = costo;
    }

    public int getId_orden_servicio() {
        return id_orden_servicio;
    }

    public void setId_orden_servicio(int id_orden_servicio) {
        this.id_orden_servicio = id_orden_servicio;
    }

    public orden getOrden() {
        return orden;
    }

    public void setOrden(orden orden) {
        this.orden = orden;
    }

    public servicio getServicio() {
        return servicio;
    }

    public void setServicio(servicio servicio) {
        this.servicio = servicio;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }
}
