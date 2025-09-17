package com.agroinspeccion.vista;

import com.agroinspeccion.controlador.ControladorContexto;
import com.agroinspeccion.modelo.DetalleInspeccionDTO;
import com.agroinspeccion.reportes.ReportePDFService;
import com.itextpdf.text.DocumentException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Panel destinado a la generación de reportes PDF.
 */
public class PanelReportes extends JPanel {

    private final ControladorContexto contexto;
    private final ReportePDFService reporteService = new ReportePDFService();
    private final JTextField txtRuta = new JTextField("reporte_inspecciones.pdf", 30);

    public PanelReportes(ControladorContexto contexto) {
        this.contexto = contexto;
        inicializar();
    }

    private void inicializar() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Generación de reportes"));

        JPanel rutaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rutaPanel.add(new JLabel("Ruta del archivo:"));
        rutaPanel.add(txtRuta);
        JButton btnExplorar = new JButton("Explorar");
        rutaPanel.add(btnExplorar);
        add(rutaPanel, BorderLayout.NORTH);

        JButton btnGenerar = new JButton("Generar reporte PDF");
        add(btnGenerar, BorderLayout.CENTER);

        btnExplorar.addActionListener(e -> seleccionarArchivo());
        btnGenerar.addActionListener(e -> generarReporte());
    }

    private void seleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(txtRuta.getText()));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtRuta.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void generarReporte() {
        try {
            List<DetalleInspeccionDTO> detalles = contexto.getInformeFitoDAO().listarDetalles();
            if (detalles.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay datos de inspecciones para generar el reporte");
                return;
            }
            File archivo = new File(txtRuta.getText());
            reporteService.generarReporteInformes(detalles, archivo.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Reporte generado en: " + archivo.getAbsolutePath());
        } catch (SQLException | IOException | DocumentException ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
