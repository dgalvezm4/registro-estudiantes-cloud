package com.dgalvez.estudiantes.dto;

import com.dgalvez.estudiantes.model.Estudiante;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EstudianteResponse(Long id, String carne, String nombres, String apellidos, String correo,
                                 String carrera, String telefono, LocalDate fechaNacimiento,
                                 String genero, LocalDateTime creadoEn) {
    public static EstudianteResponse desde(Estudiante e) {
        return new EstudianteResponse(e.getId(), e.getCarne(), e.getNombres(), e.getApellidos(), e.getCorreo(),
                e.getCarrera(), e.getTelefono(), e.getFechaNacimiento(), e.getGenero(), e.getCreadoEn());
    }
}
