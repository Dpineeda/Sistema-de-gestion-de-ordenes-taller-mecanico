package com.umg.edu.taller.controlador;

import com.umg.edu.taller.modelo.orden;
import com.umg.edu.taller.modelo.orden_repuesto;
import com.umg.edu.taller.modelo.repuesto;
import com.umg.edu.taller.servicio.ordenServicio;
import com.umg.edu.taller.servicio.repuestoServicio;
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
    private final ordenServicio ordenServicio = new ordenServicio();
    private final repuestoServicio repuestoServicio = new repuestoServicio();

    @GetMapping()
    public String listar(Model model) {
        agregarDatos(model);
        return "orden_repuesto";
    }

    @GetMapping("/orden/{idOrden}")
    public String listarPorOrden(@PathVariable Integer idOrden, Model model,
                                 RedirectAttributes redirectAttributes) {
        orden orden = ordenServicio.buscarOrden(idOrden);
        if (orden == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró la orden con ID " + idOrden);
            return "redirect:/ordenes";
        }
        model.addAttribute("orden", orden);
        model.addAttribute("asignaciones", ordenServicio.listarRepuestosAsignados(idOrden));
        model.addAttribute("ordenes", ordenServicio.listarOrdenes());
        model.addAttribute("repuestos", repuestoServicio.listarRepuestos());
        return "orden_repuesto";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Integer idOrden,
                          @RequestParam Integer idRepuesto,
                          @RequestParam Integer cantidad,
                          RedirectAttributes redirectAttributes) {
        orden orden = ordenServicio.buscarOrden(idOrden);
        repuesto repuesto = repuestoServicio.buscarRepuesto(idRepuesto);
        if (orden == null || repuesto == null) {
            redirectAttributes.addFlashAttribute("error", "Seleccione una orden y un repuesto existentes");
            return "redirect:/orden-repuestos";
        }

        orden_repuesto asignacion = new orden_repuesto(
                0, orden, repuesto, cantidad, repuesto.getPrecio());
        boolean guardada = ordenServicio.guardarRepuestoEnOrden(asignacion);
        redirectAttributes.addFlashAttribute(guardada ? "mensaje" : "error",
                guardada ? "Repuesto asignado y existencia descontada"
                        : "No se pudo asignar; verifique estado de la orden y existencia disponible");
        return "redirect:/orden-repuestos/orden/" + idOrden;
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        orden_repuesto asignacion = ordenServicio.buscarRepuestoAsignado(id);
        if (asignacion == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el repuesto asignado con ID " + id);
            return "redirect:/orden-repuestos";
        }
        boolean eliminado = ordenServicio.retirarRepuestoDeOrden(id);
        redirectAttributes.addFlashAttribute(eliminado ? "mensaje" : "error",
                eliminado ? "Repuesto retirado y existencia reintegrada"
                        : "Solo se pueden retirar repuestos de órdenes EN PROCESO");
        return "redirect:/orden-repuestos/orden/" + asignacion.getOrden().getid_orden();
    }

    private void agregarDatos(Model model) {
        model.addAttribute("asignaciones", ordenServicio.listarRepuestosAsignados());
        model.addAttribute("ordenes", ordenServicio.listarOrdenes());
        model.addAttribute("repuestos", repuestoServicio.listarRepuestos());
    }
}
