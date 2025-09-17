package com.agroinspeccion.vista;

import com.agroinspeccion.controlador.BaseControlador;
import com.agroinspeccion.modelo.InformeProduccion;
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
import java.time.LocalDate;

/**
 * Panel CRUD para informes de producción.
 */
public class PanelInformeProduccion extends JPanel {

    private final BaseControlador<InformeProduccion> controlador;
    private final BaseControlador<Lote> loteControlador;
    private final JComboBox<Lote> cboLote = new JComboBox<>();
    private final JTextField txtFecha = new JTextField();
    private final JTextField txtCantidad = new JTextField();
    private final JTextArea txtObservaciones = new JTextArea(3, 20);
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Lote", "Fecha", "Cantidad", "Observaciones"}, 0);
    private final JTable tabla = new JTable(modeloTabla);

    public PanelInformeProduccion(BaseControlador<InformeProduccion> controlador,
                                  BaseControlador<Lote> loteControlador) {
        this.controlador = controlador;
        this.loteControlador = loteControlador;
        inicializar();
        cargarLotes();
        cargarDatos();
    }

    private void inicializar() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new GridLayout(4, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del informe"));
        formulario.add(new JLabel("Lote:"));
        formulario.add(cboLote);
        formulario.add(new JLabel("Fecha (yyyy-MM-dd):"));
        formulario.add(txtFecha);
        formulario.add(new JLabel("Cantidad cosechada (t):"));
        formulario.add(txtCantidad);
        formulario.add(new JLabel("Observaciones:"));
        formulario.add(new JScrollPane(txtObservaciones));
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
            cargarLotes();
            cargarDatos();
        });
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
    }

    private void cargarLotes() {
        try {
            cboLote.removeAllItems();
            for (Lote lote : loteControlador.listar()) {
                cboLote.addItem(lote);
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void guardar() {
        if (cboLote.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un lote");
            return;
        }
        try {
            Lote lote = (Lote) cboLote.getSelectedItem();
            LocalDate fecha = txtFecha.getText().isBlank() ? LocalDate.now() : LocalDate.parse(txtFecha.getText());
            double cantidad = Double.parseDouble(txtCantidad.getText());
            InformeProduccion informe = new InformeProduccion(
                    lote != null ? lote.getId() : 0,
                    fecha,
                    cantidad,
                    txtObservaciones.getText());
            controlador.crear(informe);
            cargarDatos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Informe registrado");
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un informe");
            return;
        }
        if (cboLote.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un lote");
            return;
        }
        try {
            Lote lote = (Lote) cboLote.getSelectedItem();
            LocalDate fecha = txtFecha.getText().isBlank() ? LocalDate.now() : LocalDate.parse(txtFecha.getText());
            double cantidad = Double.parseDouble(txtCantidad.getText());
            InformeProduccion informe = new InformeProduccion(
                    Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString()),
                    lote != null ? lote.getId() : 0,
                    fecha,
                    cantidad,
                    txtObservaciones.getText());
            controlador.actualizar(informe);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Informe actualizado");
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un informe");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar informe?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
                controlador.eliminar(id);
                cargarDatos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Informe eliminado");
            } catch (SQLException ex) {
                mostrarError(ex);
            }
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            seleccionarLotePorDescripcion(modeloTabla.getValueAt(fila, 1).toString());
            txtFecha.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtCantidad.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtObservaciones.setText(modeloTabla.getValueAt(fila, 4) != null ? modeloTabla.getValueAt(fila, 4).toString() : "");
        }
    }

    private void seleccionarLotePorDescripcion(String descripcion) {
        for (int i = 0; i < cboLote.getItemCount(); i++) {
            Lote lote = cboLote.getItemAt(i);
            if (lote != null && lote.toString().equals(descripcion)) {
                cboLote.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiarFormulario() {
        cboLote.setSelectedIndex(-1);
        txtFecha.setText("");
        txtCantidad.setText("");
        txtObservaciones.setText("");
        tabla.clearSelection();
    }

    private void cargarDatos() {
        try {
            modeloTabla.setRowCount(0);
            for (InformeProduccion informe : controlador.listar()) {
                Lote lote = loteControlador.obtenerPorId(informe.getLoteId());
                modeloTabla.addRow(new Object[]{
                        informe.getId(),
                        lote != null ? lote.toString() : "",
                        informe.getFecha(),
                        informe.getCantidadCosechada(),
                        informe.getObservaciones()
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
