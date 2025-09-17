package com.agroinspeccion.vista;

import com.agroinspeccion.controlador.BaseControlador;
import com.agroinspeccion.modelo.Cultivo;
import com.agroinspeccion.modelo.Lote;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

/**
 * Panel CRUD para lotes.
 */
public class PanelLote extends JPanel {

    private final BaseControlador<Lote> controlador;
    private final BaseControlador<Cultivo> cultivoControlador;
    private final JTextField txtNumero = new JTextField();
    private final JTextArea txtDescripcion = new JTextArea(3, 20);
    private final JTextField txtArea = new JTextField();
    private final JComboBox<Cultivo> cboCultivo = new JComboBox<>();
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Número", "Área", "Cultivo"}, 0);
    private final JTable tabla = new JTable(modeloTabla);

    public PanelLote(BaseControlador<Lote> controlador, BaseControlador<Cultivo> cultivoControlador) {
        this.controlador = controlador;
        this.cultivoControlador = cultivoControlador;
        inicializar();
        cargarCultivos();
        cargarDatos();
    }

    private void inicializar() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new GridLayout(4, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del lote"));
        formulario.add(new JLabel("Número:"));
        formulario.add(txtNumero);
        formulario.add(new JLabel("Descripción:"));
        formulario.add(new JScrollPane(txtDescripcion));
        formulario.add(new JLabel("Área (ha):"));
        formulario.add(txtArea);
        formulario.add(new JLabel("Cultivo:"));
        formulario.add(cboCultivo);
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
        btnRefrescar.addActionListener(e -> {
            cargarCultivos();
            cargarDatos();
        });
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
    }

    private void cargarCultivos() {
        try {
            cboCultivo.removeAllItems();
            for (Cultivo cultivo : cultivoControlador.listar()) {
                cboCultivo.addItem(cultivo);
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void guardar() {
        if (cboCultivo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cultivo");
            return;
        }
        try {
            Cultivo cultivo = (Cultivo) cboCultivo.getSelectedItem();
            double area = txtArea.getText().isBlank() ? 0 : Double.parseDouble(txtArea.getText());
            Lote lote = new Lote(
                    txtNumero.getText(),
                    txtDescripcion.getText(),
                    area,
                    cultivo != null ? cultivo.getId() : 0);
            controlador.crear(lote);
            cargarDatos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Lote registrado");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Área inválida", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un lote");
            return;
        }
        if (cboCultivo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cultivo");
            return;
        }
        try {
            Cultivo cultivo = (Cultivo) cboCultivo.getSelectedItem();
            double area = txtArea.getText().isBlank() ? 0 : Double.parseDouble(txtArea.getText());
            Lote lote = new Lote(
                    Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString()),
                    txtNumero.getText(),
                    txtDescripcion.getText(),
                    area,
                    cultivo != null ? cultivo.getId() : 0);
            controlador.actualizar(lote);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Lote actualizado");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Área inválida", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un lote");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar lote?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
                controlador.eliminar(id);
                cargarDatos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Lote eliminado");
            } catch (SQLException ex) {
                mostrarError(ex);
            }
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            txtNumero.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtArea.setText(modeloTabla.getValueAt(fila, 2) != null ? modeloTabla.getValueAt(fila, 2).toString() : "");
            seleccionarCultivoPorDescripcion(modeloTabla.getValueAt(fila, 3).toString());
        }
    }

    private void seleccionarCultivoPorDescripcion(String descripcion) {
        for (int i = 0; i < cboCultivo.getItemCount(); i++) {
            Cultivo cultivo = cboCultivo.getItemAt(i);
            if (cultivo != null && cultivo.toString().equals(descripcion)) {
                cboCultivo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiarFormulario() {
        txtNumero.setText("");
        txtDescripcion.setText("");
        txtArea.setText("");
        tabla.clearSelection();
    }

    private void cargarDatos() {
        try {
            modeloTabla.setRowCount(0);
            for (Lote lote : controlador.listar()) {
                Cultivo cultivo = cultivoControlador.obtenerPorId(lote.getCultivoId());
                modeloTabla.addRow(new Object[]{
                        lote.getId(),
                        lote.getNumero(),
                        lote.getArea(),
                        cultivo != null ? cultivo.toString() : ""
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
