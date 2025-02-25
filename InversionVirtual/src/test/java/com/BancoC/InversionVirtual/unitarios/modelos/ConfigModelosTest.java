package com.BancoC.InversionVirtual.unitarios.modelos;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.BancoC.InversionVirtual.GeneralTest;
import com.BancoC.InversionVirtual.repositories.InversionVirtualRepository;

@DataJpaTest
@ActiveProfiles("test_unitarios")
public class ConfigModelosTest extends GeneralTest {
    
    @Autowired
    protected InversionVirtualRepository repository;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp(); //=> importa los objetos de pruebas

        inversionJohana1 = repository.save(inversionJohana1);
    }

}
