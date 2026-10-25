package com.umg.edu.taller.controlador;

import com.umg.edu.taller.dao.ordenDao;
import com.umg.edu.taller.dao.orden_repuestoDao;
import com.umg.edu.taller.dao.repuestoDao;
import com.umg.edu.taller.modelo.orden;
import com.umg.edu.taller.modelo.orden_repuesto;
import com.umg.edu.taller.modelo.repuesto;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orden-repuestos")
public class orden_repuestoControlador {
    private final orden_repuestoDao ordenRepuestoDao = new orden_repuestoDao();
    private final ordenDao ordenDao = new ordenDao();
    private final repuestoDao repuestoDao = new repuestoDao();

    @GetMapping()
    public String listar(Model model) {
        agregarDatos(model);
        return "orden_repuesto";
    }

    @GetMapping("/orden/{idOrden}")
    public String listarPorOrden(@PathVariable Integer idOrden, Model model,
                                 RedirectAttributes redirectAttributes) {
        orden orden = ordenDao.buscarporid(idOrden);
        if (orden == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró la orden con ID " + idOrden);
            return "redirect:/ordenes";
        }
        model.addAttribute("orden", orden);
        model.addAttribute("asignaciones", ordenRepuestoDao.listartodos().stream()
                .filter(asignacion -> asignacion.getOrden().getid_orden() == idOrden)
                .collect(Collectors.toList()));
        model.addAttribute("ordenes", ordenDao.listartodos());
        model.addAttribute("repuestos", repuestoDao.listartodos());
        return "orden_repuesto";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Integer idOrdenRepuesto,
                          @RequestParam Integer idOrden,
                          @RequestParam Integer idRepuesto,
                          @RequestParam Integer cantidad,
                          RedirectAttributes redirectAttributes) {
        orden orden = ordenDao.buscarporid(idOrden);
        repuesto repuesto = repuestoDao.buscarporid(idRepuesto);
        if (orden == null || repuesto == null) {
            redirectAttributes.addFlashAttribute("error", "Seleccione una orden y un repuesto existentes");
            return "redirect:/orden-repuestos";
        }

        orden_repuesto asignacion = new orden_repuesto(
                idOrdenRepuesto, orden, repuesto, cantidad, repuesto.getPrecio());
        boolean guardada = ordenRepuestoDao.buscarporid(idOrdenRepuesto) == null
                ? ordenRepuestoDao.insertar(asignacion)
                : ordenRepuestoDao.actualizar(asignacion);
        redirectAttributes.addFlashAttribute(guardada ? "mensaje" : "error",
                guardada ? "Repuesto asignado y existencia descontada"
                        : "No se pudo asignar; verifique estado de la orden y existencia disponible");
        return "redirect:/orden-repuestos/orden/" + idOrden;
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        orden_repuesto asignacion = ordenRepuestoDao.buscarporid(id);
        if (asignacion == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el repuesto asignado con ID " + id);
            return "redirect:/orden-repuestos";
        }
        boolean eliminado = ordenRepuestoDao.eliminar(id);
        redirectAttributes.addFlashAttribute(eliminado ? "mensaje" : "error",
                eliminado ? "Repuesto retirado y existencia reintegrada"
                        : "Solo se pueden retirar repuestos de órdenes EN PROCESO");
        return "redirect:/orden-repuestos/orden/" + asignacion.getOrden().getid_orden();
    }

    private void agregarDatos(Model model) {
        model.addAttribute("asignaciones", ordenRepuestoDao.listartodos());
        model.addAttribute("ordenes", ordenDao.listartodos());
        model.addAttribute("repuestos", repuestoDao.listartodos());
    }
}
