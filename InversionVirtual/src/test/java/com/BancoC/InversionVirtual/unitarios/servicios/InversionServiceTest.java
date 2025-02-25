package com.BancoC.InversionVirtual.unitarios.servicios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.BancoC.InversionVirtual.modelos.InversionVirtual;

public class InversionServiceTest extends ConfigServiceTests {
    
    @Test
    void nuevaInversion() throws Exception {
        InversionVirtual inversionGuardada = service.nuevaInversion(inversionJohana1);

        verify(micro, times(1)).obtenerCuenta(1L);
        verify(micro, times(1)).obtenerCuenta(inversionJohana1.getCuenta().getCuentaId());
        verify(micro, times(1)).transaccion(transaccionInversion1);
        verify(repository, times(1)).save(inversionJohana1);
        assertTrue(inversionGuardada.getTransaccionEnvioId() > 0);
        this.validarInversion(inversionBD1, inversionGuardada);
    }

    @Test
    void obtenerInversion() {
        Long inversionId = inversionBD1.getInversionId();
        InversionVirtual inversionObtenida = service.obtenerInversion(inversionId);

        verify(repository, times(1)).findById(inversionId);
        this.validarInversion(inversionBD1, inversionObtenida);
    }
    

    @Test
    void obtenerInversionNoExiste() {
        InversionVirtual inversionObtenida = service.obtenerInversion(9999l);

        verify(repository, times(1)).findById(9999l);
        assertNull(inversionObtenida);
    }

    @Test
    void nuevaInversionSinCuenta() {
        Exception exception = assertThrows(
            Exception.class,
            () -> service.nuevaInversion(inversionFalsa)
        );

        assertEquals("No hay una cuenta asociada a la inversión", exception.getMessage());
    }

    @Test
    void nuevaInversion4xx() throws Exception {
        inversionJohana2.setCuentaOrigenId(500L);
        Exception exception = assertThrows(
            Exception.class,
            () -> service.nuevaInversion(inversionJohana2)
        );

        verify(micro, times(1)).obtenerCuenta(inversionJohana2.getCuentaOrigenId());
        assertEquals("Ocurrió una falla HTTP 404 NOT_FOUND", exception.getMessage());        
    }

    @Test
    void reclamarInversion() throws Exception {
        Boolean reclamo = service.reclamarInversion(inversionBD2.getInversionId());

        assertTrue(reclamo);
        verify(micro, times(1)).transaccion(transaccionReclamoMicro2);
    }

    @Test
    void reclamarInversionNoBD() {
        Exception exception = assertThrows(
            Exception.class,
            () -> service.reclamarInversion(51L)
        );

        verify(repository, times(1)).findById(51L);
        assertEquals("Alerta de fraude: no existe la inversión en la base de datos", 
            exception.getMessage());
    }

    @Test
    void reclamarInversionErrorTransaccion() {
        Exception exception = assertThrows(
            Exception.class,
            () -> service.reclamarInversion(52L)
        );

        verify(repository, times(1)).findById(52L);
        assertEquals("Error en la transacción de reclamo", exception.getMessage());
    }

    @Test
    void obtenerInversiones() {
        List<InversionVirtual> inversiones = service.obtenerInversiones(Johana.getClienteId());
        
        assertNotNull(inversiones);
        assertEquals(2, inversiones.size());
        assertEquals(inversionBD1, inversiones.get(0));
        assertEquals(inversionBD2, inversiones.get(1));
        
    }

}
