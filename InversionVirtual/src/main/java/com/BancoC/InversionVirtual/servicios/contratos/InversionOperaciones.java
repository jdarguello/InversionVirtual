package com.BancoC.InversionVirtual.servicios.contratos;

import java.util.List;

import com.BancoC.InversionVirtual.modelos.InversionVirtual;

public interface InversionOperaciones {
    InversionVirtual nuevaInversion(InversionVirtual inversionVirtual) throws Exception;
    Boolean reclamarInversion(Long inversionId) throws Exception;
    InversionVirtual obtenerInversion(Long inversionId);
    List<InversionVirtual> obtenerInversiones(Long clienteId);
}
