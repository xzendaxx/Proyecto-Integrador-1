package com.agroinspeccion.vista;

import com.agroinspeccion.controlador.BaseControlador;
import com.agroinspeccion.modelo.Cultivo;
import com.agroinspeccion.modelo.Predio;
import com.agroinspeccion.util.FechaUtil;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import java.time.LocalDate;

/**
 * Panel CRUD para cultivos.
 */
public class PanelCultivo extends JPanel {

    private final BaseControlador<Cultivo> controlador;
    private final BaseControlador<Predio> predioControlador;
    private final JTextField txtEspecie = new JTextField();
    private final JTextField txtVariedad = new JTextField();
    private final JTextField txtFecha = new JTextField();
    private final JComboBox<Predio> cboPredio = new JComboBox<>();
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Especie", "Variedad", "Fecha", "Predio"}, 0);
    private final JTable tabla = new JTable(modeloTabla);

    public PanelCultivo(BaseControlador<Cultivo> controlador, BaseControlador<Predio> predioControlador) {
        this.controlador = controlador;
        this.predioControlador = predioControlador;
        inicializar();
        cargarPredios();
        cargarDatos();
    }

    private void inicializar() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new GridLayout(4, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del cultivo"));
        formulario.add(new JLabel("Especie:"));
        formulario.add(txtEspecie);
        formulario.add(new JLabel("Variedad:"));
        formulario.add(txtVariedad);
        formulario.add(new JLabel("Fecha siembra (yyyy-MM-dd):"));
        formulario.add(txtFecha);
        formulario.add(new JLabel("Predio:"));
        formulario.add(cboPredio);
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
            cargarPredios();
            cargarDatos();
        });
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
    }

    private void cargarPredios() {
        try {
            cboPredio.removeAllItems();
            for (Predio predio : predioControlador.listar()) {
                cboPredio.addItem(predio);
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void guardar() {
        if (cboPredio.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un predio");
            return;
        }
        try {
            Predio predio = (Predio) cboPredio.getSelectedItem();
            LocalDate fecha = txtFecha.getText().isBlank() ? null : LocalDate.parse(txtFecha.getText());
            Cultivo cultivo = new Cultivo(
                    txtEspecie.getText(),
                    txtVariedad.getText(),
                    fecha,
                    predio != null ? predio.getId() : 0);
            controlador.crear(cultivo);
            cargarDatos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Cultivo registrado");
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cultivo");
            return;
        }
        if (cboPredio.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un predio");
            return;
        }
        try {
            Predio predio = (Predio) cboPredio.getSelectedItem();
            LocalDate fecha = txtFecha.getText().isBlank() ? null : LocalDate.parse(txtFecha.getText());
            Cultivo cultivo = new Cultivo(
                    Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString()),
                    txtEspecie.getText(),
                    txtVariedad.getText(),
                    fecha,
                    predio != null ? predio.getId() : 0);
            controlador.actualizar(cultivo);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Cultivo actualizado");
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cultivo");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar cultivo?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
                controlador.eliminar(id);
                cargarDatos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Cultivo eliminado");
            } catch (SQLException ex) {
                mostrarError(ex);
            }
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            txtEspecie.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtVariedad.setText(modeloTabla.getValueAt(fila, 2) != null ? modeloTabla.getValueAt(fila, 2).toString() : "");
            txtFecha.setText(modeloTabla.getValueAt(fila, 3) != null ? modeloTabla.getValueAt(fila, 3).toString() : "");
            seleccionarPredioPorNombre(modeloTabla.getValueAt(fila, 4).toString());
        }
    }

    private void seleccionarPredioPorNombre(String nombre) {
        for (int i = 0; i < cboPredio.getItemCount(); i++) {
            Predio p = cboPredio.getItemAt(i);
            if (p != null && p.getNombre().equals(nombre)) {
                cboPredio.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiarFormulario() {
        txtEspecie.setText("");
        txtVariedad.setText("");
        txtFecha.setText("");
        tabla.clearSelection();
    }

    private void cargarDatos() {
        try {
            modeloTabla.setRowCount(0);
            for (Cultivo cultivo : controlador.listar()) {
                Predio predio = predioControlador.obtenerPorId(cultivo.getPredioId());
                modeloTabla.addRow(new Object[]{
                        cultivo.getId(),
                        cultivo.getEspecie(),
                        cultivo.getVariedad(),
                        cultivo.getFechaSiembra() != null ? FechaUtil.toSqlDate(cultivo.getFechaSiembra()).toString() : "",
                        predio != null ? predio.getNombre() : ""
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
