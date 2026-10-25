package com.umg.edu.taller.controlador;

import com.umg.edu.taller.dao.servicioDao;
import com.umg.edu.taller.modelo.servicio;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/servicios")
public class servicioControlador {
    private final servicioDao servicioDao = new servicioDao();

    @GetMapping()
    public String listar(Model model) {
        model.addAttribute("servicios", servicioDao.listartodos());
        return "servicio";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        servicio servicio = servicioDao.buscarporid(id);
        if (servicio == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el servicio con ID " + id);
            return "redirect:/servicios";
        }
        model.addAttribute("servicio", servicio);
        model.addAttribute("servicios", servicioDao.listartodos());
        return "servicio";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Integer idServicio,
                          @RequestParam String descripcion,
                          @RequestParam BigDecimal costoManoObra,
                          RedirectAttributes redirectAttributes) {
        servicio servicio = new servicio(idServicio, descripcion, costoManoObra);
        try {
            boolean guardado = servicioDao.buscarporid(idServicio) == null
                    ? servicioDao.insertar(servicio)
                    : servicioDao.actualizar(servicio);
            redirectAttributes.addFlashAttribute(guardado ? "mensaje" : "error",
                    guardado ? "Servicio guardado correctamente"
                            : "No fue posible guardar el servicio; verifique que el costo no sea negativo");
        } catch (Exception e) {
            System.err.println("Error al guardar el servicio");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al guardar el servicio");
        }
        return "redirect:/servicios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean eliminado = servicioDao.eliminar(id);
        redirectAttributes.addFlashAttribute(eliminado ? "mensaje" : "error",
                eliminado ? "Servicio eliminado correctamente"
                        : "No fue posible eliminar el servicio; verifique si está asignado a una orden");
        return "redirect:/servicios";
    }
}
