package com.umg.edu.taller.dao;

import java.util.List;
import com.umg.edu.taller.dao.clienteDao;
import com.umg.edu.taller.dao.mecanicoDao;
import com.umg.edu.taller.modelo.cliente;
import com.umg.edu.taller.modelo.mecanico;

public class pruebaDao {

    public static void main(String[] args) {

     
        //EJECUCION PARA LA TABLA MECANICO
        mecanicoDao daoMecanicos = new mecanicoDao();

        mecanico nuevoM = new mecanico(900, "Carlos Prueba", "55550000");
        if (daoMecanicos.insertar(nuevoM)) {
            System.out.println("Mecanico insertado correctamente");
        }
    }
}
      
