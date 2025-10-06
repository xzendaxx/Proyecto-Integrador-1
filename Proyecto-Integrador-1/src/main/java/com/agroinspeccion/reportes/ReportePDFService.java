package com.agroinspeccion.reportes;

import com.agroinspeccion.modelo.DetalleInspeccionDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio que construye reportes PDF utilizando la librería iText.
 */
public class ReportePDFService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Genera un reporte PDF con los datos de inspecciones suministrados.
     *
     * @param detalles    información a incluir en el reporte
     * @param rutaArchivo ruta de salida del archivo PDF
     * @throws IOException        si ocurre un error al escribir el archivo
     * @throws DocumentException  si ocurre un error al generar el documento
     */
    public void generarReporteInformes(List<DetalleInspeccionDTO> detalles, String rutaArchivo)
            throws IOException, DocumentException {
        Document document = new Document();
        try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
            PdfWriter.getInstance(document, fos);
            document.open();
            agregarEncabezado(document);
            agregarTabla(document, detalles);
        } finally {
            document.close();
        }
    }

    private void agregarEncabezado(Document document) throws DocumentException {
        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph titulo = new Paragraph("Reporte de Inspecciones Fitosanitarias", tituloFont);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        titulo.setSpacingAfter(20f);
        document.add(titulo);
    }

    private void agregarTabla(Document document, List<DetalleInspeccionDTO> detalles) throws DocumentException {
        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10f);
        tabla.setWidths(new float[]{2f, 3f, 2f, 2f, 2f, 3f});

        agregarCeldaEncabezado(tabla, "Lote");
        agregarCeldaEncabezado(tabla, "Cultivo");
        agregarCeldaEncabezado(tabla, "Fecha");
        agregarCeldaEncabezado(tabla, "Total Plantas");
        agregarCeldaEncabezado(tabla, "Plantas Afectadas");
        agregarCeldaEncabezado(tabla, "% Incidencia");

        for (DetalleInspeccionDTO detalle : detalles) {
            tabla.addCell(detalle.getNumeroLote());
            tabla.addCell(detalle.getEspecie());
            tabla.addCell(detalle.getFecha().format(FORMATO_FECHA));
            tabla.addCell(String.valueOf(detalle.getPlantasTotales()));
            tabla.addCell(String.valueOf(detalle.getPlantasAfectadas()));
            tabla.addCell(String.format("%.2f%% - %s", detalle.getPorcentajeIncidencia(), detalle.getTecnico()));
        }

        document.add(tabla);
    }

    private void agregarCeldaEncabezado(PdfPTable tabla, String texto) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        PdfPCell celda = new PdfPCell(new Phrase(texto, font));
        celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        tabla.addCell(celda);
    }
}
