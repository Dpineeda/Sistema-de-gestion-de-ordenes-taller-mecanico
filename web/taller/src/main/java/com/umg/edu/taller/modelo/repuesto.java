package com.umg.edu.taller.modelo;

import java.math.BigDecimal;

public class repuesto {
    private int id_repuesto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int existencia;
    private int minimo_en_stock;

    public repuesto(int id_repuesto, String nombre, String descripcion, BigDecimal precio, int existencia, int minimo_en_stock) {
        this.id_repuesto = id_repuesto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.existencia = existencia;
        this.minimo_en_stock = minimo_en_stock;
    }

    public int getId_repuesto() {
        return id_repuesto;
    }

    public void setId_repuesto(int id_repuesto) {
        this.id_repuesto = id_repuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public int getMinimo_en_stock() {
        return minimo_en_stock;
    }

    public void setMinimo_en_stock(int minimo_en_stock) {
        this.minimo_en_stock = minimo_en_stock;
    }
}
