package com.BancoC.InversionVirtual.integracion;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

@Import(IntegracionConfigTest.IntegracionConfig.class)
public class InversionIntegracionTest extends IntegracionConfigTest {
    
    @Test
    void nuevaInversion() {
        
    }

}
