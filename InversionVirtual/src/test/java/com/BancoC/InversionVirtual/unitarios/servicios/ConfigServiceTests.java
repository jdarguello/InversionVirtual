package com.BancoC.InversionVirtual.unitarios.servicios;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.BancoC.InversionVirtual.GeneralTest;
import com.BancoC.InversionVirtual.modelos.InversionVirtual;
import com.BancoC.InversionVirtual.modelos.ext.Transaccion;
import com.BancoC.InversionVirtual.repositories.InversionVirtualRepository;
import com.BancoC.InversionVirtual.servicios.InversionService;
import com.BancoC.InversionVirtual.servicios.contratos.MicroservicioCuentas;

public class ConfigServiceTests extends GeneralTest {
    
    protected MicroservicioCuentas micro;

    protected InversionVirtualRepository repository;

    protected InversionService service;

    protected Transaccion transaccionMicro1;

    protected Transaccion transaccionMicro2;

    protected Transaccion transaccionReclamoMicro1BD;

    protected Transaccion transaccionReclamoMicro2BD;

    protected InversionVirtual inversionBD1;

    protected InversionVirtual inversionBD2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();  //=> trae los objetos de pruebas

        //Objetos 'guardados' en base de datos
        transaccionMicro1 = this.copiarTransaccion(transaccionInversion1, 103L, 
            LocalDateTime.of(2021, 12, 1, 1, 10, 0));
        transaccionMicro2 = this.copiarTransaccion(transaccionInversion2, 104L,
            LocalDateTime.now());

        transaccionReclamoMicro1BD = this.copiarTransaccion(transaccionReclamoMicro1, 105L,
            LocalDateTime.now());
        transaccionReclamoMicro2BD = this.copiarTransaccion(transaccionReclamoMicro2, 106L,
            LocalDateTime.now());
        
        transaccionReclamoMicro2BD.setMonto(transaccionReclamoMicro2.getMonto());

        inversionBD1 = this.copiarInversion(inversionJohana1, 15L);
        inversionBD2 = this.copiarInversion(inversionJohana2, 16L);

        inversionBD1.setTransaccionEnvio(transaccionMicro1);
        inversionBD1.setTransaccionEnvioId(transaccionMicro1.getTransaccionId());
        inversionBD2.setTransaccionEnvio(transaccionMicro2);
        inversionBD2.setTransaccionEnvioId(transaccionMicro2.getTransaccionId());

        //Definición de mocks
        micro = mock(MicroservicioCuentas.class);
        repository = mock(InversionVirtualRepository.class);

        //Inyección de mocks
        service = new InversionService(micro, repository, 1L);

        //Comportamiento de mocks
        this.comportamientosRepository();
        this.comportamientosMicro();
    }

    private void comportamientosMicro() {
        when(micro.obtenerCuenta(1L)).thenReturn(
            ResponseEntity.ok().body(cuentaCorporativaBancoC)
        );
        
        when(micro.obtenerCuenta(inversionJohana1.getCuentaOrigenId()))
            .thenReturn(
                ResponseEntity.ok().body(cuentaAhorrosJohana)
            );
        
        when(micro.obtenerCuenta(inversionJohana2.getCuentaOrigenId()))
            .thenReturn(
                ResponseEntity.ok().body(cuentaAhorrosJohana)
            );

        when(micro.obtenerCuenta(500L))
            .thenReturn(
                ResponseEntity.notFound().build()
            );

        when(micro.transaccion(transaccionInversion1)).thenReturn(
            ResponseEntity.status(HttpStatus.CREATED).body(transaccionMicro1)
        );
        when(micro.transaccion(transaccionInversion2)).thenReturn(
            ResponseEntity.badRequest().build()
        );

        when(micro.transaccion(transaccionReclamoMicro2)).thenReturn(
            ResponseEntity.status(HttpStatus.CREATED).body(transaccionReclamoMicro2BD)
        );

        when(micro.transaccion(
            Transaccion.builder()
                .monto((1+120.0/100)*1_000_000.0)
                .cuentaOrigen(cuentaCorporativaBancoC)
                .cuentaDestino(null)
                .build()
        )).thenReturn(
            ResponseEntity.badRequest().build()
        );

        
    }

    private void comportamientosRepository() {
        when(repository.save(inversionJohana1)).thenReturn(inversionBD1);
        when(repository.findById(inversionBD1.getInversionId()))
            .thenReturn(Optional.of(inversionBD1));
        when(repository.findById(inversionBD2.getInversionId()))
            .thenReturn(Optional.of(inversionBD2));

        when(repository.findById(52L))
            .thenReturn(Optional.of(
                InversionVirtual.builder()
                    .fechaCreacion(LocalDate.now())
                    .tasa(120.0)
                    .valor(1_000_000.0)
                .build()
            ));

        when(repository.findByClienteId(Johana.getClienteId()))
            .thenReturn(List.of(
                inversionBD1,
                inversionBD2
            ));
    }

    protected InversionVirtual copiarInversion(InversionVirtual inversion, Long inversionId) {
        InversionVirtual copia = new InversionVirtual();
        BeanUtils.copyProperties(inversion, copia);
        copia.setInversionId(inversionId);
        return copia;
    }

    protected Transaccion copiarTransaccion (Transaccion transaccion, Long transaccionId, LocalDateTime fechaCreacion) {
        Transaccion copia = new Transaccion();
        BeanUtils.copyProperties(transaccion, copia);
        copia.setTransaccionId(transaccionId);
        copia.setFechaCreacion(fechaCreacion);
        return copia;
    }
}
