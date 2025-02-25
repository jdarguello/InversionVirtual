package com.BancoC.InversionVirtual.unitarios.modelos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.BancoC.InversionVirtual.modelos.InversionVirtual;

public class InversionVirtualTests extends ConfigModelosTest {
    
    @Test
    void crearInversion() {
        this.validarInversion(inversionJohana2, 
            repository.save(inversionJohana2));
    }

    @Test
    void obtenerInversion() {
        this.validarInversion(inversionJohana1, 
            repository.findById(inversionJohana1.getInversionId()).get());
    }

    @Test
    void obtenerInversiones() {
        List<InversionVirtual> inversiones = repository.findByClienteId(Johana.getClienteId());

        assertNotNull(inversiones);
        assertEquals(1, inversiones.size());
        this.validarInversion(inversionJohana1, inversiones.get(0));
    }
}
