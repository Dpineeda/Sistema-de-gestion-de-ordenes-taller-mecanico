package com.umg.edu.taller.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.umg.edu.taller.dao.clienteDao;
import com.umg.edu.taller.modelo.cliente;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller 
@RequestMapping("/clientes")
public class clienteControlador {
    private final clienteDao clienteDao = new clienteDao();

    @GetMapping()
    public String listar(Model model) {
        model.addAttribute("cliente", new cliente("", "", "", ""));
        model.addAttribute("clientes", clienteDao.listartodos());
        return "cliente";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        cliente cliente = clienteDao.buscarporid(id);
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el cliente con ese ID " + id);
            return "redirect:/clientes";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("clientes", clienteDao.listartodos());
        return "cliente";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute cliente cliente, RedirectAttributes redirectAttributes) {
        try {
            boolean guardado;
            if (clienteDao.buscarporid(cliente.getid_cliente()) == null) {
                guardado = clienteDao.insertar(cliente);
            } else {
                guardado = clienteDao.actualizar(cliente);
            }

            if (guardado) {
                redirectAttributes.addFlashAttribute("mensaje", "Cliente guardado correctamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No fue posible guardar el cliente");
            }
        } catch (Exception e) {
            System.err.println("Error al guardar el cliente");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al guardar el cliente");
        }
        return "redirect:/clientes";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {
        if (clienteDao.eliminar(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "No fue posible eliminar el cliente. Puede estar relacionado con un vehículo");
        }
        return "redirect:/clientes";
    }
}
