package com.BancoC.InversionVirtual.unitarios.servicios;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();  //=> trae los objetos de pruebas

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
}
