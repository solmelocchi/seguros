package com.example.seguros.config;

import com.example.seguros.entities.Compania;
import com.example.seguros.repositories.CompaniaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CompaniaRepository companiaRepository;

    public DataInitializer(CompaniaRepository companiaRepository) {
        this.companiaRepository = companiaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (companiaRepository.count() == 0) {
            companiaRepository.save(new Compania(null, "SANCOR", "229232"));
            companiaRepository.save(new Compania(null, "EXPERTA", "25806"));
            companiaRepository.save(new Compania(null, "GALICIA", "42604"));
            companiaRepository.save(new Compania(null, "MERCANTIL", "95157"));
            companiaRepository.save(new Compania(null, "SAN CRISTOBAL", "7084"));
            companiaRepository.save(new Compania(null, "RIVADAVIA", "20689"));
            companiaRepository.save(new Compania(null, "ZURICH", null));
            companiaRepository.save(new Compania(null, "CHUBB", null));
            companiaRepository.save(new Compania(null, "BREKLEY", null));
            companiaRepository.save(new Compania(null, "INTERWELT", null));
            companiaRepository.save(new Compania(null, "AXXA", null));
            companiaRepository.save(new Compania(null, "ASSISTO", null));
            System.out.println(" Compañías cargadas correctamente.");
        }
    }
}