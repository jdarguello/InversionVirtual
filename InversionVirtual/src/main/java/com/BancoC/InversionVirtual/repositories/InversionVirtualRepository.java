package com.BancoC.InversionVirtual.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BancoC.InversionVirtual.modelos.InversionVirtual;

public interface InversionVirtualRepository extends JpaRepository<InversionVirtual, Long> {

    List<InversionVirtual> findByClienteId(Long clienteId);
    
}
