package com.agroinspeccion.vista;

import com.agroinspeccion.controlador.BaseControlador;
import com.agroinspeccion.modelo.LugarProduccion;
import com.agroinspeccion.modelo.Productor;

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

/**
 * Panel CRUD para lugares de producción.
 */
public class PanelLugarProduccion extends JPanel {

    private final BaseControlador<LugarProduccion> controlador;
    private final BaseControlador<Productor> productorControlador;
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtRegistro = new JTextField();
    private final JComboBox<Productor> cboProductor = new JComboBox<>();
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Registro ICA", "Productor"}, 0);
    private final JTable tabla = new JTable(modeloTabla);

    public PanelLugarProduccion(BaseControlador<LugarProduccion> controlador,
                                BaseControlador<Productor> productorControlador) {
        this.controlador = controlador;
        this.productorControlador = productorControlador;
        inicializar();
        cargarProductores();
        cargarDatos();
    }

    private void inicializar() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new GridLayout(3, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del lugar de producción"));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Registro ICA:"));
        formulario.add(txtRegistro);
        formulario.add(new JLabel("Productor asociado:"));
        formulario.add(cboProductor);
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
            cargarProductores();
            cargarDatos();
        });
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
    }

    private void cargarProductores() {
        try {
            cboProductor.removeAllItems();
            for (Productor productor : productorControlador.listar()) {
                cboProductor.addItem(productor);
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void guardar() {
        if (cboProductor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un productor");
            return;
        }
        try {
            Productor productor = (Productor) cboProductor.getSelectedItem();
            LugarProduccion lugar = new LugarProduccion(
                    txtNombre.getText(),
                    txtRegistro.getText(),
                    productor != null ? productor.getId() : 0);
            controlador.crear(lugar);
            cargarDatos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Lugar registrado correctamente");
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un lugar");
            return;
        }
        if (cboProductor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un productor");
            return;
        }
        try {
            Productor productor = (Productor) cboProductor.getSelectedItem();
            LugarProduccion lugar = new LugarProduccion(
                    Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString()),
                    txtNombre.getText(),
                    txtRegistro.getText(),
                    productor != null ? productor.getId() : 0);
            controlador.actualizar(lugar);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Lugar actualizado");
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un lugar");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar lugar de producción?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
                controlador.eliminar(id);
                cargarDatos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Lugar eliminado");
            } catch (SQLException ex) {
                mostrarError(ex);
            }
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtRegistro.setText(modeloTabla.getValueAt(fila, 2).toString());
            seleccionarProductorPorNombre(modeloTabla.getValueAt(fila, 3).toString());
        }
    }

    private void seleccionarProductorPorNombre(String nombre) {
        for (int i = 0; i < cboProductor.getItemCount(); i++) {
            Productor p = cboProductor.getItemAt(i);
            if (p != null && p.getNombre().equals(nombre)) {
                cboProductor.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtRegistro.setText("");
        tabla.clearSelection();
    }

    private void cargarDatos() {
        try {
            modeloTabla.setRowCount(0);
            for (LugarProduccion lugar : controlador.listar()) {
                Productor productor = productorControlador.obtenerPorId(lugar.getProductorId());
                modeloTabla.addRow(new Object[]{
                        lugar.getId(),
                        lugar.getNombre(),
                        lugar.getNumeroRegistroIca(),
                        productor != null ? productor.getNombre() : ""
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
