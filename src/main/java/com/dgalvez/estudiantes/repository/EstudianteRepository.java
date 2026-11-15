package com.dgalvez.estudiantes.repository;

import com.dgalvez.estudiantes.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    boolean existsByCarneIgnoreCase(String carne);
    boolean existsByCorreoIgnoreCase(String correo);
    List<Estudiante> findAllByOrderByCreadoEnDesc();

    @Query("select e from Estudiante e where " +
           "lower(e.carne) like lower(concat('%', :q, '%')) or " +
           "lower(e.nombres) like lower(concat('%', :q, '%')) or " +
           "lower(e.apellidos) like lower(concat('%', :q, '%')) or " +
           "lower(e.correo) like lower(concat('%', :q, '%')) or " +
           "lower(e.carrera) like lower(concat('%', :q, '%')) " +
           "order by e.creadoEn desc")
    List<Estudiante> buscar(@Param("q") String q);
}
