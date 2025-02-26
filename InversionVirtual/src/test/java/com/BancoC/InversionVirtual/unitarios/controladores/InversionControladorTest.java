package com.BancoC.InversionVirtual.unitarios.controladores;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.BancoC.InversionVirtual.modelos.InversionVirtual;
import com.fasterxml.jackson.core.type.TypeReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;

@Import(ControladorConfigTest.ServiceConfig.class)
public class InversionControladorTest extends ControladorConfigTest {
    
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

        this.validarInversion(inversionBD1, inversionNueva);
    }

    @Test
    void nuevaInversion400() throws Exception {
        when(service.nuevaInversion(inversionFalsa))
            .thenThrow(new Exception("Inversión Falsa!"));

        String requestBody = objectMapper.writeValueAsString(inversionFalsa);

        MvcResult response = mockMvc.perform(
            post("/api/inversion")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().is4xxClientError())
        .andReturn();

        Map<String, String> error = objectMapper.readValue(
            response.getResponse().getContentAsString(),
            new TypeReference<Map<String, String>>() {}
        );

        assertEquals("Inversión Falsa!", error.get("Mensaje de error"));
    }

    @Test
    void obtenerInversion200() throws Exception {
        MvcResult response = this.mockMvc.perform(
            get("/api/inversion/{inversionId}", inversionBD1.getInversionId()))
            .andExpect(status().isOk())
            .andReturn();

        InversionVirtual inversionObtenida = objectMapper.readValue(
            response.getResponse().getContentAsString(),
            InversionVirtual.class
        );

        assertNotNull(inversionObtenida);
        this.validarInversion(inversionBD1, inversionObtenida);
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
        assertEquals(2, inversiones.size());
        this.validarInversion(inversionBD1, inversiones.get(0));
        this.validarInversion(inversionBD2, inversiones.get(1));
    }

    @Test
    void obtenerInversiones404() throws Exception {
        mockMvc.perform(
            get("/api/inversion/todas")
            .param("clienteId", "" + 9999L))
            .andExpect(status().isNotFound());
    }

    @Test
    void reclamarInversion200() throws Exception {
        mockMvc.perform(
            put("/api/inversion/{inversionId}", inversionBD2.getInversionId()))
        .andExpect(status().isOk());

        verify(service, times(1)).reclamarInversion(inversionBD2.getInversionId());
    }

    @Test
    void reclamarInversion400() throws Exception {
        when(service.reclamarInversion(500L))
            .thenThrow(new Exception("ERROR DESCONOCIDO"));

        
        MvcResult response = mockMvc.perform(
            put("/api/inversion/{inversionId}", 500L))
        .andExpect(status().is4xxClientError())
        .andReturn();

        Map<String, String> error = objectMapper.readValue(
            response.getResponse().getContentAsString(),
            new TypeReference<Map<String, String>>() {}
        );

        assertEquals("ERROR DESCONOCIDO", error.get("Mensaje de error"));
    }

}