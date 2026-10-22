package com.umg.edu.taller.modelo;

import java.math.BigDecimal;

public class orden_repuesto {
    private int id_orden_repuesto;
    private orden orden;
    private repuesto repuesto;
    private int cantidad;
    private BigDecimal precio_unidad;

    public orden_repuesto(int id_orden_repuesto, orden orden, repuesto repuesto, int cantidad, BigDecimal precio_unidad) {
        this.id_orden_repuesto = id_orden_repuesto;
        this.orden = orden;
        this.repuesto = repuesto;
        this.cantidad = cantidad;
        this.precio_unidad = precio_unidad;
    }

    public int getId_orden_repuesto() {
        return id_orden_repuesto;
    }

    public void setId_orden_repuesto(int id_orden_repuesto) {
        this.id_orden_repuesto = id_orden_repuesto;
    }

    public orden getOrden() {
        return orden;
    }

    public void setOrden(orden orden) {
        this.orden = orden;
    }

    public repuesto getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(repuesto repuesto) {
        this.repuesto = repuesto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio_unidad() {
        return precio_unidad;
    }

    public void setPrecio_unidad(BigDecimal precio_unidad) {
        this.precio_unidad = precio_unidad;
    }
}
