package com.umg.edu.taller.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.umg.edu.taller.dao.mecanicoDao;
import com.umg.edu.taller.modelo.mecanico;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/mecanicos")
public class mecanicoControlador {
    private mecanicoDao mecanicoDao = new mecanicoDao();

    @GetMapping()
    public String listar(Model model) {
        model.addAttribute("mecanicos", mecanicoDao.listartodos());
        return "mecanico";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        mecanico mecanico = mecanicoDao.buscarporid(id);
        if (mecanico == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el mecánico con ID " + id);
            return "redirect:/mecanicos";
        }

        model.addAttribute("mecanico", mecanico);
        model.addAttribute("mecanicos", mecanicoDao.listartodos());
        return "mecanico";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute mecanico mecanico, RedirectAttributes redirectAttributes) {
        try {
            boolean guardado;
            if (mecanicoDao.buscarporid(mecanico.getid_mecanico()) == null) {
                guardado = mecanicoDao.insertar(mecanico);
            } else {
                guardado = mecanicoDao.actualizar(mecanico);
            }

            if (guardado) {
                redirectAttributes.addFlashAttribute("mensaje", "Mecánico guardado correctamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No fue posible guardar el mecánico");
            }
        } catch (Exception e) {
            System.err.println("Error al guardar el mecánico");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al guardar el mecánico");
        }
        return "redirect:/mecanicos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        if (mecanicoDao.eliminar(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "Mecánico eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "No fue posible eliminar el mecánico; puede estar relacionado con una orden");
        }
        return "redirect:/mecanicos";
    }
}
