package com.example.seguros.controllers;

import com.example.seguros.entities.Compania;
import com.example.seguros.services.CompaniaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/companias")
public class CompaniaController {

    private final CompaniaService companiaService;

    public CompaniaController(CompaniaService companiaService) {
        this.companiaService = companiaService;
    }

    @GetMapping
    public ResponseEntity<List<Compania>> getAll() {
        return ResponseEntity.ok(companiaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compania> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(companiaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Compania> save(@RequestBody Compania compania) {
        return ResponseEntity.ok(companiaService.save(compania));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companiaService.delete(id);
        return ResponseEntity.ok().build();
    }
}