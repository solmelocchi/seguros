package com.example.seguros.controllers;

import com.example.seguros.entities.Poliza;
import com.example.seguros.services.EmailService;
import com.example.seguros.services.ExcelService;
import com.example.seguros.services.PolizaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/polizas")
public class PolizaController {

    private final PolizaService polizaService;
    private final ExcelService excelService;
    private final EmailService emailService;  // ← AGREGAR

    public PolizaController(PolizaService polizaService,
                            ExcelService excelService,
                            EmailService emailService) {  // ← AGREGAR
        this.polizaService = polizaService;
        this.excelService = excelService;
        this.emailService = emailService;  // ← AGREGAR
    }

    @GetMapping
    public ResponseEntity<List<Poliza>> getAll() {
        return ResponseEntity.ok(polizaService.findAll());
    }

    @GetMapping("/compania/{id}")
    public ResponseEntity<List<Poliza>> getByCompania(@PathVariable Long id) {
        return ResponseEntity.ok(polizaService.findByCompaniaId(id));
    }

    @PostMapping
    public ResponseEntity<Poliza> save(@RequestBody Poliza poliza) {
        return ResponseEntity.ok(polizaService.save(poliza));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        polizaService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportarExcel() {
        try {
            List<Poliza> polizas = polizaService.findAll();
            byte[] excelBytes = excelService.generarExcel(polizas);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment",
                    "COMISIONES_PATRIMONIALES_MELO.xlsx");

            return ResponseEntity.ok().headers(headers).body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/exportar/compania/{id}")
    public ResponseEntity<byte[]> exportarExcelPorCompania(@PathVariable Long id) {
        try {
            List<Poliza> polizas = polizaService.findByCompaniaId(id);
            byte[] excelBytes = excelService.generarExcel(polizas);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment",
                    "COMISIONES_MELO.xlsx");

            return ResponseEntity.ok().headers(headers).body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // PRUEBA: forzar aviso de cobro ahora
    @GetMapping("/test/cobro")
    public ResponseEntity<String> testCobro() {
        try {
            List<Poliza> polizas = polizaService.findPolizasConCobroHoy();
            if (polizas.isEmpty()) {
                return ResponseEntity.ok("Sin cobros para hoy (día "
                        + java.time.LocalDate.now().getDayOfMonth() + ")");
            }
            emailService.enviarAvisoCobro(polizas);
            return ResponseEntity.ok("✅ Email de cobro enviado: "
                    + polizas.size() + " pólizas");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // PRUEBA: forzar aviso de renovación ahora
    @GetMapping("/test/renovacion")
    public ResponseEntity<String> testRenovacion() {
        try {
            List<Poliza> polizas = polizaService.findPolizasQueVencenProximoMes();
            if (polizas.isEmpty()) {
                return ResponseEntity.ok("Sin renovaciones para el próximo mes");
            }
            emailService.enviarAvisoRenovacion(polizas);
            return ResponseEntity.ok("✅ Email de renovación enviado: "
                    + polizas.size() + " pólizas");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
