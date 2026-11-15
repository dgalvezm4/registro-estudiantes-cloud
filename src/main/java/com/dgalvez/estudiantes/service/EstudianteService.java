package com.dgalvez.estudiantes.service;

import com.dgalvez.estudiantes.dto.*;
import com.dgalvez.estudiantes.model.Estudiante;
import com.dgalvez.estudiantes.repository.EstudianteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class EstudianteService {
    public static final List<String> CARRERAS = List.of(
            "Ingeniería en sistemas", "Ingeniería industrial", "Administración de empresas",
            "Contaduría pública", "Psicología", "Ingeniería Civil"
    );

    private final EstudianteRepository repository;
    private final CaptchaService captcha;

    public EstudianteService(EstudianteRepository repository, CaptchaService captcha) {
        this.repository = repository;
        this.captcha = captcha;
    }

    @Transactional
    public EstudianteResponse registrar(RegistroEstudianteRequest request) {
        validarReglas(request);
        if (!captcha.verificar(request.captchaToken()))
            throw new IllegalArgumentException("La verificación reCAPTCHA no fue válida");
        if (repository.existsByCarneIgnoreCase(request.carne().trim()))
            throw new IllegalArgumentException("El carné ya se encuentra registrado");
        if (repository.existsByCorreoIgnoreCase(request.correo().trim()))
            throw new IllegalArgumentException("El correo ya se encuentra registrado");

        Estudiante e = new Estudiante();
        e.setCarne(request.carne()); e.setNombres(request.nombres()); e.setApellidos(request.apellidos());
        e.setCorreo(request.correo()); e.setCarrera(request.carrera()); e.setTelefono(request.telefono());
        e.setFechaNacimiento(request.fechaNacimiento()); e.setGenero(request.genero());
        return EstudianteResponse.desde(repository.save(e));
    }

    @Transactional(readOnly = true)
    public List<EstudianteResponse> listar(String q) {
        List<Estudiante> datos = q == null || q.isBlank()
                ? repository.findAllByOrderByCreadoEnDesc() : repository.buscar(q.trim());
        return datos.stream().map(EstudianteResponse::desde).toList();
    }

    private void validarReglas(RegistroEstudianteRequest r) {
        if (!CARRERAS.contains(r.carrera())) throw new IllegalArgumentException("La carrera seleccionada no es válida");
        if (r.fechaNacimiento() != null && r.fechaNacimiento().isAfter(LocalDate.now().minusYears(16)))
            throw new IllegalArgumentException("El estudiante debe tener al menos 16 años");
    }
}
