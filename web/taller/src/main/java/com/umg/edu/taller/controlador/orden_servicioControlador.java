package com.umg.edu.taller.controlador;

import com.umg.edu.taller.dao.ordenDao;
import com.umg.edu.taller.dao.orden_servicioDao;
import com.umg.edu.taller.dao.servicioDao;
import com.umg.edu.taller.modelo.orden;
import com.umg.edu.taller.modelo.orden_servicio;
import com.umg.edu.taller.modelo.servicio;
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
@RequestMapping("/orden-servicios")
public class orden_servicioControlador {
    private final orden_servicioDao ordenServicioDao = new orden_servicioDao();
    private final ordenDao ordenDao = new ordenDao();
    private final servicioDao servicioDao = new servicioDao();

    @GetMapping()
    public String listar(Model model) {
        agregarDatos(model);
        return "orden_servicio";
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
        model.addAttribute("asignaciones", ordenServicioDao.listartodos().stream()
                .filter(asignacion -> asignacion.getOrden().getid_orden() == idOrden)
                .collect(Collectors.toList()));
        model.addAttribute("ordenes", ordenDao.listartodos());
        model.addAttribute("servicios", servicioDao.listartodos());
        return "orden_servicio";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Integer idOrdenServicio,
                          @RequestParam Integer idOrden,
                          @RequestParam Integer idServicio,
                          RedirectAttributes redirectAttributes) {
        orden orden = ordenDao.buscarporid(idOrden);
        servicio servicio = servicioDao.buscarporid(idServicio);
        if (orden == null || servicio == null) {
            redirectAttributes.addFlashAttribute("error", "Seleccione una orden y un servicio existentes");
            return "redirect:/orden-servicios";
        }

        orden_servicio asignacion = new orden_servicio(
                idOrdenServicio, orden, servicio, servicio.getcosto_manodeobra());
        boolean guardada = ordenServicioDao.buscarporid(idOrdenServicio) == null
                ? ordenServicioDao.insertar(asignacion)
                : ordenServicioDao.actualizar(asignacion);
        redirectAttributes.addFlashAttribute(guardada ? "mensaje" : "error",
                guardada ? "Servicio asignado a la orden"
                        : "No se pudo asignar; la orden debe estar EN PROCESO");
        return "redirect:/orden-servicios/orden/" + idOrden;
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        orden_servicio asignacion = ordenServicioDao.buscarporid(id);
        if (asignacion == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el servicio asignado con ID " + id);
            return "redirect:/orden-servicios";
        }
        boolean eliminado = ordenServicioDao.eliminar(id);
        redirectAttributes.addFlashAttribute(eliminado ? "mensaje" : "error",
                eliminado ? "Servicio retirado de la orden"
                        : "Solo se pueden retirar servicios de órdenes EN PROCESO");
        return "redirect:/orden-servicios/orden/" + asignacion.getOrden().getid_orden();
    }

    private void agregarDatos(Model model) {
        model.addAttribute("asignaciones", ordenServicioDao.listartodos());
        model.addAttribute("ordenes", ordenDao.listartodos());
        model.addAttribute("servicios", servicioDao.listartodos());
    }
}
