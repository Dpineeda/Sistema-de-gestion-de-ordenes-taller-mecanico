package com.umg.edu.taller.controlador;

import com.umg.edu.taller.modelo.comprobante;
import com.umg.edu.taller.modelo.orden;
import com.umg.edu.taller.servicio.comprobanteServicio;
import com.umg.edu.taller.servicio.ordenServicio;
import com.umg.edu.taller.servicio.reportePdfServicio;
import com.lowagie.text.DocumentException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/comprobantes")
public class comprobanteControlador {
    private final comprobanteServicio comprobanteServicio = new comprobanteServicio();
    private final ordenServicio ordenServicio = new ordenServicio();
    private final reportePdfServicio reportePdfServicio = new reportePdfServicio();

    @GetMapping()
    public String listar(Model model) {
        model.addAttribute("comprobantes", comprobanteServicio.listarComprobantes());
        return "comprobante";
    }

    @GetMapping("/{id}")
    public String consultar(@PathVariable Integer id, Model model,
                            RedirectAttributes redirectAttributes) {
        comprobante comprobante = comprobanteServicio.buscarComprobante(id);
        if (comprobante == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el comprobante con ID " + id);
            return "redirect:/comprobantes";
        }
        model.addAttribute("comprobante", comprobante);
        return "comprobante";
    }

    @GetMapping("/reportes/ingresos")
    public String ingresos(@RequestParam LocalDateTime inicio,
                           @RequestParam LocalDateTime fin,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("inicio", inicio);
            model.addAttribute("fin", fin);
            model.addAttribute("ingresos", comprobanteServicio.ingresosEntre(inicio, fin));
            model.addAttribute("ordenes", ordenServicio.listarPorFecha(inicio, fin));
            return "reporte_ingresos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/comprobantes";
        } catch (SQLException e) {
            System.err.println("Error al consultar los ingresos del periodo");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al consultar los ingresos");
            return "redirect:/comprobantes";
        }
    }

    @GetMapping("/reportes/ingresos/pdf")
    public ResponseEntity<byte[]> ingresosPdf(@RequestParam LocalDateTime inicio,
                                              @RequestParam LocalDateTime fin)
            throws DocumentException {
        if (inicio == null || fin == null || !inicio.isBefore(fin)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El inicio debe ser anterior al fin del rango");
        }
        try {
            List<orden> ordenes = ordenServicio.listarPorFecha(inicio, fin);
            List<? extends List<?>> filas = ordenes.stream()
                    .map(orden -> Arrays.<Object>asList(
                            orden.getid_orden(),
                            orden.getvehiculo() == null ? "" : orden.getvehiculo().getPlaca(),
                            orden.getmecanico() == null ? "" : orden.getmecanico().getnombre(),
                            orden.getdescripcion_problema(),
                            orden.getEstado(),
                            orden.precio_total()))
                    .toList();
            String detalle = "Desde: " + inicio + " | Hasta (exclusivo): " + fin;
            String resumen = "Ingresos totales: " + comprobanteServicio.ingresosEntre(inicio, fin);
            byte[] pdf = reportePdfServicio.generar("Ingresos por reparaciones",
                    detalle, List.of("ID", "Placa", "Mecánico", "Problema", "Estado", "Total"),
                    filas, resumen);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"ingresos_reparaciones.pdf\"")
                    .body(pdf);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (SQLException e) {
            System.err.println("Error al generar el PDF de ingresos");
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al generar el reporte de ingresos", e);
        }
    }
}
