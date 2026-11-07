package com.umg.edu.taller.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

class PropiedadesJavaBeanTest {

    @Test
    void exponePropiedadesUsadasPorLasPlantillas() {
        servicio servicio = new servicio(1, "Cambio de aceite", new BigDecimal("100.00"));
        BeanWrapper servicioWrapper = new BeanWrapperImpl(servicio);

        assertEquals(1, servicioWrapper.getPropertyValue("id_servicio"));
        assertEquals("Cambio de aceite", servicioWrapper.getPropertyValue("descripcion"));
        assertEquals(new BigDecimal("100.00"), servicioWrapper.getPropertyValue("costo_manodeobra"));

        orden orden = new orden(1, null, null, "Revisión", BigDecimal.ZERO);
        comprobante comprobante = new comprobante(1, null, orden, BigDecimal.ZERO, BigDecimal.ZERO);
        assertEquals(orden, new BeanWrapperImpl(comprobante).getPropertyValue("orden"));
    }

}
