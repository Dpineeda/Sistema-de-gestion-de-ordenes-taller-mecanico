package com.umg.edu.taller.modelo;

public class mecanico extends Persona {
    private int id_mecanico;

public mecanico (int id_mecanico, String nombre, String telefono) {
        super(nombre, telefono);
        this.id_mecanico = id_mecanico;
}

public int getid_mecanico() {
    return id_mecanico;
}

public int getId_mecanico() {
    return id_mecanico;
}

public void setid_mecanico (int id_mecanico) {
    this.id_mecanico = id_mecanico;
}

public void setId_mecanico(int id_mecanico) {
    this.id_mecanico = id_mecanico;
}

public String getnombre() {
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
}
