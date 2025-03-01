package com.BancoC.InversionVirtual.integracion;

import java.io.File;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.BancoC.InversionVirtual.GeneralTest;
import com.BancoC.InversionVirtual.repositories.InversionVirtualRepository;
import com.BancoC.InversionVirtual.servicios.contratos.MicroservicioCuentas;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class IntegracionConfigTest extends GeneralTest {
    
    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @Autowired
    private InversionVirtualRepository repository;

    @Autowired
    protected MicroservicioCuentas micro;

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> baseDatosPrueba = 
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    @Container
    public static ComposeContainer microCuentas = 
        new ComposeContainer(new File("src/test/java/com/BancoC/InversionVirtual/integracion/microcuentas/compose-test.yaml"))
            .withExposedService("micro-cuentas-db-1", 5432)
            .withExposedService("micro-cuentas", 8080);

    //Configuración de la url de conexión al microservicio de cuentas
    @DynamicPropertySource
    static void conectorMicroCuentas(DynamicPropertyRegistry registry) {
        String microCuentasHost = microCuentas.getServiceHost("micro-cuentas-1", 8080);
        Integer microCuentasPort = microCuentas.getServicePort("micro-cuentas-1", 8080);
        registry.add(
            "integraciones.micro-cuentas.url", 
            () -> "http://" + microCuentasHost + ":" + microCuentasPort
        );
    }

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        //Serializador
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        //Guardar inversión de prueba
        inversionJohana2 =  repository.save(inversionJohana2);
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }
}
