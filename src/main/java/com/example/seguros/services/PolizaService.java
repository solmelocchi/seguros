package com.example.seguros.services;

import com.example.seguros.entities.Poliza;
import com.example.seguros.repositories.PolizaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PolizaService {

    private final PolizaRepository polizaRepository;

    public PolizaService(PolizaRepository polizaRepository) {
        this.polizaRepository = polizaRepository;
    }

    public List<Poliza> findAll() {
        return polizaRepository.findAll();
    }

    public List<Poliza> findByCompaniaId(Long companiaId) {
        return polizaRepository.findByCompaniaId(companiaId);
    }

    public Poliza findById(Long id) {
        return polizaRepository.findById(id).orElseThrow();
    }

    public Poliza save(Poliza poliza) {
        poliza.setProductor("MELO");
        poliza.setCodigoOrganizador("MENDOZA BROKER SRL");

        // Si no se cargó fecha de vencimiento, calcular 1 año desde vigencia
        if (poliza.getFechaVencimiento() == null && poliza.getFechaVigencia() != null) {
            poliza.setFechaVencimiento(poliza.getFechaVigencia().plusYears(1));
        }

        return polizaRepository.save(poliza);
    }

    public void delete(Long id) {
        polizaRepository.deleteById(id);
    }

    // Pólizas que vencen en el mes siguiente
    public List<Poliza> findPolizasQueVencenProximoMes() {
        LocalDate hoy = LocalDate.now();
        LocalDate primerDiaProximoMes = hoy.plusMonths(1).withDayOfMonth(1);
        LocalDate ultimoDiaProximoMes = primerDiaProximoMes
                .withDayOfMonth(primerDiaProximoMes.lengthOfMonth());
        return polizaRepository
                .findByFechaVencimientoBetween(primerDiaProximoMes, ultimoDiaProximoMes);
    }

    // Pólizas con cobro hoy (mismo día del mes que la fecha de vigencia)
    public List<Poliza> findPolizasConCobroHoy() {
        int diaHoy = LocalDate.now().getDayOfMonth();
        return polizaRepository.findByDiaCobro(diaHoy);
    }
}
