package com.agroinspeccion.vista;

import com.agroinspeccion.controlador.BaseControlador;
import com.agroinspeccion.modelo.AsistenteTecnico;
import com.agroinspeccion.modelo.InformeFitosanitario;
import com.agroinspeccion.modelo.Lote;
import com.agroinspeccion.modelo.Plaga;

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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Panel CRUD para informes fitosanitarios.
 */
public class PanelInformeFitosanitario extends JPanel {

    private final BaseControlador<InformeFitosanitario> controlador;
    private final BaseControlador<Lote> loteControlador;
    private final BaseControlador<Plaga> plagaControlador;
    private final BaseControlador<AsistenteTecnico> asistenteControlador;
    private final JComboBox<Lote> cboLote = new JComboBox<>();
    private final JComboBox<Plaga> cboPlaga = new JComboBox<>();
    private final JComboBox<AsistenteTecnico> cboAsistente = new JComboBox<>();
    private final JTextField txtFecha = new JTextField();
    private final JTextField txtPlantasTotales = new JTextField();
    private final JTextField txtPlantasAfectadas = new JTextField();
    private final JLabel lblPorcentaje = new JLabel("0.00%");
    private final JTextArea txtObservaciones = new JTextArea(3, 20);
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Lote", "Plaga", "Fecha", "Totales", "Afectadas", "%", "Asistente", "Observaciones"}, 0);
    private final JTable tabla = new JTable(modeloTabla);

    public PanelInformeFitosanitario(BaseControlador<InformeFitosanitario> controlador,
                                     BaseControlador<Lote> loteControlador,
                                     BaseControlador<Plaga> plagaControlador,
                                     BaseControlador<AsistenteTecnico> asistenteControlador) {
        this.controlador = controlador;
        this.loteControlador = loteControlador;
        this.plagaControlador = plagaControlador;
        this.asistenteControlador = asistenteControlador;
        inicializar();
        cargarCatalogos();
        cargarDatos();
    }

