package com.dgalvez.estudiantes.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "estudiantes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_estudiante_carne", columnNames = "carne"),
        @UniqueConstraint(name = "uk_estudiante_correo", columnNames = "correo")
})
public class Estudiante {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30) private String carne;
    @Column(nullable = false, length = 100) private String nombres;
    @Column(nullable = false, length = 100) private String apellidos;
    @Column(nullable = false, length = 160) private String correo;
    @Column(nullable = false, length = 100) private String carrera;
    @Column(nullable = false, length = 20) private String telefono;
    @Column(name = "fecha_nacimiento", nullable = false) private LocalDate fechaNacimiento;
    @Column(nullable = false, length = 12) private String genero;
    @Column(name = "creado_en", nullable = false, updatable = false) private LocalDateTime creadoEn;

    @PrePersist
    void antesDeGuardar() {
        creadoEn = LocalDateTime.now();
        carne = carne.trim().toUpperCase();
        correo = correo.trim().toLowerCase();
        nombres = nombres.trim();
        apellidos = apellidos.trim();
        telefono = telefono.trim();
    }

    public Long getId() { return id; }
    public String getCarne() { return carne; }
    public void setCarne(String carne) { this.carne = carne; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
}
