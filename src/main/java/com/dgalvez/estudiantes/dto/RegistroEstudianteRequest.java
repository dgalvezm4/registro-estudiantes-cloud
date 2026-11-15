package com.dgalvez.estudiantes.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record RegistroEstudianteRequest(
        @NotBlank(message = "El carné es obligatorio")
        @Size(min = 8, max = 30, message = "El carné debe tener entre 8 y 30 caracteres")
        @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El carné solo acepta letras y números")
        String carne,

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 100, message = "Los nombres no pueden superar 100 caracteres")
        @Pattern(regexp = "^[\\p{L}ÁÉÍÓÚáéíóúÑñÜü ]+$", message = "Los nombres solo aceptan letras y espacios")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 100, message = "Los apellidos no pueden superar 100 caracteres")
        @Pattern(regexp = "^[\\p{L}ÁÉÍÓÚáéíóúÑñÜü ]+$", message = "Los apellidos solo aceptan letras y espacios")
        String apellidos,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Ingrese un correo válido")
        @Size(max = 160, message = "El correo no puede superar 160 caracteres")
        String correo,

        @NotBlank(message = "Seleccione una carrera")
        String carrera,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^(?:\\+502[ -]?)?[2-7]\\d{3}[ -]?\\d{4}$", message = "Use un teléfono guatemalteco válido, por ejemplo 5555-1234")
        String telefono,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe estar en el pasado")
        LocalDate fechaNacimiento,

        @NotBlank(message = "Seleccione un género")
        @Pattern(regexp = "^(Masculino|Femenino|Otro)$", message = "El género seleccionado no es válido")
        String genero,

        @NotBlank(message = "Complete la verificación reCAPTCHA")
        String captchaToken
) {}
