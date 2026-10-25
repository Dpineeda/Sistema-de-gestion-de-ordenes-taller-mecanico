package com.umg.edu.taller.controlador;

import com.umg.edu.taller.dao.comprobanteDao;
import com.umg.edu.taller.dao.ordenDao;
import com.umg.edu.taller.modelo.comprobante;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comprobantes")
public class comprobanteControlador {
    private final comprobanteDao comprobanteDao = new comprobanteDao();
    private final ordenDao ordenDao = new ordenDao();

    @GetMapping()
    public String listar(Model model) {
        model.addAttribute("comprobantes", comprobanteDao.listartodos());
        return "comprobante";
    }

    @GetMapping("/{id}")
    public String consultar(@PathVariable Integer id, Model model,
                            RedirectAttributes redirectAttributes) {
        comprobante comprobante = comprobanteDao.buscarporid(id);
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
            model.addAttribute("ingresos", comprobanteDao.ingresosEntre(inicio, fin));
            model.addAttribute("ordenes", ordenDao.listarPorFecha(inicio, fin));
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
}
