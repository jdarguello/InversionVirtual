package com.BancoC.InversionVirtual.modelos.ext;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CuentaBancaria {
    private Long cuentaId;
    private String numeroCuenta;
    private Double saldo;
    private LocalDate fechaCreacion;
}
