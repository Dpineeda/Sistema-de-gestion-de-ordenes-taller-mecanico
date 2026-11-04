package com.umg.edu.taller.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.umg.edu.taller.modelo.mecanico;
import com.umg.edu.taller.servicio.mecanicoServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/mecanicos")
public class mecanicoControlador {
    private final mecanicoServicio mecanicoServicio = new mecanicoServicio();

    @GetMapping()
    public String listar(Model model) {
        model.addAttribute("mecanicos", mecanicoServicio.listarMecanicos());
        return "mecanico";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        mecanico mecanico = mecanicoServicio.buscarMecanico(id);
        if (mecanico == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el mecánico con ID " + id);
            return "redirect:/mecanicos";
        }

        model.addAttribute("mecanico", mecanico);
        model.addAttribute("mecanicos", mecanicoServicio.listarMecanicos());
        return "mecanico";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute mecanico mecanico, RedirectAttributes redirectAttributes) {
        try {
            boolean guardado = mecanicoServicio.guardarMecanico(mecanico);

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
        if (mecanicoServicio.eliminarMecanico(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "Mecánico eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "No fue posible eliminar el mecánico; puede estar relacionado con una orden");
        }
        return "redirect:/mecanicos";
    }
}
