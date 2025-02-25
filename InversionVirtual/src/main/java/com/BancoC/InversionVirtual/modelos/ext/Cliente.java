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
public class Cliente {
    private Long clienteId;
    private String nombre;
    private LocalDate fechaVinculacion;
}
