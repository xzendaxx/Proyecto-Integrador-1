package com.agroinspeccion.controlador;

import com.agroinspeccion.vista.VentanaPrincipal;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicación de inspecciones fitosanitarias.
 */
public class Aplicacion {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControladorContexto contexto = new ControladorContexto();
            VentanaPrincipal ventana = new VentanaPrincipal(contexto);
            ventana.setVisible(true);
        });
    }
}
