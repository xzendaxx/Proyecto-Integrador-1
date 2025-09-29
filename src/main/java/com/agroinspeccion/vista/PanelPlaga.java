package com.agroinspeccion.vista;

import com.agroinspeccion.controlador.BaseControlador;
import com.agroinspeccion.modelo.Plaga;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

/**
 * Panel CRUD para plagas monitoreadas.
 */
public class PanelPlaga extends JPanel {

    private final BaseControlador<Plaga> controlador;
    private final JTextField txtNombreComun = new JTextField();
    private final JTextField txtNombreCientifico = new JTextField();
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre común", "Nombre científico"}, 0);
    private final JTable tabla = new JTable(modeloTabla);

    public PanelPlaga(BaseControlador<Plaga> controlador) {
        this.controlador = controlador;
        inicializar();
        cargarDatos();
    }

    private void inicializar() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new GridLayout(2, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos de la plaga"));
        formulario.add(new JLabel("Nombre común:"));
        formulario.add(txtNombreComun);
        formulario.add(new JLabel("Nombre científico:"));
        formulario.add(txtNombreCientifico);
        add(formulario, BorderLayout.NORTH);

        JPanel acciones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnRefrescar = new JButton("Refrescar");
        acciones.add(btnGuardar);
        acciones.add(btnActualizar);
        acciones.add(btnEliminar);
        acciones.add(btnLimpiar);
        acciones.add(btnRefrescar);
        add(acciones, BorderLayout.CENTER);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        add(new JScrollPane(tabla), BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnRefrescar.addActionListener(e -> cargarDatos());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
    }

    private void guardar() {
        try {
            Plaga plaga = new Plaga(
                    txtNombreComun.getText(),
                    txtNombreCientifico.getText());
            controlador.crear(plaga);
            cargarDatos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Plaga registrada");
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una plaga");
            return;
        }
        try {
            Plaga plaga = new Plaga(
                    Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString()),
                    txtNombreComun.getText(),
                    txtNombreCientifico.getText());
            controlador.actualizar(plaga);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Plaga actualizada");
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una plaga");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar plaga?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
                controlador.eliminar(id);
                cargarDatos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Plaga eliminada");
            } catch (SQLException ex) {
                mostrarError(ex);
            }
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            txtNombreComun.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtNombreCientifico.setText(modeloTabla.getValueAt(fila, 2) != null ? modeloTabla.getValueAt(fila, 2).toString() : "");
        }
    }

    private void limpiarFormulario() {
        txtNombreComun.setText("");
        txtNombreCientifico.setText("");
        tabla.clearSelection();
    }

    private void cargarDatos() {
        try {
            modeloTabla.setRowCount(0);
            for (Plaga plaga : controlador.listar()) {
                modeloTabla.addRow(new Object[]{
                        plaga.getId(),
                        plaga.getNombreComun(),
                        plaga.getNombreCientifico()
                });
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
