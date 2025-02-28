package com.BancoC.InversionVirtual.integracion;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;

import com.BancoC.InversionVirtual.modelos.InversionVirtual;
import com.BancoC.InversionVirtual.modelos.ext.CuentaBancaria;
import com.fasterxml.jackson.core.type.TypeReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

public class InversionIntegracionTest extends IntegracionConfigTest {
    
    @Test
    void nuevaInversion201() throws Exception {
        String requestBody = objectMapper.writeValueAsString(inversionJohana1);

        MvcResult response = mockMvc.perform(
            post("/api/inversion")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isCreated())
        .andReturn();

        InversionVirtual inversionNueva = objectMapper.readValue(
            response.getResponse().getContentAsString(),
            InversionVirtual.class
        );

        //Validaciones
        this.validarInversion(inversionBD1, inversionNueva);

        //Cuentas obtenidas desde el microservicio de cuentas
        ResponseEntity<CuentaBancaria> origenResponse = micro.obtenerCuenta(101L);
        CuentaBancaria cuentaOrigen = origenResponse.getBody();

        assertAll(
            () -> assertEquals("1924823928", cuentaOrigen.getNumeroCuenta()),
            () -> assertEquals(10_000_000.0 - 500_000.0, cuentaOrigen.getSaldo())
        );        
    }

    @Test
    void obtenerInversion200() throws Exception {
        MvcResult response = this.mockMvc.perform(
            get("/api/inversion/{inversionId}", inversionJohana2.getInversionId()))
            .andExpect(status().isOk())
            .andReturn();

        InversionVirtual inversionObtenida = objectMapper.readValue(
            response.getResponse().getContentAsString(),
            InversionVirtual.class
        );

        assertNotNull(inversionObtenida);
        this.validarInversion(inversionJohana2, inversionObtenida);
    }

    @Test
    void obtenerInversion404() throws Exception {
        mockMvc.perform(
            get("/api/inversion/{inversionId}", 500L))
            .andExpect(status().isNotFound());
    }

    @Test
    void obtenerInversiones200() throws Exception {
        MvcResult response = mockMvc.perform(
            get("/api/inversion/todas")
            .param("clienteId", "" + Johana.getClienteId()))
            .andExpect(status().isOk())
            .andReturn();

        List<InversionVirtual> inversiones = objectMapper.readValue(
            response.getResponse().getContentAsString(),
            new TypeReference<List<InversionVirtual>>() {}
        );

        assertNotNull(inversiones);
        assertEquals(1, inversiones.size());
        this.validarInversion(inversionJohana2, inversiones.get(0));
    }

    @Test
    void obtenerInversiones404() throws Exception {
        mockMvc.perform(
            get("/api/inversion/todas")
            .param("clienteId", "" + 9999L))
            .andExpect(status().isNotFound());
    }

}
