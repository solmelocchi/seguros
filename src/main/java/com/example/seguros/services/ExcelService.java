package com.example.seguros.services;

import com.example.seguros.entities.Poliza;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExcelService {

    public byte[] generarExcel(List<Poliza> polizas) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Hoja3");

            // === ESTILO HEADER ===
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 10);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setWrapText(true);

            // === ESTILO DATOS ===
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // === ESTILO NÚMERO ===
            CellStyle numberStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            numberStyle.setDataFormat(format.getFormat("#,##0.00"));
            numberStyle.setBorderBottom(BorderStyle.THIN);
            numberStyle.setBorderTop(BorderStyle.THIN);
            numberStyle.setBorderLeft(BorderStyle.THIN);
            numberStyle.setBorderRight(BorderStyle.THIN);

            // === FILA HEADER ===
            Row header = sheet.createRow(0);
            String[] columnas = {
                    "PERIODO LIQUIDACION\n(inicio de vigencia)",
                    "COMERCIAL/\nPRODUCTOR",
                    "CLIENTE",
                    "COMPAÑÍA",
                    "RAMO",
                    "CODIGO\nORGANIZADOR",
                    "CODIGO\nPRODUCTOR",
                    "POLIZA/\nREFERENCIA",
                    "COMISION\nTOTAL",
                    "COMISION\nCOMERCIAL",
                    "%",
                    "COMISION\nBROKER",
                    "%"
            };

            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }
            header.setHeight((short) 900);

            // === FILAS DE DATOS ===
            int rowNum = 1;
            for (Poliza p : polizas) {
                Row row = sheet.createRow(rowNum++);

                // Fecha
                Cell fechaCell = row.createCell(0);
                if (p.getFechaVigencia() != null) {
                    fechaCell.setCellValue(p.getFechaVigencia().toString());
                }
                fechaCell.setCellStyle(dataStyle);

                // Productor
                Cell prodCell = row.createCell(1);
                prodCell.setCellValue(p.getProductor() != null ? p.getProductor() : "MELO");
                prodCell.setCellStyle(dataStyle);

                // Cliente
                Cell clienteCell = row.createCell(2);
                clienteCell.setCellValue(p.getCliente() != null ? p.getCliente() : "");
                clienteCell.setCellStyle(dataStyle);

                // Compañía
                Cell companiaCell = row.createCell(3);
                companiaCell.setCellValue(p.getCompania() != null ?
                        p.getCompania().getNombre() : "");
                companiaCell.setCellStyle(dataStyle);

                // Ramo
                Cell ramoCell = row.createCell(4);
                ramoCell.setCellValue(p.getRamo() != null ? p.getRamo() : "");
                ramoCell.setCellStyle(dataStyle);

                // Cod Organizador
                Cell codOrgCell = row.createCell(5);
                codOrgCell.setCellValue(p.getCodigoOrganizador() != null ?
                        p.getCodigoOrganizador() : "MENDOZA BROKER SRL");
                codOrgCell.setCellStyle(dataStyle);

                // Cod Productor
                Cell codProdCell = row.createCell(6);
                codProdCell.setCellValue(p.getCodigoProductor() != null ?
                        p.getCodigoProductor() : "");
                codProdCell.setCellStyle(dataStyle);

                // Nro Póliza
                Cell polizaCell = row.createCell(7);
                polizaCell.setCellValue(p.getNumeroPoliza() != null ?
                        p.getNumeroPoliza() : "");
                polizaCell.setCellStyle(dataStyle);

                // Comisión Total
                Cell comTotalCell = row.createCell(8);
                if (p.getComisionTotal() != null) {
                    comTotalCell.setCellValue(p.getComisionTotal());
                    comTotalCell.setCellStyle(numberStyle);
                } else {
                    comTotalCell.setCellStyle(dataStyle);
                }

                // Comisión Comercial
                Cell comComCell = row.createCell(9);
                if (p.getComisionComercial() != null) {
                    comComCell.setCellValue(p.getComisionComercial());
                    comComCell.setCellStyle(numberStyle);
                } else {
                    comComCell.setCellStyle(dataStyle);
                }

                // % col 10 vacío
                row.createCell(10).setCellStyle(dataStyle);

                // Comisión Broker
                Cell comBrokerCell = row.createCell(11);
                if (p.getComisionBroker() != null) {
                    comBrokerCell.setCellValue(p.getComisionBroker());
                    comBrokerCell.setCellStyle(numberStyle);
                } else {
                    comBrokerCell.setCellStyle(dataStyle);
                }

                // % col 12 vacío
                row.createCell(12).setCellStyle(dataStyle);
            }

            // === ANCHO DE COLUMNAS ===
            sheet.setColumnWidth(0, 5000);
            sheet.setColumnWidth(1, 4000);
            sheet.setColumnWidth(2, 7000);
            sheet.setColumnWidth(3, 4000);
            sheet.setColumnWidth(4, 4000);
            sheet.setColumnWidth(5, 6000);
            sheet.setColumnWidth(6, 4000);
            sheet.setColumnWidth(7, 4000);
            sheet.setColumnWidth(8, 4000);
            sheet.setColumnWidth(9, 4000);
            sheet.setColumnWidth(10, 1500);
            sheet.setColumnWidth(11, 4000);
            sheet.setColumnWidth(12, 1500);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }
}
