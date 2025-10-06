package com.agroinspeccion.vista;

import com.agroinspeccion.controlador.BaseControlador;
import com.agroinspeccion.modelo.LugarProduccion;
import com.agroinspeccion.modelo.Predio;
import com.agroinspeccion.modelo.Propietario;

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
 * Panel CRUD para predios agrícolas.
 */
public class PanelPredio extends JPanel {

    private final BaseControlador<Predio> controlador;
    private final BaseControlador<Propietario> propietarioControlador;
    private final BaseControlador<LugarProduccion> lugarControlador;
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtDepartamento = new JTextField();
    private final JTextField txtMunicipio = new JTextField();
    private final JTextField txtVereda = new JTextField();
    private final JTextField txtArea = new JTextField();
    private final JComboBox<Propietario> cboPropietario = new JComboBox<>();
    private final JComboBox<LugarProduccion> cboLugar = new JComboBox<>();
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Municipio", "Departamento", "Vereda", "Área", "Propietario", "Lugar"}, 0);
    private final JTable tabla = new JTable(modeloTabla);

    public PanelPredio(BaseControlador<Predio> controlador,
                       BaseControlador<Propietario> propietarioControlador,
                       BaseControlador<LugarProduccion> lugarControlador) {
        this.controlador = controlador;
        this.propietarioControlador = propietarioControlador;
        this.lugarControlador = lugarControlador;
        inicializar();
        cargarPropietarios();
        cargarLugares();
        cargarDatos();
    }

    private void inicializar() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new GridLayout(7, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del predio"));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Departamento:"));
        formulario.add(txtDepartamento);
        formulario.add(new JLabel("Municipio:"));
        formulario.add(txtMunicipio);
        formulario.add(new JLabel("Vereda:"));
        formulario.add(txtVereda);
        formulario.add(new JLabel("Área (ha):"));
        formulario.add(txtArea);
        formulario.add(new JLabel("Propietario:"));
        formulario.add(cboPropietario);
        formulario.add(new JLabel("Lugar de producción:"));
        formulario.add(cboLugar);
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
            cargarPropietarios();
            cargarLugares();
            cargarDatos();
        });
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
    }

    private void cargarPropietarios() {
        try {
            cboPropietario.removeAllItems();
            for (Propietario propietario : propietarioControlador.listar()) {
                cboPropietario.addItem(propietario);
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void cargarLugares() {
        try {
            cboLugar.removeAllItems();
            for (LugarProduccion lugar : lugarControlador.listar()) {
                cboLugar.addItem(lugar);
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void guardar() {
        if (cboPropietario.getSelectedItem() == null || cboLugar.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione propietario y lugar");
            return;
        }
        try {
            Propietario propietario = (Propietario) cboPropietario.getSelectedItem();
            LugarProduccion lugar = (LugarProduccion) cboLugar.getSelectedItem();
            double area = Double.parseDouble(txtArea.getText());
            Predio predio = new Predio(
                    txtNombre.getText(),
                    txtDepartamento.getText(),
                    txtMunicipio.getText(),
                    txtVereda.getText(),
                    area,
                    propietario != null ? propietario.getId() : 0,
                    lugar != null ? lugar.getId() : 0);
            controlador.crear(predio);
            cargarDatos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Predio registrado");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Área inválida", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un predio");
            return;
        }
        if (cboPropietario.getSelectedItem() == null || cboLugar.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione propietario y lugar");
            return;
        }
        try {
            Propietario propietario = (Propietario) cboPropietario.getSelectedItem();
            LugarProduccion lugar = (LugarProduccion) cboLugar.getSelectedItem();
            double area = Double.parseDouble(txtArea.getText());
            Predio predio = new Predio(
                    Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString()),
                    txtNombre.getText(),
                    txtDepartamento.getText(),
                    txtMunicipio.getText(),
                    txtVereda.getText(),
                    area,
                    propietario != null ? propietario.getId() : 0,
                    lugar != null ? lugar.getId() : 0);
            controlador.actualizar(predio);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Predio actualizado");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Área inválida", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un predio");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar predio?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
                controlador.eliminar(id);
                cargarDatos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Predio eliminado");
            } catch (SQLException ex) {
                mostrarError(ex);
            }
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtMunicipio.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtDepartamento.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtVereda.setText(modeloTabla.getValueAt(fila, 4) != null ? modeloTabla.getValueAt(fila, 4).toString() : "");
            txtArea.setText(modeloTabla.getValueAt(fila, 5).toString());
            seleccionarPropietarioPorNombre(modeloTabla.getValueAt(fila, 6).toString());
            seleccionarLugarPorNombre(modeloTabla.getValueAt(fila, 7).toString());
        }
    }

    private void seleccionarPropietarioPorNombre(String nombre) {
        for (int i = 0; i < cboPropietario.getItemCount(); i++) {
            Propietario p = cboPropietario.getItemAt(i);
            if (p != null && p.getNombre().equals(nombre)) {
                cboPropietario.setSelectedIndex(i);
                break;
            }
        }
    }

    private void seleccionarLugarPorNombre(String nombre) {
        for (int i = 0; i < cboLugar.getItemCount(); i++) {
            LugarProduccion l = cboLugar.getItemAt(i);
            if (l != null && l.getNombre().equals(nombre)) {
                cboLugar.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDepartamento.setText("");
        txtMunicipio.setText("");
        txtVereda.setText("");
        txtArea.setText("");
        tabla.clearSelection();
    }

    private void cargarDatos() {
        try {
            modeloTabla.setRowCount(0);
            for (Predio predio : controlador.listar()) {
                Propietario propietario = propietarioControlador.obtenerPorId(predio.getPropietarioId());
                LugarProduccion lugar = lugarControlador.obtenerPorId(predio.getLugarProduccionId());
                modeloTabla.addRow(new Object[]{
                        predio.getId(),
                        predio.getNombre(),
                        predio.getMunicipio(),
                        predio.getDepartamento(),
                        predio.getVereda(),
                        predio.getAreaHectareas(),
                        propietario != null ? propietario.getNombre() : "",
                        lugar != null ? lugar.getNombre() : ""
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
