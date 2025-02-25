package com.BancoC.InversionVirtual.modelos.ext;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaccion {
    private Long transaccionId;
    private Double monto;
    private LocalDateTime fechaCreacion;
    private CuentaBancaria cuentaOrigen;
    private CuentaBancaria cuentaDestino;
}
