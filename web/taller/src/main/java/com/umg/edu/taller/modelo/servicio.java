package com.umg.edu.taller.modelo;

import java.math.BigDecimal;

public class servicio {
    private int id_servicio;
    private String descripcion;
    private BigDecimal costo_manodeobra;

    public servicio (int id_servicio, String descripcion, BigDecimal costo_manodeobra) {
        this.id_servicio = id_servicio;
        this.descripcion = descripcion;
        this.costo_manodeobra = costo_manodeobra;
    }

    public int getid_servicio () {
        return id_servicio;

    }

    public void setid_servicio (int id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getdescripcion() {
        return descripcion;

    }

    public void setdescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getcosto_manodeobra() {
        return costo_manodeobra;

    }

    public void setcosto_manodeobra(BigDecimal costo_manodeobra) {
        this.costo_manodeobra = costo_manodeobra;
    }


}
