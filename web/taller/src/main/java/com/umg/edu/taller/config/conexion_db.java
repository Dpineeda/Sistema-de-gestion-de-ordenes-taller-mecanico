
package com.umg.edu.taller.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexion_db {

    private static final String URL =
            "jdbc:mysql://localhost:3306/db_taller";

    private static final String USER = "root";
    private static final String PASSWORD = "MiClave123";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
