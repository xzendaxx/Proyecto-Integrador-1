package com.agroinspeccion.vista;

import com.agroinspeccion.controlador.ControladorContexto;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.Dimension;

/**
 * Ventana principal de la aplicación de inspecciones fitosanitarias.
 */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(ControladorContexto contexto) {
        super("Sistema de Inspecciones Fitosanitarias");
        configurarVentana(contexto);
    }

    private void configurarVentana(ControladorContexto contexto) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1100, 700));

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Productores", new PanelProductor(contexto.getProductorControlador()));
        pestañas.addTab("Propietarios", new PanelPropietario(contexto.getPropietarioControlador()));
        pestañas.addTab("Asistentes técnicos", new PanelAsistenteTecnico(contexto.getAsistenteControlador()));
        pestañas.addTab("Lugares de producción", new PanelLugarProduccion(
                contexto.getLugarControlador(), contexto.getProductorControlador()));
        pestañas.addTab("Predios", new PanelPredio(
                contexto.getPredioControlador(),
                contexto.getPropietarioControlador(),
                contexto.getLugarControlador()));
        pestañas.addTab("Cultivos", new PanelCultivo(
                contexto.getCultivoControlador(),
                contexto.getPredioControlador()));
        pestañas.addTab("Lotes", new PanelLote(
                contexto.getLoteControlador(),
                contexto.getCultivoControlador()));
        pestañas.addTab("Plagas", new PanelPlaga(contexto.getPlagaControlador()));
        pestañas.addTab("Informes fitosanitarios", new PanelInformeFitosanitario(
                contexto.getInformeFitoControlador(),
                contexto.getLoteControlador(),
                contexto.getPlagaControlador(),
                contexto.getAsistenteControlador()));
        pestañas.addTab("Informes de producción", new PanelInformeProduccion(
                contexto.getInformeProduccionControlador(),
                contexto.getLoteControlador()));
        pestañas.addTab("Reportes", new PanelReportes(contexto));

        add(pestañas);
        pack();
        setLocationRelativeTo(null);
    }
}
