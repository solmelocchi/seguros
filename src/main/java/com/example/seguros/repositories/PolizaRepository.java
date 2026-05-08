package com.example.seguros.repositories;

import com.example.seguros.entities.Poliza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PolizaRepository extends JpaRepository<Poliza, Long> {

    List<Poliza> findByCompaniaId(Long companiaId);

    List<Poliza> findByCompaniaNombre(String nombre);

    // Pólizas que vencen entre dos fechas (para aviso de renovación)
    List<Poliza> findByFechaVencimientoBetween(LocalDate desde, LocalDate hasta);

    // Pólizas cuyo día de cobro es hoy
    // El día de cobro = día del mes de la fecha de vigencia
    @Query("SELECT p FROM Poliza p WHERE " +
            "FUNCTION('DAY', p.fechaVigencia) = ?1")
    List<Poliza> findByDiaCobro(int dia);
}