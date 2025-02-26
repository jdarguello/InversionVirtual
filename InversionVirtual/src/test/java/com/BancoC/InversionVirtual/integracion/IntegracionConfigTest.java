package com.BancoC.InversionVirtual.integracion;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import com.BancoC.InversionVirtual.GeneralTest;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegracionConfigTest extends GeneralTest {
    
    @Autowired
    protected MockMvc mockMvc;

    @TestConfiguration(proxyBeanMethods = false)
    public static class IntegracionConfig {

        @Bean
        @ServiceConnection
        public PostgreSQLContainer<?> baseDatosPruebas() {
            return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
        }

        @Bean
        public ComposeContainer microCuentas() {
            return new ComposeContainer(
                new File("src/test/java/com/BancoC/InversionVirtual/compose-test.yaml"))
                .withExposedService("micro-db-1", 5430)
                .withExposedService("micro-1", 8081);
        }

    }

}
