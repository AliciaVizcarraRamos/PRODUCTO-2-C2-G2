package pe.edu.upeu.sysalmacenfx.control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysalmacenfx.modelo.Proveedor;
import pe.edu.upeu.sysalmacenfx.servicio.ProveedorService;

import java.util.List;

@Component
public class RegistrarProveedorControl {

    @Autowired
    private ProveedorService proveedorService;

    @FXML
    private AnchorPane miContenedor;

    @FXML
    private TextField txtRuc, txtNombres, txtTelefono, txtDireccion, txtRazonSocial, txtFiltroDato;

    @FXML
    private TableView<Proveedor> tableView;

    @FXML
    private TableColumn<Proveedor, String> colRuc, colNombres, colTelefono, colDireccion, colRazonSocial;

    @FXML
    private Label lbnMsg;

    @FXML
    public void initialize() {
        configurarTabla();
        cargarDatosTabla();
    }

    private void configurarTabla() {
        colRuc.setCellValueFactory(new PropertyValueFactory<>("dniRuc"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombresRaso"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("celular"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colRazonSocial.setCellValueFactory(new PropertyValueFactory<>("tipoDoc"));
    }
    public void save(Proveedor proveedor) {
        // Aquí se agregaría a la lista o se guardaría en la base de datos
        proveedorService.save(proveedor); // proveedoresList es una lista simulada
    }
    private void cargarDatosTabla() {
        List<Proveedor> proveedores = proveedorService.List();
        if (proveedores != null && !proveedores.isEmpty()) {
            tableView.getItems().setAll(proveedores);
        } else {
            tableView.getItems().clear(); // Limpiar la tabla si no hay datos
        }

    }
    @FXML
    private void validarFormulario() {
        if (camposValidos()) {
            Proveedor nuevoProveedor = Proveedor.builder()
                    .dniRuc(txtRuc.getText())
                    .nombresRaso(txtNombres.getText())
                    .celular(txtTelefono.getText())
                    .direccion(txtDireccion.getText())
                    .tipoDoc(txtRazonSocial.getText())
                    .build();

            proveedorService.save(nuevoProveedor);
            System.out.println("Proveedor guardado: " + nuevoProveedor); // Mensaje de confirmación
            lbnMsg.setText("Proveedor guardado exitosamente.");
            limpiarFormulario();
            cargarDatosTabla();
        } else {
            lbnMsg.setText("Por favor, complete todos los campos.");
        }
    }

    @FXML
    private void cancelarAccion() {
        limpiarFormulario();
        lbnMsg.setText("");
    }

    private boolean camposValidos() {
        return !txtRuc.getText().isEmpty() &&
                !txtNombres.getText().isEmpty() &&
                !txtTelefono.getText().isEmpty() &&
                !txtDireccion.getText().isEmpty() &&
                !txtRazonSocial.getText().isEmpty();
    }

    private void limpiarFormulario() {
        txtRuc.clear();
        txtNombres.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtRazonSocial.clear();
    }

    @FXML
    private void eliminarProveedor() {
        Proveedor proveedorSeleccionado = tableView.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado != null) {
            proveedorService.delete(proveedorSeleccionado.getIdProveedor());
            lbnMsg.setText("Proveedor eliminado correctamente.");
            cargarDatosTabla();
        } else {
            lbnMsg.setText("Seleccione un proveedor para eliminar.");
        }
    }

    @FXML
    private void actualizarProveedor() {
        Proveedor proveedorSeleccionado = tableView.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado != null && camposValidos()) {
            proveedorSeleccionado.setDniRuc(txtRuc.getText());
            proveedorSeleccionado.setNombresRaso(txtNombres.getText());
            proveedorSeleccionado.setCelular(txtTelefono.getText());
            proveedorSeleccionado.setDireccion(txtDireccion.getText());
            proveedorSeleccionado.setTipoDoc(txtRazonSocial.getText());

            proveedorService.update(proveedorSeleccionado, proveedorSeleccionado.getIdProveedor());
            lbnMsg.setText("Proveedor actualizado correctamente.");
            cargarDatosTabla();
            limpiarFormulario();
        } else {
            lbnMsg.setText("Seleccione un proveedor y complete todos los campos.");
        }
    }

    @FXML
    private void filtrarProveedores() {
        String filtro = txtFiltroDato.getText().toLowerCase();
        List<Proveedor> proveedoresFiltrados = proveedorService.List().stream()
                .filter(p -> p.getDniRuc().toLowerCase().contains(filtro) ||
                        p.getNombresRaso().toLowerCase().contains(filtro) ||
                        p.getCelular().toLowerCase().contains(filtro) ||
                        p.getDireccion().toLowerCase().contains(filtro) ||
                        p.getTipoDoc().toLowerCase().contains(filtro))
                .toList();
        tableView.getItems().setAll(proveedoresFiltrados);
    }
}
