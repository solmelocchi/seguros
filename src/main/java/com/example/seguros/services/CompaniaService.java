package com.example.seguros.services;

import com.example.seguros.entities.Compania;
import com.example.seguros.repositories.CompaniaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompaniaService {

    private final CompaniaRepository companiaRepository;

    public CompaniaService(CompaniaRepository companiaRepository) {
        this.companiaRepository = companiaRepository;
    }

    public List<Compania> findAll() {
        return companiaRepository.findAll();
    }

    public Compania findById(Long id) {
        return companiaRepository.findById(id).orElseThrow();
    }

    public Compania save(Compania compania) {
        return companiaRepository.save(compania);
    }

    public void delete(Long id) {
        companiaRepository.deleteById(id);
    }
}
