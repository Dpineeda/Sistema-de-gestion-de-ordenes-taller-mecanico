package com.umg.edu.taller.config;

import java.sql.Connection;

public class pruebaconexion {

    public static void main(String[] args) {

        try {

            Connection con = conexion_db.conectar();

            if (con != null) {
                System.out.println("CONEXION EXITOSA A MYSQL");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
