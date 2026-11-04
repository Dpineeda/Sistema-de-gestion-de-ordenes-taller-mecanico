package com.umg.edu.taller.controlador;

import com.umg.edu.taller.modelo.cliente;
import com.umg.edu.taller.modelo.vehiculo;
import com.umg.edu.taller.servicio.clienteServicio;
import com.umg.edu.taller.servicio.reportePdfServicio;
import com.umg.edu.taller.servicio.vehiculoServicio;
import com.lowagie.text.DocumentException;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/vehiculos")
public class vehiculoControlador {
    private final vehiculoServicio vehiculoServicio = new vehiculoServicio();
    private final clienteServicio clienteServicio = new clienteServicio();
    private final reportePdfServicio reportePdfServicio = new reportePdfServicio();

    @GetMapping()
    public String listar(Model model) {
        model.addAttribute("vehiculo", new vehiculo(0, "", "", "", null, "", null));
        model.addAttribute("vehiculos", vehiculoServicio.listarVehiculos());
        model.addAttribute("clientes", clienteServicio.listarClientes());
        return "vehiculo";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        vehiculo vehiculo = vehiculoServicio.buscarVehiculo(id);
        if (vehiculo == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el vehículo con ID " + id);
            return "redirect:/vehiculos";
        }

        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("vehiculos", vehiculoServicio.listarVehiculos());
        model.addAttribute("clientes", clienteServicio.listarClientes());
        return "vehiculo";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam("id_vehiculo") Integer idVehiculo,
                          @RequestParam String placa,
                          @RequestParam(required = false) String marca,
                          @RequestParam(required = false) String modelo,
                          @RequestParam(required = false) Short anio,
                          @RequestParam(required = false) String color,
                          @RequestParam String idCliente,
                          RedirectAttributes redirectAttributes) {
        if (anio == null) {
            redirectAttributes.addFlashAttribute("error", "El año del vehículo es obligatorio");
            return "redirect:/vehiculos";
        }

        cliente cliente = clienteServicio.buscarCliente(idCliente);
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("error", "Debe seleccionar un cliente existente");
            return "redirect:/vehiculos";
        }

        vehiculo vehiculo = new vehiculo(idVehiculo, placa, marca, modelo, anio, color, cliente);
        try {
            boolean guardado = vehiculoServicio.guardarVehiculo(vehiculo);
            redirectAttributes.addFlashAttribute(guardado ? "mensaje" : "error",
                    guardado ? "Vehículo guardado correctamente"
                            : "No fue posible guardar el vehículo; verifique que la placa sea única");
        } catch (Exception e) {
            System.err.println("Error al guardar el vehículo");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al guardar el vehículo");
        }
        return "redirect:/vehiculos";
    }

    @GetMapping("/historial/{id}")
    public String historial(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        vehiculo vehiculo = vehiculoServicio.buscarVehiculo(id);
        if (vehiculo == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el vehículo con ID " + id);
            return "redirect:/vehiculos";
        }
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("placa", vehiculo.getPlaca());
        model.addAttribute("ordenes", vehiculoServicio.listarHistorialOrdenes(id));
        return "historial_vehiculo";
    }

    @GetMapping("/historial")
    public String historialPorPlaca(@RequestParam(required = false) String placa, Model model,
                                    RedirectAttributes redirectAttributes) {
        if (placa == null || placa.isBlank()) {
            model.addAttribute("placa", "");
            model.addAttribute("ordenes", List.of());
            return "historial_vehiculo";
        }

        try {
            model.addAttribute("placa", placa);
            model.addAttribute("ordenes", vehiculoServicio.listarHistorialOrdenes(placa));
            return "historial_vehiculo";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/vehiculos";
        }
    }

    @GetMapping("/historial/pdf")
    public ResponseEntity<byte[]> historialPdf(@RequestParam String placa) throws DocumentException {
        if (placa.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La placa es obligatoria");
        }
        String placaConsultada = placa.trim();
        List<com.umg.edu.taller.modelo.orden> ordenes =
                vehiculoServicio.listarHistorialOrdenes(placaConsultada);
        List<? extends List<?>> filas = ordenes.stream()
                .map(orden -> Arrays.<Object>asList(
                        orden.getid_orden(),
                        orden.getdescripcion_problema(),
                        orden.getmecanico() == null ? "" : orden.getmecanico().getnombre(),
                        orden.getEstado(),
                        orden.precio_total()))
                .toList();
        byte[] pdf = reportePdfServicio.generar("Historial de reparaciones",
                "Placa: " + placaConsultada,
                List.of("ID de orden", "Problema", "Mecánico", "Estado", "Total"),
                filas, null);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"historial_reparaciones.pdf\"")
                .body(pdf);
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean eliminado = vehiculoServicio.eliminarVehiculo(id);
        redirectAttributes.addFlashAttribute(eliminado ? "mensaje" : "error",
                eliminado ? "Vehículo eliminado correctamente"
                        : "No se pudo eliminar el vehículo; puede tener órdenes asociadas");
        return "redirect:/vehiculos";
    }
}
