package com.BancoC.InversionVirtual.unitarios.controladores;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.BancoC.InversionVirtual.GeneralTest;
import com.BancoC.InversionVirtual.servicios.contratos.InversionOperaciones;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest
@ActiveProfiles("test_unitarios")
public class ControladorConfigTest extends GeneralTest {

    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @TestConfiguration
    public static class ServiceConfig {

        @Bean
        public InversionOperaciones mockService() {
            return mock(InversionOperaciones.class);
        }

    }

    @Autowired
    protected InversionOperaciones service;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp(); //=> trae los objetos de pruebas

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        //Comportamiento de mocks
        this.nuevaInversion();
        this.obtenerInversion();
        this.obtenerInversiones();
        this.reclamarInversion();
    } 

    private void nuevaInversion() throws Exception {
        when(service.nuevaInversion(inversionJohana1))
            .thenReturn(inversionBD1);
    }

    private void obtenerInversion() {
        when(service.obtenerInversion(inversionBD1.getInversionId()))
            .thenReturn(inversionBD1);
    }

    private void obtenerInversiones() {
        when(service.obtenerInversiones(Johana.getClienteId()))
            .thenReturn(List.of(
                inversionBD1,
                inversionBD2
            ));
    }

    private void reclamarInversion() throws Exception {
        when(service.reclamarInversion(inversionBD2.getInversionId()))
            .thenReturn(true);
    }

}
