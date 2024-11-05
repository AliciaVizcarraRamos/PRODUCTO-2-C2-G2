package pe.edu.upeu.sysalmacenfx.control;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysalmacenfx.componente.ColumnInfo;
import pe.edu.upeu.sysalmacenfx.componente.ComboBoxAutoComplete;
import pe.edu.upeu.sysalmacenfx.componente.TableViewHelper;
import pe.edu.upeu.sysalmacenfx.componente.Toast;
import pe.edu.upeu.sysalmacenfx.dto.ComboBoxOption;
import pe.edu.upeu.sysalmacenfx.modelo.Cliente;
import pe.edu.upeu.sysalmacenfx.modelo.Usuario;
import pe.edu.upeu.sysalmacenfx.modelo.Venta;
import pe.edu.upeu.sysalmacenfx.servicio.ClienteService;
import pe.edu.upeu.sysalmacenfx.servicio.UsuarioService;
import pe.edu.upeu.sysalmacenfx.servicio.VentaService;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;
import static pe.edu.upeu.sysalmacenfx.componente.Toast.showToast;

@Component
public class VentaController {

    @FXML
    TextField txtPrecioBase, txtIGV, txtPrecioTotal, txtNumDoc, txtSerie, txtFiltroDato;
    @FXML
    ComboBox<ComboBoxOption> cbxCliente;
    @FXML
    ComboBox<ComboBoxOption> cbxUsuario;
    @FXML
    private TableView<Venta> tableView;
    @FXML
    Label lbnMsg;
    @FXML
    private AnchorPane miContenedor;
    Stage stage;

    @Autowired
    ClienteService clienteService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    VentaService ventaService;

