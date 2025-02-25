package com.BancoC.InversionVirtual.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.BancoC.InversionVirtual.modelos.InversionVirtual;
import com.BancoC.InversionVirtual.modelos.ext.CuentaBancaria;
import com.BancoC.InversionVirtual.modelos.ext.Transaccion;
import com.BancoC.InversionVirtual.repositories.InversionVirtualRepository;
import com.BancoC.InversionVirtual.servicios.contratos.InversionOperaciones;
import com.BancoC.InversionVirtual.servicios.contratos.MicroservicioCuentas;

@Service
public class InversionService implements InversionOperaciones {

    private final MicroservicioCuentas micro;

    private final InversionVirtualRepository repository;

    //@Value("${integraciones.cuenta-bancaria-corporativa.id}:1")
    private Long cuentaCorporativaId; 

    public InversionService(MicroservicioCuentas micro,
        InversionVirtualRepository repository,
        Long cuentaCorporativaId) {
            this.micro = micro;
            this.repository = repository;
            this.cuentaCorporativaId = cuentaCorporativaId;
    }

    @Override
    public InversionVirtual nuevaInversion(InversionVirtual inversionVirtual) throws Exception{
        //Validación de la cuenta bancaria de origen
        this.validarCuentaBancaria(inversionVirtual);

        //Generación y envío de la transacción
        inversionVirtual.setTransaccionEnvio(this.generarTransaccion(inversionVirtual, false));
        this.enviarTransaccion(inversionVirtual);

        //Registro de la inversión
        return repository.save(inversionVirtual);
    }

    @Override
    public InversionVirtual obtenerInversion(Long inversionId) {
        Optional<InversionVirtual> inversionObtenida = repository.findById(inversionId);
        if (inversionObtenida.isEmpty()) {
            return null;
        }
        return inversionObtenida.get();
    }

    /**
     * Debe validar:
     *  1. Que la inversión exista en BD
     *  2. Que la transacción de reclamo no se haya ejecutado
          * @throws Exception 
          */
         @Override
         public Boolean reclamarInversion(Long inversionId) throws Exception {
        //Obtener datos de la inversión
        InversionVirtual inversion = this.obtenerInversion(inversionId);
        if (inversion == null) {
            throw new Exception("Alerta de fraude: no existe la inversión en la base de datos");
        }

        //Generar la transacción y enviarla
        inversion.setTransaccionReclamo(this.generarTransaccion(inversion, true));
        this.solicitarReclamo(inversion);

        return true;
    }

    @Override
    public List<InversionVirtual> obtenerInversiones(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    private void validarCuentaBancaria(InversionVirtual inversion) throws Exception {
        //1. Validar que haya una cuenta vinculada
        if(inversion.getCuentaOrigenId() == null) {
            throw new Exception("No hay una cuenta asociada a la inversión");
        }

        //2. Contrastar con el microservicio que la cuenta exista
        ResponseEntity<CuentaBancaria> response = micro.obtenerCuenta(inversion.getCuentaOrigenId());
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Ocurrió una falla HTTP " + response.getStatusCode());
        }
        inversion.setCuenta(response.getBody());
    }

    private CuentaBancaria obtenerCuentaCorporativa() throws Exception {
        ResponseEntity<CuentaBancaria> response = micro.obtenerCuenta(this.cuentaCorporativaId);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Ocurrió un error cargando la cuenta corporativa");
        }
        return response.getBody();
    }

    private Transaccion generarTransaccion(InversionVirtual inversion, Boolean reclamar) throws Exception {
        CuentaBancaria cuentaCorporativa = this.obtenerCuentaCorporativa();
        Transaccion transaccion = Transaccion.builder()
            .monto(inversion.getValor())
            .cuentaOrigen(inversion.getCuenta())
            .cuentaDestino(cuentaCorporativa)
            .build();
        if (reclamar) {
            transaccion.setCuentaOrigen(cuentaCorporativa);
            transaccion.setCuentaDestino(inversion.getCuenta());
            transaccion.setMonto(valorReclamo(inversion.getTasa(), inversion.getValor()));
        }
        return transaccion;
    }

    private Double valorReclamo(Double tasa, Double valorInversion) {
        return (1+tasa/100)*valorInversion;
    }

    private void enviarTransaccion(InversionVirtual inversion) throws Exception {
        ResponseEntity<Transaccion> response = micro.transaccion(inversion.getTransaccionEnvio());
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Error en la transacción de envío");
        }
        inversion.setTransaccionEnvio(response.getBody());
        inversion.setTransaccionEnvioId(response.getBody().getTransaccionId());
    }

    private void solicitarReclamo(InversionVirtual inversion) throws Exception {
        ResponseEntity<Transaccion> response = micro.transaccion(inversion.getTransaccionReclamo());
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Error en la transacción de reclamo");
        }
        inversion.setTransaccionReclamo(response.getBody());
        inversion.setTransaccionReclamoId(response.getBody().getTransaccionId());
    }
    
}
