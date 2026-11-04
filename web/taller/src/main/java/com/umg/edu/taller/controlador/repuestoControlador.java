package com.umg.edu.taller.controlador;

import com.umg.edu.taller.modelo.repuesto;
import com.umg.edu.taller.servicio.reportePdfServicio;
import com.umg.edu.taller.servicio.repuestoServicio;
import com.lowagie.text.DocumentException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/repuestos")
public class repuestoControlador {
    private final repuestoServicio repuestoServicio = new repuestoServicio();
    private final reportePdfServicio reportePdfServicio = new reportePdfServicio();

    @GetMapping()
    public String listar(Model model) {
        model.addAttribute("repuestos", repuestoServicio.listarRepuestos());
        return "repuesto";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        repuesto repuesto = repuestoServicio.buscarRepuesto(id);
        if (repuesto == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el repuesto con ID " + id);
            return "redirect:/repuestos";
        }
        model.addAttribute("repuesto", repuesto);
        model.addAttribute("repuestos", repuestoServicio.listarRepuestos());
        return "repuesto";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Integer idRepuesto,
                          @RequestParam String nombre,
                          @RequestParam(required = false) String descripcion,
                          @RequestParam BigDecimal precio,
                          @RequestParam Integer existencia,
                          @RequestParam Integer minimoEnStock,
                          RedirectAttributes redirectAttributes) {
        repuesto repuesto = new repuesto(idRepuesto, nombre, descripcion, precio, existencia, minimoEnStock);
        try {
            boolean guardado = repuestoServicio.guardarRepuesto(repuesto);
            redirectAttributes.addFlashAttribute(guardado ? "mensaje" : "error",
                    guardado ? "Repuesto guardado correctamente"
                            : "No fue posible guardar el repuesto; verifique que los valores no sean negativos");
        } catch (Exception e) {
            System.err.println("Error al guardar el repuesto");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al guardar el repuesto");
        }
        return "redirect:/repuestos";
    }

    @PostMapping("/abastecer/{id}")
    public String abastecer(@PathVariable Integer id, @RequestParam Integer unidades,
                            RedirectAttributes redirectAttributes) {
        boolean abastecido = repuestoServicio.abastecer(id, unidades);
        redirectAttributes.addFlashAttribute(abastecido ? "mensaje" : "error",
                abastecido ? "Inventario abastecido correctamente"
                        : "No fue posible abastecer; indique unidades positivas y un repuesto existente");
        return "redirect:/repuestos";
    }

    @GetMapping("/agotados")
    public String listarAgotados(Model model) {
        model.addAttribute("repuestos", repuestoServicio.listarAgotados());
        return "repuesto";
    }

    @GetMapping("/bajo-inventario")
    public String listarBajoInventario(Model model) {
        model.addAttribute("repuestos", repuestoServicio.listarBajoInventario());
        return "repuesto";
    }

    @GetMapping("/existencia-hasta-cinco")
    public String listarExistenciaHastaCinco(Model model) {
        model.addAttribute("repuestos", repuestoServicio.listarExistenciaHastaCinco());
        return "reporte_repuestos";
    }

    @GetMapping("/reporte/existencia-hasta-cinco/pdf")
    public ResponseEntity<byte[]> reporteExistenciaPdf() throws DocumentException {
        List<repuesto> repuestos = repuestoServicio.listarExistenciaHastaCinco();
        List<? extends List<?>> filas = repuestos.stream()
                .map(repuesto -> Arrays.<Object>asList(
                        repuesto.getId_repuesto(),
                        repuesto.getNombre(),
                        repuesto.getDescripcion(),
                        repuesto.getPrecio(),
                        repuesto.getExistencia(),
                        repuesto.getMinimo_en_stock()))
                .toList();
        byte[] pdf = reportePdfServicio.generar("Repuestos con existencia hasta cinco",
                "Existencia menor o igual a 5 unidades",
                List.of("ID", "Nombre", "Descripción", "Precio", "Existencia", "Mínimo en stock"),
                filas, null);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"repuestos_existencia_baja.pdf\"")
                .body(pdf);
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean eliminado = repuestoServicio.eliminarRepuesto(id);
        redirectAttributes.addFlashAttribute(eliminado ? "mensaje" : "error",
                eliminado ? "Repuesto eliminado correctamente"
                        : "No fue posible eliminar el repuesto; verifique si está asignado a una orden");
        return "redirect:/repuestos";
    }
}
