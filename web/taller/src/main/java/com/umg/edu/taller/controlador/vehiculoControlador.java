package com.umg.edu.taller.controlador;

import com.umg.edu.taller.dao.clienteDao;
import com.umg.edu.taller.dao.vehiculoDao;
import com.umg.edu.taller.modelo.cliente;
import com.umg.edu.taller.modelo.vehiculo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vehiculos")
public class vehiculoControlador {
    private final vehiculoDao vehiculoDao = new vehiculoDao();
    private final clienteDao clienteDao = new clienteDao();

    @GetMapping()
    public String listar(Model model) {
        model.addAttribute("vehiculo", new vehiculo(0, "", "", "", null, "", null));
        model.addAttribute("vehiculos", vehiculoDao.listartodos());
        model.addAttribute("clientes", clienteDao.listartodos());
        return "vehiculo";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        vehiculo vehiculo = vehiculoDao.buscarporid(id);
        if (vehiculo == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el vehículo con ID " + id);
            return "redirect:/vehiculos";
        }

        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("vehiculos", vehiculoDao.listartodos());
        model.addAttribute("clientes", clienteDao.listartodos());
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
        cliente cliente = clienteDao.buscarporid(idCliente);
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("error", "Debe seleccionar un cliente existente");
            return "redirect:/vehiculos";
        }

        vehiculo vehiculo = new vehiculo(idVehiculo, placa, marca, modelo, anio, color, cliente);
        try {
            boolean guardado = vehiculoDao.buscarporid(idVehiculo) == null
                    ? vehiculoDao.insertar(vehiculo)
                    : vehiculoDao.actualizar(vehiculo);
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
        vehiculo vehiculo = vehiculoDao.buscarporid(id);
        if (vehiculo == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el vehículo con ID " + id);
            return "redirect:/vehiculos";
        }
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("ordenes", vehiculoDao.listarHistorialOrdenes(id));
        return "historial_vehiculo";
    }

    @GetMapping("/historial")
    public String historialPorPlaca(@RequestParam String placa, Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("placa", placa);
            model.addAttribute("ordenes", vehiculoDao.listarHistorialOrdenes(placa));
            return "historial_vehiculo";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/vehiculos";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean eliminado = vehiculoDao.eliminar(id);
        redirectAttributes.addFlashAttribute(eliminado ? "mensaje" : "error",
                eliminado ? "Vehículo eliminado correctamente"
                        : "No se pudo eliminar el vehículo; puede tener órdenes asociadas");
        return "redirect:/vehiculos";
    }
}