    private Validator validator;
    ObservableList<Venta> listarVenta;
    Venta formulario;
    Long idVentaCE = 0L;

    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
            stage = (Stage) miContenedor.getScene().getWindow();
            if (stage != null) {
                System.out.println("El título del stage es: " + stage.getTitle());
            } else {
                System.out.println("Stage aún no disponible.");
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();

        cbxCliente.setTooltip(new Tooltip());
        cbxCliente.getItems().addAll(clienteService.listarCombobox());
        new ComboBoxAutoComplete<>(cbxCliente);

        cbxUsuario.setTooltip(new Tooltip());
        cbxUsuario.getItems().addAll((ComboBoxOption) usuarioService.List());
        new ComboBoxAutoComplete<>(cbxUsuario);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Configurar columnas de la tabla
        TableViewHelper<Venta> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Venta", new ColumnInfo("idVenta", 60.0));
        columns.put("Cliente", new ColumnInfo("cliente.dniruc", 100.0));
        columns.put("Usuario", new ColumnInfo("usuario.nombre", 100.0));
        columns.put("Precio Base", new ColumnInfo("precioBase", 80.0));
        columns.put("IGV", new ColumnInfo("igv", 60.0));
        columns.put("Precio Total", new ColumnInfo("precioTotal", 80.0));
        columns.put("Número Documento", new ColumnInfo("numDoc", 100.0));

        Consumer<Venta> updateAction = (Venta venta) -> {
            System.out.println("Actualizar: " + venta);
            editForm(venta);
        };
        Consumer<Venta> deleteAction = (Venta venta) -> {
            ventaService.delete(venta.getIdVenta());
            double with = stage.getWidth() / 1.5;
            double h = stage.getHeight() / 2;
            showToast(stage, "Venta eliminada correctamente!", 2000, with, h);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarVenta = FXCollections.observableArrayList(ventaService.List());
            tableView.getItems().addAll(listarVenta);

            // Agregar un listener para filtrar las ventas
            txtFiltroDato.textProperty().addListener((observable, oldValue, newValue) -> {
                filtrarVentas(newValue);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void limpiarError() {
        txtPrecioBase.getStyleClass().remove("text-field-error");
        txtIGV.getStyleClass().remove("text-field-error");
        txtPrecioTotal.getStyleClass().remove("text-field-error");
        cbxCliente.getStyleClass().remove("text-field-error");
        cbxUsuario.getStyleClass().remove("text-field-error");
    }

    public void clearForm() {
        txtPrecioBase.setText("");
        txtIGV.setText("");
        txtPrecioTotal.setText("");
        cbxCliente.getSelectionModel().select(null);
        cbxUsuario.getSelectionModel().select(null);
        idVentaCE = 0L;
        limpiarError();
    }

    @FXML
    public void cancelarAccion() {
        clearForm();
        limpiarError();
    }

    private void validarCampos(List<ConstraintViolation<Venta>> violacionesOrdenadasPorPropiedad) {
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();

        for (ConstraintViolation<Venta> violacion : violacionesOrdenadasPorPropiedad) {
            String campo = violacion.getPropertyPath().toString();
            if (campo.equals("precioBase")) {
                erroresOrdenados.put("precioBase", violacion.getMessage());
                txtPrecioBase.getStyleClass().add("text-field-error");
            } else if (campo.equals("igv")) {
                erroresOrdenados.put("igv", violacion.getMessage());
                txtIGV.getStyleClass().add("text-field-error");
            } else if (campo.equals("precioTotal")) {
                erroresOrdenados.put("precioTotal", violacion.getMessage());
                txtPrecioTotal.getStyleClass().add("text-field-error");
            } else if (campo.equals("cliente")) {
                erroresOrdenados.put("cliente", violacion.getMessage());
                cbxCliente.getStyleClass().add("text-field-error");
            } else if (campo.equals("usuario")) {
                erroresOrdenados.put("usuario", violacion.getMessage());
                cbxUsuario.getStyleClass().add("text-field-error");
            }
        }

        Map.Entry<String, String> primerError = erroresOrdenados.entrySet().iterator().next();
        lbnMsg.setText(primerError.getValue());
        lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
    }

    @FXML
    public void validarFormulario() {
        formulario = new Venta();
        formulario.setPrecioBase(Double.parseDouble(txtPrecioBase.getText().isEmpty() ? "0" : txtPrecioBase.getText()));
        formulario.setIgv(Double.parseDouble(txtIGV.getText().isEmpty() ? "0" : txtIGV.getText()));
        formulario.setPrecioTotal(Double.parseDouble(txtPrecioTotal.getText().isEmpty() ? "0" : txtPrecioTotal.getText()));
        formulario.setFechaGener(LocalDateTime.now());

        String idxCliente = cbxCliente.getSelectionModel().getSelectedItem() == null ? "0" : cbxCliente.getSelectionModel().getSelectedItem().getKey();
        formulario.setCliente(clienteService.buscarId(parseLong(idxCliente)));

        String idxUsuario = cbxUsuario.getSelectionModel().getSelectedItem() == null ? "0" : cbxUsuario.getSelectionModel().getSelectedItem().getKey();
        try {
            //formulario.setUsuario(usuarioService.loginUsuario(parseLong()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Set<ConstraintViolation<Venta>> violaciones = validator.validate(formulario);
        List<ConstraintViolation<Venta>> violacionesOrdenadasPorPropiedad = violaciones.stream()
                .sorted((v1, v2) -> v1.getPropertyPath().toString().compareTo(v2.getPropertyPath().toString()))
                .collect(Collectors.toList());

        if (violacionesOrdenadasPorPropiedad.isEmpty()) {
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarError();

            double with = stage.getWidth() / 1.5;
            double h = stage.getHeight() / 2;
            if (idVentaCE != 0L && idVentaCE > 0L) {
                formulario.setIdVenta(idVentaCE);
                ventaService.update(formulario);
                showToast(stage, "Venta actualizada correctamente!", 2000, with, h);
            } else {
                ventaService.save(formulario);
                showToast(stage, "Venta registrada correctamente!", 2000, with, h);
            }
            clearForm();
            listar();
        } else {
            validarCampos(violacionesOrdenadasPorPropiedad);
        }
    }

    public void filtrarVentas(String filtro) {
        ObservableList<Venta> ventasFiltradas = listarVenta.stream()
                .filter(venta -> venta.getNumDoc().toLowerCase().contains(filtro.toLowerCase()) ||
                        venta.getSerie().toLowerCase().contains(filtro.toLowerCase()) ||
                        venta.getCliente().getDniruc().toLowerCase().contains(filtro.toLowerCase()) ||
                        venta.getUsuario().getClave().toLowerCase().contains(filtro.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        tableView.setItems(ventasFiltradas);
    }

    public void editForm(Venta venta) {
        txtPrecioBase.setText(String.valueOf(venta.getPrecioBase()));
        txtIGV.setText(String.valueOf(venta.getIgv()));
        txtPrecioTotal.setText(String.valueOf(venta.getPrecioTotal()));
        cbxCliente.getSelectionModel().select(new ComboBoxOption(String.valueOf(venta.getCliente().getNombres()), venta.getCliente().getDniruc()));
        cbxUsuario.getSelectionModel().select(new ComboBoxOption(String.valueOf(venta.getUsuario().getIdUsuario()), venta.getUsuario().getClave()));
        idVentaCE = venta.getIdVenta();
    }
}
