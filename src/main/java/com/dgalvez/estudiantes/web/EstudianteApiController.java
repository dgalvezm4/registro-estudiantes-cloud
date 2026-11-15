package com.dgalvez.estudiantes.web;

import com.dgalvez.estudiantes.dto.*;
import com.dgalvez.estudiantes.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class EstudianteApiController {
    private final EstudianteService service;
    public EstudianteApiController(EstudianteService service) { this.service = service; }

    @PostMapping("/estudiantes")
    public ResponseEntity<Map<String, Object>> registrar(@Valid @RequestBody RegistroEstudianteRequest request) {
        EstudianteResponse estudiante = service.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Estudiante registrado correctamente", "estudiante", estudiante));
    }

    @GetMapping("/estudiantes")
    public List<EstudianteResponse> listar(@RequestParam(required = false) String q) { return service.listar(q); }

    @GetMapping("/health")
    public Map<String, String> health() { return Map.of("estado", "activo"); }
}