    private void inicializar() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new GridLayout(7, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del informe"));
        formulario.add(new JLabel("Lote:"));
        formulario.add(cboLote);
        formulario.add(new JLabel("Plaga:"));
        formulario.add(cboPlaga);
        formulario.add(new JLabel("Asistente:"));
        formulario.add(cboAsistente);
        formulario.add(new JLabel("Fecha (yyyy-MM-dd):"));
        formulario.add(txtFecha);
        formulario.add(new JLabel("Plantas totales:"));
        formulario.add(txtPlantasTotales);
        formulario.add(new JLabel("Plantas afectadas:"));
        formulario.add(txtPlantasAfectadas);
        formulario.add(new JLabel("Incidencia calculada:"));
        formulario.add(lblPorcentaje);
        add(formulario, BorderLayout.NORTH);

        JPanel observacionesPanel = new JPanel(new BorderLayout());
        observacionesPanel.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        observacionesPanel.add(new JScrollPane(txtObservaciones), BorderLayout.CENTER);
        add(observacionesPanel, BorderLayout.CENTER);

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
        add(acciones, BorderLayout.SOUTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setPreferredScrollableViewportSize(new Dimension(450, 400));
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        add(new JScrollPane(tabla), BorderLayout.EAST);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnRefrescar.addActionListener(e -> {
            cargarCatalogos();
            cargarDatos();
        });
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
        txtPlantasTotales.getDocument().addDocumentListener(SimpleDocumentListener.of(e -> recalcularPorcentaje()));
        txtPlantasAfectadas.getDocument().addDocumentListener(SimpleDocumentListener.of(e -> recalcularPorcentaje()));
    }

    private void cargarCatalogos() {
        try {
            cboLote.removeAllItems();
            for (Lote lote : loteControlador.listar()) {
                cboLote.addItem(lote);
            }
            cboPlaga.removeAllItems();
            for (Plaga plaga : plagaControlador.listar()) {
                cboPlaga.addItem(plaga);
            }
            cboAsistente.removeAllItems();
            for (AsistenteTecnico asistente : asistenteControlador.listar()) {
                cboAsistente.addItem(asistente);
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void guardar() {
        if (!validarSeleccion()) {
            return;
        }
        if (!validarCamposNumericos()) {
            return;
        }
        try {
            InformeFitosanitario informe = construirInformeDesdeFormulario(0);
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
        if (!validarSeleccion()) {
            return;
        }
        if (!validarCamposNumericos()) {
            return;
        }
        try {
            int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            InformeFitosanitario informe = construirInformeDesdeFormulario(id);
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

    private boolean validarSeleccion() {
        if (cboLote.getSelectedItem() == null || cboPlaga.getSelectedItem() == null || cboAsistente.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione lote, plaga y asistente");
            return false;
        }
        return true;
    }

    private boolean validarCamposNumericos() {
        if (txtPlantasTotales.getText().isBlank() || txtPlantasAfectadas.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Ingrese el número de plantas totales y afectadas");
            return false;
        }
        return true;
    }

    private InformeFitosanitario construirInformeDesdeFormulario(int id) {
        Lote lote = (Lote) cboLote.getSelectedItem();
        Plaga plaga = (Plaga) cboPlaga.getSelectedItem();
        AsistenteTecnico asistente = (AsistenteTecnico) cboAsistente.getSelectedItem();
        LocalDate fecha = txtFecha.getText().isBlank() ? LocalDate.now() : LocalDate.parse(txtFecha.getText());
        int totales = Integer.parseInt(txtPlantasTotales.getText());
        int afectadas = Integer.parseInt(txtPlantasAfectadas.getText());
        double porcentaje = calcularPorcentaje(totales, afectadas);
        lblPorcentaje.setText(String.format("%.2f%%", porcentaje));
        return id == 0
                ? new InformeFitosanitario(
                lote != null ? lote.getId() : 0,
                plaga != null ? plaga.getId() : 0,
                asistente != null ? asistente.getId() : 0,
                fecha,
                totales,
                afectadas,
                porcentaje,
                txtObservaciones.getText())
                : new InformeFitosanitario(
                id,
                lote != null ? lote.getId() : 0,
                plaga != null ? plaga.getId() : 0,
                asistente != null ? asistente.getId() : 0,
                fecha,
                totales,
                afectadas,
                porcentaje,
                txtObservaciones.getText());
    }

    private void cargarDatos() {
        try {
            modeloTabla.setRowCount(0);
            for (InformeFitosanitario informe : controlador.listar()) {
                Lote lote = loteControlador.obtenerPorId(informe.getLoteId());
                Plaga plaga = plagaControlador.obtenerPorId(informe.getPlagaId());
                AsistenteTecnico asistente = asistenteControlador.obtenerPorId(informe.getAsistenteId());
                modeloTabla.addRow(new Object[]{
                        informe.getId(),
                        lote != null ? lote.toString() : "",
                        plaga != null ? plaga.getNombreComun() : "",
                        informe.getFecha(),
                        informe.getPlantasTotales(),
                        informe.getPlantasAfectadas(),
                        informe.getPorcentajeIncidencia(),
                        asistente != null ? asistente.getNombre() : "",
                        informe.getObservaciones()
                });
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            seleccionarLotePorDescripcion(modeloTabla.getValueAt(fila, 1).toString());
            seleccionarPlagaPorNombre(modeloTabla.getValueAt(fila, 2).toString());
            seleccionarAsistentePorNombre(modeloTabla.getValueAt(fila, 7).toString());
            txtFecha.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtPlantasTotales.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtPlantasAfectadas.setText(modeloTabla.getValueAt(fila, 5).toString());
            lblPorcentaje.setText(String.format("%s%%", modeloTabla.getValueAt(fila, 6)));
            txtObservaciones.setText(modeloTabla.getValueAt(fila, 8) != null ? modeloTabla.getValueAt(fila, 8).toString() : "");
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

    private void seleccionarPlagaPorNombre(String nombre) {
        for (int i = 0; i < cboPlaga.getItemCount(); i++) {
            Plaga plaga = cboPlaga.getItemAt(i);
            if (plaga != null && plaga.getNombreComun().equals(nombre)) {
                cboPlaga.setSelectedIndex(i);
                break;
            }
        }
    }

    private void seleccionarAsistentePorNombre(String nombre) {
        for (int i = 0; i < cboAsistente.getItemCount(); i++) {
            AsistenteTecnico asistente = cboAsistente.getItemAt(i);
            if (asistente != null && asistente.getNombre().equals(nombre)) {
                cboAsistente.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiarFormulario() {
        cboLote.setSelectedIndex(-1);
        cboPlaga.setSelectedIndex(-1);
        cboAsistente.setSelectedIndex(-1);
        txtFecha.setText("");
        txtPlantasTotales.setText("");
        txtPlantasAfectadas.setText("");
        txtObservaciones.setText("");
        lblPorcentaje.setText("0.00%");
        tabla.clearSelection();
    }

    private void recalcularPorcentaje() {
        try {
            int totales = txtPlantasTotales.getText().isBlank() ? 0 : Integer.parseInt(txtPlantasTotales.getText());
            int afectadas = txtPlantasAfectadas.getText().isBlank() ? 0 : Integer.parseInt(txtPlantasAfectadas.getText());
            double porcentaje = calcularPorcentaje(totales, afectadas);
            lblPorcentaje.setText(String.format("%.2f%%", porcentaje));
        } catch (NumberFormatException ex) {
            lblPorcentaje.setText("0.00%");
        }
    }

    private double calcularPorcentaje(int totales, int afectadas) {
        if (totales <= 0) {
            return 0;
        }
        if (afectadas < 0) {
            afectadas = 0;
        }
        return (afectadas * 100.0) / totales;
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
