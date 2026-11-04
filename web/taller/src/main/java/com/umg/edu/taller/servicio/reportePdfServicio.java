package com.umg.edu.taller.servicio;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class reportePdfServicio {
    public byte[] generar(String titulo, String detalle, List<String> encabezados,
                          List<? extends List<?>> filas, String resumen) throws DocumentException {
        if (encabezados == null || encabezados.isEmpty() || filas == null) {
            throw new IllegalArgumentException("El reporte debe incluir encabezados y filas");
        }
        for (List<?> fila : filas) {
            if (fila == null || fila.size() != encabezados.size()) {
                throw new IllegalArgumentException("Las filas deben coincidir con las columnas del reporte");
            }
        }

        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document documento = new Document(encabezados.size() > 5 ? PageSize.A4.rotate() : PageSize.A4);
        PdfWriter.getInstance(documento, salida);
        documento.open();

        Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font encabezadoFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        documento.add(new Paragraph(titulo, tituloFont));
        if (detalle != null && !detalle.isBlank()) {
            documento.add(new Paragraph(detalle));
        }
        if (resumen != null && !resumen.isBlank()) {
            documento.add(new Paragraph(resumen));
        }
        documento.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(encabezados.size());
        tabla.setWidthPercentage(100);
        tabla.setHeaderRows(1);
        for (String encabezado : encabezados) {
            PdfPCell celda = new PdfPCell(new Phrase(encabezado, encabezadoFont));
            celda.setBackgroundColor(new Color(55, 71, 79));
            celda.setPadding(6);
            tabla.addCell(celda);
        }
        for (List<?> fila : filas) {
            for (Object valor : fila) {
                PdfPCell celda = new PdfPCell(new Phrase(valor == null ? "" : valor.toString()));
                celda.setPadding(5);
                tabla.addCell(celda);
            }
        }
        documento.add(tabla);
        documento.close();
        return salida.toByteArray();
    }
}
