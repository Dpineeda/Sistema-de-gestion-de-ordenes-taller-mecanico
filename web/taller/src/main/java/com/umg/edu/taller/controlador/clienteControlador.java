package com.umg.edu.taller.controlador;

import com.umg.edu.taller.TallerApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.umg.edu.taller.dao.clienteDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;


@Controller 
@RequestMapping("/clientes")
public class clienteControlador {
    private final TallerApplication tallerApplication;
    private clienteDao clienteDao = new clienteDao();

    clienteControlador(TallerApplication tallerApplication) {
        this.tallerApplication = tallerApplication;
    }

    @GetMapping()
    public String listar (@Model model) {
        model.addALLtallerApplication
    }
    

}
