package com.example.seguros.services;

import com.example.seguros.entities.Poliza;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private static final String DESTINATARIO = "solmelocchi582@gmail.com";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarAvisoRenovacion(List<Poliza> polizas) {
        if (polizas.isEmpty()) return;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(DESTINATARIO);
            helper.setSubject("🔔 MendozaBroker - Pólizas a renovar el próximo mes");
            helper.setText(buildHtmlRenovacion(polizas), true);
            mailSender.send(message);
            System.out.println("✅ Email de renovación enviado a " + DESTINATARIO);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar email de renovación: " + e.getMessage());
        }
    }

    public void enviarAvisoCobro(List<Poliza> polizas) {
        if (polizas.isEmpty()) return;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(DESTINATARIO);
            helper.setSubject("💰 MendozaBroker - Pólizas con cobro HOY");
            helper.setText(buildHtmlCobro(polizas), true);
            mailSender.send(message);
            System.out.println("Email de cobro enviado a " + DESTINATARIO);
        } catch (Exception e) {
            System.err.println("Error al enviar email de cobro: " + e.getMessage());
        }
    }

    private String buildHtmlRenovacion(List<Poliza> polizas) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial,sans-serif; max-width:700px;'>")
                .append("<div style='background:#1a4a5c; padding:20px; border-radius:10px 10px 0 0;'>")
                .append("<h2 style='color:white; margin:0;'>🔔 MendozaBroker</h2>")
                .append("<p style='color:#3aaa85; margin:5px 0 0;'>Pólizas a renovar el próximo mes</p>")
                .append("</div>")
                .append("<div style='background:#f4f6f7; padding:20px;'>")
                .append("<p style='color:#444;'>Hola Sol, estas pólizas <strong>vencen el próximo mes</strong>:</p>")
                .append("<table style='width:100%; border-collapse:collapse; background:white; border-radius:8px; overflow:hidden;'>")
                .append("<thead><tr style='background:#1a4a5c; color:white;'>")
                .append("<th style='padding:12px; text-align:left;'>Cliente</th>")
                .append("<th style='padding:12px; text-align:left;'>Compañía</th>")
                .append("<th style='padding:12px; text-align:left;'>Ramo</th>")
                .append("<th style='padding:12px; text-align:left;'>Póliza</th>")
                .append("<th style='padding:12px; text-align:left;'>Vencimiento</th>")
                .append("</tr></thead><tbody>");

        for (Poliza p : polizas) {
            sb.append("<tr style='border-bottom:1px solid #eee;'>")
                    .append("<td style='padding:11px;'><strong>")
                    .append(p.getCliente()).append("</strong></td>")
                    .append("<td style='padding:11px;'>")
                    .append(p.getCompania().getNombre()).append("</td>")
                    .append("<td style='padding:11px;'>").append(p.getRamo()).append("</td>")
                    .append("<td style='padding:11px;'>").append(p.getNumeroPoliza()).append("</td>")
                    .append("<td style='padding:11px; color:#e74c3c;'><strong>")
                    .append(p.getFechaVencimiento() != null
                            ? p.getFechaVencimiento().format(FMT) : "—")
                    .append("</strong></td></tr>");
        }

        sb.append("</tbody></table>")
                .append("<p style='color:#888; font-size:12px; margin-top:15px;'>")
                .append("Este aviso fue enviado automáticamente por el sistema de gestión de pólizas.")
                .append("</p></div></div>");
        return sb.toString();
    }

    private String buildHtmlCobro(List<Poliza> polizas) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial,sans-serif; max-width:700px;'>")
                .append("<div style='background:#1a4a5c; padding:20px; border-radius:10px 10px 0 0;'>")
                .append("<h2 style='color:white; margin:0;'>MendozaBroker</h2>")
                .append("<p style='color:#3aaa85; margin:5px 0 0;'>Pólizas con cobro hoy</p>")
                .append("</div>")
                .append("<div style='background:#f4f6f7; padding:20px;'>")
                .append("<p style='color:#444;'>Hola Sol, estas pólizas tienen <strong>cobro hoy</strong>:</p>")
                .append("<table style='width:100%; border-collapse:collapse; background:white; border-radius:8px; overflow:hidden;'>")
                .append("<thead><tr style='background:#1a4a5c; color:white;'>")
                .append("<th style='padding:12px; text-align:left;'>Cliente</th>")
                .append("<th style='padding:12px; text-align:left;'>Compañía</th>")
                .append("<th style='padding:12px; text-align:left;'>Ramo</th>")
                .append("<th style='padding:12px; text-align:left;'>Póliza</th>")
                .append("<th style='padding:12px; text-align:left;'>Inicio Vigencia</th>")
                .append("</tr></thead><tbody>");

        for (Poliza p : polizas) {
            sb.append("<tr style='border-bottom:1px solid #eee;'>")
                    .append("<td style='padding:11px;'><strong>")
                    .append(p.getCliente()).append("</strong></td>")
                    .append("<td style='padding:11px;'>")
                    .append(p.getCompania().getNombre()).append("</td>")
                    .append("<td style='padding:11px;'>").append(p.getRamo()).append("</td>")
                    .append("<td style='padding:11px;'>").append(p.getNumeroPoliza()).append("</td>")
                    .append("<td style='padding:11px; color:#3aaa85;'><strong>")
                    .append(p.getFechaVigencia() != null
                            ? p.getFechaVigencia().format(FMT) : "—")
                    .append("</strong></td></tr>");
        }

        sb.append("</tbody></table>")
                .append("<p style='color:#888; font-size:12px; margin-top:15px;'>")
                .append("Este aviso fue enviado automáticamente por el sistema de gestión de pólizas.")
                .append("</p></div></div>");
        return sb.toString();
    }
}