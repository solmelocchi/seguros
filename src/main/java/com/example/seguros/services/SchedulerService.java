package com.example.seguros.services;

import com.example.seguros.entities.Poliza;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulerService {

    private final PolizaService polizaService;
    private final EmailService emailService;

    public SchedulerService(PolizaService polizaService, EmailService emailService) {
        this.polizaService = polizaService;
        this.emailService = emailService;
    }

    // Todos los días a las 8:00am → avisa pólizas con cobro hoy
    @Scheduled(cron = "0 0 8 * * *")
    public void avisoCobro() {
        System.out.println("⏰ Verificando cobros del día...");
        List<Poliza> polizas = polizaService.findPolizasConCobroHoy();
        if (!polizas.isEmpty()) {
            emailService.enviarAvisoCobro(polizas);
            System.out.println("📧 Aviso de cobro enviado: " + polizas.size() + " pólizas");
        } else {
            System.out.println("✅ Sin cobros para hoy.");
        }
    }

    // El día 25 de cada mes a las 9:00am → avisa pólizas que vencen el mes siguiente
    @Scheduled(cron = "0 0 9 25 * *")
    public void avisoRenovacion() {
        System.out.println("⏰ Verificando renovaciones del próximo mes...");
        List<Poliza> polizas = polizaService.findPolizasQueVencenProximoMes();
        if (!polizas.isEmpty()) {
            emailService.enviarAvisoRenovacion(polizas);
            System.out.println("📧 Aviso de renovación enviado: "
                    + polizas.size() + " pólizas");
        } else {
            System.out.println("✅ Sin renovaciones para el próximo mes.");
        }
    }
}