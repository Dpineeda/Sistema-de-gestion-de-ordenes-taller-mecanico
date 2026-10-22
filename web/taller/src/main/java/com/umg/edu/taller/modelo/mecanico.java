package com.umg.edu.taller.modelo;

public class mecanico {
    private int id_mecanico;
    private String nombre;
    private String telefono;

public mecanico (int id_mecanico, String nombre, String telefono) {

        this.id_mecanico = id_mecanico;
        this.nombre = nombre;
        this.telefono = telefono;
}

public int getid_mecanico() {
    return id_mecanico;
}

public void setid_mecanico (int id_mecanico) {
    this.id_mecanico = id_mecanico;
}

public String getnombre() {
    return nombre;
}

public void setnombre(String nombre) {
    this.nombre = nombre;
}

public String gettelefono () {
    return telefono;

}

public void settelefono (String telefono) {
    this.telefono = telefono;
}



}
