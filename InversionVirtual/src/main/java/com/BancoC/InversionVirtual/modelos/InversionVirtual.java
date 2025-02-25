package com.BancoC.InversionVirtual.modelos;

import java.time.LocalDate;

import com.BancoC.InversionVirtual.modelos.ext.Cliente;
import com.BancoC.InversionVirtual.modelos.ext.CuentaBancaria;
import com.BancoC.InversionVirtual.modelos.ext.Transaccion;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InversionVirtual {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long inversionId;

    @Transient
    private Transaccion transaccionEnvio;    //Desconocido
    private Long transaccionEnvioId;

    @Transient
    private Transaccion transaccionReclamo; //Desconocido
    private Long transaccionReclamoId;

    @Transient
    private CuentaBancaria cuenta;
    private Long cuentaOrigenId;   

    @Transient
    private Cliente cliente;
    private Long clienteId;

    private Double valor;
    private Double tasa;
    private LocalDate fechaCreacion;
    private LocalDate tiempoDuracion;
}
