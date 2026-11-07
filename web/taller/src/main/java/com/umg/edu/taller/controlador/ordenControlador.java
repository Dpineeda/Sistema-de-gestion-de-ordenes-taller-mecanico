package com.umg.edu.taller.controlador;

import com.umg.edu.taller.modelo.mecanico;
import com.umg.edu.taller.modelo.orden;
import com.umg.edu.taller.modelo.vehiculo;
import com.umg.edu.taller.servicio.mecanicoServicio;
import com.umg.edu.taller.servicio.ordenServicio;
import com.umg.edu.taller.servicio.reportePdfServicio;
import com.umg.edu.taller.servicio.vehiculoServicio;
import com.lowagie.text.DocumentException;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ordenes")
public class ordenControlador {
    private final ordenServicio ordenServicio = new ordenServicio();
    private final reportePdfServicio reportePdfServicio = new reportePdfServicio();
    private final vehiculoServicio vehiculoServicio = new vehiculoServicio();
    private final mecanicoServicio mecanicoServicio = new mecanicoServicio();

    @GetMapping()
    public String listar(Model model) {
        model.addAttribute("ordenes", ordenServicio.listarOrdenes());
        agregarOpciones(model);
        return "orden";
    }

    @GetMapping("/reporte")
    public String reporte(@RequestParam(required = false) String estado,
                          @RequestParam(required = false) Integer idMecanico,
                          Model model) {
        model.addAttribute("mecanicos", mecanicoServicio.listarMecanicos());
        model.addAttribute("ordenes", List.of());

        if (estado != null && idMecanico != null) {
            model.addAttribute("error", "Seleccione un solo criterio para consultar el reporte");
            return "reporte_ordenes";
        }

        if (estado != null && !estado.isBlank()) {
            try {
                model.addAttribute("estado", estado);
                model.addAttribute("ordenes", ordenServicio.listarPorEstado(estado));
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
            }
        } else if (idMecanico != null) {
            mecanico mecanico = mecanicoServicio.buscarMecanico(idMecanico);
            if (mecanico == null) {
                model.addAttribute("error", "No se encontró el mecánico con ID " + idMecanico);
            } else {
                model.addAttribute("mecanico", mecanico);
                model.addAttribute("ordenes", ordenServicio.listarPorMecanico(idMecanico));
            }
        }

        return "reporte_ordenes";
    }

    @GetMapping("/reporte/pdf")
    public ResponseEntity<byte[]> reportePdf(@RequestParam(required = false) String estado,
                                             @RequestParam(required = false) Integer idMecanico)
            throws DocumentException {
        if ((estado == null || estado.isBlank()) == (idMecanico == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Seleccione un estado o un mecánico para generar el reporte");
        }

        String titulo;
        String detalle;
        List<orden> ordenes;
        if (estado != null && !estado.isBlank()) {
            try {
                ordenes = ordenServicio.listarPorEstado(estado);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            }
            titulo = "Órdenes por estado";
            detalle = "Estado: " + estado;
        } else {
            mecanico mecanico = mecanicoServicio.buscarMecanico(idMecanico);
            if (mecanico == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontró el mecánico con ID " + idMecanico);
            }
            ordenes = ordenServicio.listarPorMecanico(idMecanico);
            titulo = "Órdenes por mecánico";
            detalle = "Mecánico: " + mecanico.getnombre();
        }

        List<? extends List<?>> filas = ordenes.stream()
                .map(orden -> Arrays.<Object>asList(
                        orden.getid_orden(),
                        orden.getvehiculo() == null ? "" : orden.getvehiculo().getPlaca(),
                        orden.getmecanico() == null ? "" : orden.getmecanico().getnombre(),
                        orden.getdescripcion_problema(),
                        orden.getEstado(),
                        orden.precio_total()))
                .toList();
        byte[] pdf = reportePdfServicio.generar(titulo, detalle,
                List.of("ID", "Placa", "Mecánico", "Problema", "Estado", "Total"),
                filas, null);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"reporte_ordenes.pdf\"")
                .body(pdf);
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        orden orden = ordenServicio.buscarOrden(id);
        if (orden == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró la orden con ID " + id);
            return "redirect:/ordenes";
        }
        model.addAttribute("orden", orden);
        model.addAttribute("ordenes", ordenServicio.listarOrdenes());
        agregarOpciones(model);
        return "orden";
    }

