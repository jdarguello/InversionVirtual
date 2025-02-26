package com.BancoC.InversionVirtual.controladores;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BancoC.InversionVirtual.modelos.InversionVirtual;
import com.BancoC.InversionVirtual.servicios.contratos.InversionOperaciones;

@RestController
@RequestMapping("/api/inversion")
public class InversionControlador {
    
    private InversionOperaciones operaciones;

    public InversionControlador (InversionOperaciones operaciones) {
        this.operaciones = operaciones;
    }

    @PostMapping
    public ResponseEntity<Object> nuevaInversion(
        @RequestBody InversionVirtual inversion
    ) throws Exception {
        InversionVirtual inversionObtenida = null;
        try {
            inversionObtenida = operaciones.nuevaInversion(inversion);
        } catch (Exception exception) {
            return this.respuestaError(exception, inversion);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(inversionObtenida); 
    }

    @PutMapping("{inversionId}")
    public ResponseEntity<Object> reclamarInversion(
        @PathVariable("inversionId") Long inversionId
    ) throws Exception {
        Boolean reclamacion = false;
        try {
            reclamacion = operaciones.reclamarInversion(inversionId);
        } catch (Exception exception) {
            return this.respuestaError(exception, inversionId);
        }
        if (reclamacion) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("{inversionId}")
    public ResponseEntity<InversionVirtual> obtenerInversion(
        @PathVariable("inversionId") Long inversionId
    ) {
        InversionVirtual inversionObtenida = operaciones.obtenerInversion(inversionId);
        if (inversionObtenida == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(inversionObtenida);
    }

    @GetMapping("/todas")
    public ResponseEntity<List<InversionVirtual>> obtenerInversiones(
        @RequestParam("clienteId") Long clienteId
    ) {
        List<InversionVirtual> inversiones = operaciones.obtenerInversiones(clienteId);
        System.out.println(inversiones);
        if (inversiones == null || inversiones.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(inversiones);
    }

    private ResponseEntity<Object> respuestaError(Exception exception, Object input) {
        return ResponseEntity.badRequest().body(Map.of(
            "Mensaje de error", exception.getMessage(),
            "Hora", LocalDateTime.now().toString(),
            "Datos de la petición", input.toString()
        ));
    }

}
