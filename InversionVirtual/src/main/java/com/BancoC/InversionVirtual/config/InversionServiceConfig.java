package com.BancoC.InversionVirtual.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.BancoC.InversionVirtual.repositories.InversionVirtualRepository;
import com.BancoC.InversionVirtual.servicios.InversionService;
import com.BancoC.InversionVirtual.servicios.contratos.InversionOperaciones;
import com.BancoC.InversionVirtual.servicios.contratos.MicroservicioCuentas;

@Configuration
public class InversionServiceConfig {
    
    @Value("${integraciones.cuenta-bancaria-corporativa.id}")
    private String cuentaCorporativaId; 

    @Bean
    public InversionOperaciones service(
        MicroservicioCuentas micro,
        InversionVirtualRepository repository
    ) {
        return new InversionService(micro, repository, Long.parseLong(cuentaCorporativaId));
    }
    

}
