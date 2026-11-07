package com.umg.edu.taller.modelo;

public class cliente extends Persona {
private String id_cliente;
private String correo;

public cliente() {
    super(null, null);
}

public cliente(String id_cliente, String nombre, String telefono, String correo) {
    super(nombre, telefono);
    this.id_cliente = id_cliente;
    this.correo = correo;
}

public String getId_cliente() {
    return id_cliente;
}

public void setId_cliente(String id_cliente) {
    this.id_cliente = id_cliente;
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
    return getNombre();
}

public void setnombre(String nombre) {
    setNombre(nombre);
}

public String gettelefono () {
    return getTelefono();
}

public void settelefono (String telefono) {
    setTelefono(telefono);
}

public String getcorreo () {
    return correo;
}

public void setcorreo(String correo) {
    this.correo = correo;
}


}
