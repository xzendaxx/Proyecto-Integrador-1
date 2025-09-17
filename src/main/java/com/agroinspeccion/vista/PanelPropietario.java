package com.agroinspeccion.vista;

import com.agroinspeccion.controlador.BaseControlador;
import com.agroinspeccion.modelo.Propietario;

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
 * Panel CRUD para propietarios de predios.
 */
public class PanelPropietario extends JPanel {

    private final BaseControlador<Propietario> controlador;
    private final JTextField txtIdentificacion = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtCorreo = new JTextField();
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Identificación", "Nombre", "Teléfono", "Correo"}, 0);
    private final JTable tabla = new JTable(modeloTabla);

    public PanelPropietario(BaseControlador<Propietario> controlador) {
        this.controlador = controlador;
        inicializar();
        cargarDatos();
    }

    private void inicializar() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new GridLayout(4, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del propietario"));
        formulario.add(new JLabel("Identificación:"));
        formulario.add(txtIdentificacion);
        formulario.add(new JLabel("Nombre completo:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Teléfono:"));
        formulario.add(txtTelefono);
        formulario.add(new JLabel("Correo electrónico:"));
        formulario.add(txtCorreo);
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
            Propietario propietario = new Propietario(
                    txtIdentificacion.getText(),
                    txtNombre.getText(),
                    txtTelefono.getText(),
                    txtCorreo.getText());
            controlador.crear(propietario);
            cargarDatos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Propietario registrado correctamente");
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un propietario");
            return;
        }
        try {
            Propietario propietario = new Propietario(
                    Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString()),
                    txtIdentificacion.getText(),
                    txtNombre.getText(),
                    txtTelefono.getText(),
                    txtCorreo.getText());
            controlador.actualizar(propietario);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Propietario actualizado");
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un propietario");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el propietario?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
                controlador.eliminar(id);
                cargarDatos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Propietario eliminado");
            } catch (SQLException ex) {
                mostrarError(ex);
            }
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            txtIdentificacion.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtNombre.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtTelefono.setText(modeloTabla.getValueAt(fila, 3) != null ? modeloTabla.getValueAt(fila, 3).toString() : "");
            txtCorreo.setText(modeloTabla.getValueAt(fila, 4) != null ? modeloTabla.getValueAt(fila, 4).toString() : "");
        }
    }

    private void limpiarFormulario() {
        txtIdentificacion.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        tabla.clearSelection();
    }

    private void cargarDatos() {
        try {
            modeloTabla.setRowCount(0);
            for (Propietario propietario : controlador.listar()) {
                modeloTabla.addRow(new Object[]{
                        propietario.getId(),
                        propietario.getIdentificacion(),
                        propietario.getNombre(),
                        propietario.getTelefono(),
                        propietario.getCorreo()
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
