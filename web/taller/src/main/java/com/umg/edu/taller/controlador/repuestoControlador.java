package com.umg.edu.taller.controlador;

import com.umg.edu.taller.dao.repuestoDao;
import com.umg.edu.taller.modelo.repuesto;
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
@RequestMapping("/repuestos")
public class repuestoControlador {
    private final repuestoDao repuestoDao = new repuestoDao();

    @GetMapping()
    public String listar(Model model) {
        model.addAttribute("repuestos", repuestoDao.listartodos());
        return "repuesto";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        repuesto repuesto = repuestoDao.buscarporid(id);
        if (repuesto == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el repuesto con ID " + id);
            return "redirect:/repuestos";
        }
        model.addAttribute("repuesto", repuesto);
        model.addAttribute("repuestos", repuestoDao.listartodos());
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
            boolean guardado = repuestoDao.buscarporid(idRepuesto) == null
                    ? repuestoDao.insertar(repuesto)
                    : repuestoDao.actualizar(repuesto);
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
        boolean abastecido = repuestoDao.abastecer(id, unidades);
        redirectAttributes.addFlashAttribute(abastecido ? "mensaje" : "error",
                abastecido ? "Inventario abastecido correctamente"
                        : "No fue posible abastecer; indique unidades positivas y un repuesto existente");
        return "redirect:/repuestos";
    }

    @GetMapping("/agotados")
    public String listarAgotados(Model model) {
        model.addAttribute("repuestos", repuestoDao.listarAgotados());
        return "repuesto";
    }

    @GetMapping("/bajo-inventario")
    public String listarBajoInventario(Model model) {
        model.addAttribute("repuestos", repuestoDao.listarBajoInventario());
        return "repuesto";
    }

    @GetMapping("/existencia-hasta-cinco")
    public String listarExistenciaHastaCinco(Model model) {
        model.addAttribute("repuestos", repuestoDao.listarExistenciaHastaCinco());
        return "reporte_repuestos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean eliminado = repuestoDao.eliminar(id);
        redirectAttributes.addFlashAttribute(eliminado ? "mensaje" : "error",
                eliminado ? "Repuesto eliminado correctamente"
                        : "No fue posible eliminar el repuesto; verifique si está asignado a una orden");
        return "redirect:/repuestos";
    }
}
