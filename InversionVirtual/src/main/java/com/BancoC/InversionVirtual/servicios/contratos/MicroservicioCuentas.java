package com.BancoC.InversionVirtual.servicios.contratos;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.BancoC.InversionVirtual.modelos.ext.CuentaBancaria;
import com.BancoC.InversionVirtual.modelos.ext.Transaccion;

@FeignClient(name = "micro-cuentas", url = "${integraciones.micro-cuentas.url}")
@RequestMapping("/api/cuenta")
public interface MicroservicioCuentas {
        @GetMapping("{cuentaId}")
        ResponseEntity<CuentaBancaria> obtenerCuenta(
                @PathVariable("cuentaId") Long cuentaId);

        @GetMapping
        ResponseEntity<CuentaBancaria> obtenerCuenta(
                @RequestParam("numeroCuenta") String numeroCuenta);

        @PostMapping("transaccion")
        ResponseEntity<Transaccion> transaccion(@RequestBody Transaccion transaccion);
}