    @PostMapping("/guardar")
    public String guardar(    @RequestParam Integer idOrden,
    @RequestParam Integer idVehiculo,
    @RequestParam Integer idMecanico,
    @RequestParam String descripcionProblema,
    RedirectAttributes redirectAttributes) {
        vehiculo vehiculo = vehiculoServicio.buscarVehiculo(idVehiculo);
        mecanico mecanico = mecanicoServicio.buscarMecanico(idMecanico);
        if (vehiculo == null || mecanico == null) {
            redirectAttributes.addFlashAttribute("error", "Seleccione un vehículo y un mecánico existentes");
            return "redirect:/ordenes";
        }

        orden orden = new orden(idOrden, vehiculo, mecanico, descripcionProblema, null);
        try {
            boolean guardada = ordenServicio.guardarOrden(orden);
            redirectAttributes.addFlashAttribute(guardada ? "mensaje" : "error",
                    guardada ? "Orden guardada en estado PENDIENTE"
                            : "No fue posible guardar la orden; las órdenes cerradas no se pueden modificar");
        } catch (Exception e) {
            System.err.println("Error al guardar la orden");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al guardar la orden");
        }
        return "redirect:/ordenes";
    }

    @PostMapping("/iniciar/{id}")
    public String iniciar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean iniciada = ordenServicio.iniciarReparacion(id);
        redirectAttributes.addFlashAttribute(iniciada ? "mensaje" : "error",
                iniciada ? "La reparación inició correctamente"
                        : "Solo se puede iniciar una orden que esté PENDIENTE");
        return "redirect:/ordenes";
    }

    @PostMapping("/finalizar/{id}")
    public String finalizar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean finalizada = ordenServicio.finalizarOrden(id);
        redirectAttributes.addFlashAttribute(finalizada ? "mensaje" : "error",
                finalizada ? "Orden finalizada y comprobante generado"
                        : "No se pudo finalizar; verifique que esté EN PROCESO y tenga servicios asignados");
        return "redirect:/ordenes";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean cancelada = ordenServicio.cancelarOrden(id);
        redirectAttributes.addFlashAttribute(cancelada ? "mensaje" : "error",
                cancelada ? "Orden cancelada y repuestos reintegrados al inventario"
                        : "Solo se pueden cancelar órdenes PENDIENTES o EN PROCESO");
        return "redirect:/ordenes";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean eliminada = ordenServicio.eliminarOrden(id);
        redirectAttributes.addFlashAttribute(eliminada ? "mensaje" : "error",
                eliminada ? "Orden eliminada correctamente"
                        : "No se pudo eliminar; solo se eliminan órdenes PENDIENTES sin asignaciones");
        return "redirect:/ordenes";
    }

    @GetMapping("/estado/{estado}")
    public String listarPorEstado(@PathVariable String estado, Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("estado", estado);
            model.addAttribute("ordenes", ordenServicio.listarPorEstado(estado));
            return "reporte_ordenes";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/ordenes";
        }
    }

    @GetMapping("/mecanico/{id}")
    public String listarPorMecanico(@PathVariable Integer id, Model model,
                                    RedirectAttributes redirectAttributes) {
        mecanico mecanico = mecanicoServicio.buscarMecanico(id);
        if (mecanico == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el mecánico con ID " + id);
            return "redirect:/ordenes";
        }
        model.addAttribute("mecanico", mecanico);
        model.addAttribute("ordenes", ordenServicio.listarPorMecanico(id));
        return "reporte_ordenes";
    }

    private void agregarOpciones(Model model) {
        model.addAttribute("vehiculos", vehiculoServicio.listarVehiculos());
        model.addAttribute("mecanicos", mecanicoServicio.listarMecanicos());
    }
}
