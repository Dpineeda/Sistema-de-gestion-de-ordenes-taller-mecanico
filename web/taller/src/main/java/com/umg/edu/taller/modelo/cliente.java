package com.umg.edu.taller.modelo;

public class cliente {
private String id_cliente;
private String nombre;
private String telefono;
private String correo;

public cliente() {
}

public cliente(String id_cliente, String nombre, String telefono, String correo) {
    this.id_cliente = id_cliente;
    this.nombre = nombre;
    this.telefono = telefono;
    this.correo = correo;
}

public String getId_cliente() {
    return id_cliente;
}

public void setId_cliente(String id_cliente) {
    this.id_cliente = id_cliente;
}

public String getNombre() {
    return nombre;
}

public void setNombre(String nombre) {
    this.nombre = nombre;
}

public String getTelefono() {
    return telefono;
}

public void setTelefono(String telefono) {
    this.telefono = telefono;
}

public String getCorreo() {
    return correo;
}

public void setCorreo(String correo) {
    this.correo = correo;
}

public String getid_cliente () {
    return id_cliente;
}

public void setid_cliente(String id_cliente) {
    this.id_cliente = id_cliente;
}

public String getnombre () {
    return nombre;
}

public void setnombre(String nombre) {
    this.nombre = nombre;
}

public String gettelefono () {
    return  telefono;
}

public void settelefono (String telefono) {
    this.telefono = telefono;
}

public String getcorreo () {
    return correo;
}

public void setcorreo(String correo) {
    this.correo = correo;
}


}
