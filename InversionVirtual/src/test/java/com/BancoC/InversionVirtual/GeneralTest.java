package com.BancoC.InversionVirtual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;

import com.BancoC.InversionVirtual.modelos.InversionVirtual;
import com.BancoC.InversionVirtual.modelos.ext.Cliente;
import com.BancoC.InversionVirtual.modelos.ext.CuentaBancaria;
import com.BancoC.InversionVirtual.modelos.ext.Transaccion;


/**
 * Definición de los objetos de pruebas para todas las capas de testing
 */
public class GeneralTest {
 
    protected Cliente Johana;

    protected CuentaBancaria cuentaAhorrosJohana;

    protected CuentaBancaria cuentaCorporativaBancoC;

    protected Transaccion transaccionInversion1;

    protected Transaccion transaccionInversion2;

    protected Transaccion transaccionReclamoMicro1;
                 
    protected Transaccion transaccionReclamoMicro2;

    protected InversionVirtual inversionJohana1;

    protected InversionVirtual inversionJohana2;

    protected InversionVirtual inversionFalsa;

    @BeforeEach
    public void setUp() {
        Johana = Cliente.builder()
            .clienteId(1L)
            .nombre("Joahan Aristizabal")
            .fechaVinculacion(LocalDate.now())
            .build();

        cuentaAhorrosJohana = CuentaBancaria.builder()
            .cuentaId(101L)
            .saldo(10_000_000.0)
            .numeroCuenta("452623526")
            .fechaCreacion(LocalDate.of(2020, 10, 2))
            .build();
        
        cuentaCorporativaBancoC = CuentaBancaria.builder()
            .cuentaId(1L)
            .numeroCuenta("000000001")
            .fechaCreacion(LocalDate.of(2010, 1, 1))
            .build();

        inversionJohana1 = InversionVirtual.builder()
            .cliente(Johana)
            .clienteId(Johana.getClienteId())
            .cuenta(cuentaAhorrosJohana)
            .cuentaOrigenId(cuentaAhorrosJohana.getCuentaId())
            .tasa(11.0)
            .tiempoDuracion(LocalDate.of(0, 12, 1))
            .valor(500_000.0)
            .build();

        inversionJohana2 = InversionVirtual.builder()
            .cliente(Johana)
            .clienteId(Johana.getClienteId())
            .cuenta(cuentaAhorrosJohana)
            .cuentaOrigenId(cuentaAhorrosJohana.getCuentaId())
            .tasa(10.0)
            .tiempoDuracion(LocalDate.of(0, 6, 1))
            .valor(1_000_000.0)
            .build();

        inversionFalsa = InversionVirtual.builder()
            .cliente(Johana)
            .clienteId(Johana.getClienteId())
            .cuenta(cuentaAhorrosJohana)
            .tasa(100.0)
            .tiempoDuracion(LocalDate.of(0,1,1))
            .valor(100_000_000_000.0)
            .build();

        transaccionInversion1 = Transaccion.builder()
            .cuentaOrigen(cuentaAhorrosJohana)
            .cuentaDestino(cuentaCorporativaBancoC)
            .monto(inversionJohana1.getValor())
            .build();

        transaccionInversion2 = Transaccion.builder()
            .cuentaOrigen(cuentaAhorrosJohana)
            .cuentaDestino(cuentaCorporativaBancoC)
            .monto(inversionJohana2.getValor())
            .build();

        transaccionReclamoMicro1 = Transaccion.builder()
            .cuentaOrigen(cuentaCorporativaBancoC)
            .cuentaDestino(cuentaAhorrosJohana)
            .monto((1+inversionJohana1.getTasa()/100)*inversionJohana1.getValor())
            .build();
        
        transaccionReclamoMicro2 = Transaccion.builder()
            .cuentaOrigen(cuentaCorporativaBancoC)
            .cuentaDestino(cuentaAhorrosJohana)
            .monto((1+inversionJohana2.getTasa()/100)*inversionJohana2.getValor())
            .build();
    }

    protected void validarInversion(
        InversionVirtual inversionReferencia,
        InversionVirtual inversionAValidar
    ) {
        assertTrue(inversionAValidar.getInversionId() > 0);
        assertEquals(inversionReferencia.getClienteId(), 
            inversionAValidar.getClienteId());
        assertEquals(inversionReferencia.getCuentaOrigenId(), 
            inversionAValidar.getCuentaOrigenId());
        assertEquals(inversionReferencia.getFechaCreacion(), 
            inversionAValidar.getFechaCreacion());  
        assertEquals(inversionReferencia.getTransaccionEnvioId(), 
            inversionAValidar.getTransaccionEnvioId());  
        assertEquals(inversionReferencia.getValor(), 
            inversionAValidar.getValor());  
        assertEquals(inversionReferencia.getTasa(), 
            inversionAValidar.getTasa());  
        assertEquals(inversionReferencia.getTiempoDuracion(), 
            inversionAValidar.getTiempoDuracion());     
    }
    
}
