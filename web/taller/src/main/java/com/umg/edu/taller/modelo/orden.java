package com.umg.edu.taller.modelo;

import java.math.BigDecimal;

public class orden {
    public static final String PENDIENTE = "PENDIENTE";
    public static final String EN_PROCESO = "EN PROCESO";
    public static final String FINALIZADA = "FINALIZADA";
    public static final String CANCELADA = "CANCELADA";

    private int id_orden;
    private vehiculo vehiculo;
    private mecanico mecanico;
    private String descripcion_problema;
    private BigDecimal precio_total;
    private String estado;

    public orden (int id_orden, vehiculo vehiculo, mecanico mecanico, String descripcion_problema, BigDecimal precio_total) {
        this(id_orden, vehiculo, mecanico, descripcion_problema, precio_total, PENDIENTE);
    }

    public orden(int id_orden, vehiculo vehiculo, mecanico mecanico, String descripcion_problema,
                 BigDecimal precio_total, String estado) {
        this.id_orden = id_orden;
        this.vehiculo = vehiculo;
        this.mecanico = mecanico;
        this.descripcion_problema = descripcion_problema;
        this.precio_total = precio_total;
        this.estado = estado;
    }

    public int getid_orden() {
        return id_orden;

    }

    public void setid_orden(int id_orden) {
        this.id_orden = id_orden;
    }

    public vehiculo getvehiculo() {
        return vehiculo;
    }
 
    public void setvehiculo(vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public mecanico getmecanico() {
        return  mecanico;

    }

    public void setmecanico(mecanico mecanico) {
        this.mecanico = mecanico;
    }

    public String getdescripcion_problema() {
        return  descripcion_problema;
    }

    public void setdescripcion_problema(String descripcion_problema) {
        this.descripcion_problema = descripcion_problema;
    }

    public BigDecimal precio_total() {
        return precio_total;
    }

    public void setprecio_total(BigDecimal precio_total) {
        this.precio_total = precio_total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
