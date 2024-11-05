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
import pe.edu.upeu.sysalmacenfx.componente.TableViewHelper;
import pe.edu.upeu.sysalmacenfx.dto.ComboBoxOption;
import pe.edu.upeu.sysalmacenfx.modelo.Cliente;
import pe.edu.upeu.sysalmacenfx.servicio.ClienteService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static pe.edu.upeu.sysalmacenfx.componente.Toast.showToast;
@Component
public class ClienteController {

    @FXML
    TextField txtnombres, txtdniruc, txtrepLegal, txtFiltroDato;
    @FXML
    ComboBox<ComboBoxOption> cbxtipoDocumento;
    @FXML
    private TableView<Cliente> tableView;
    @FXML
    Label lbnMsg;
    @FXML
    private AnchorPane miContenedor;
    Stage stage;

    @Autowired
    ClienteService ms;
    private Validator validator;
    ObservableList<Cliente> listarCliente;
    Cliente formulario;
    Long idProductoCE = 0L;

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

        cbxtipoDocumento.setTooltip(new Tooltip());
        cbxtipoDocumento.getItems().addAll(ms.listarCombobox());
        cbxtipoDocumento.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxtipoDocumento.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey();
                System.out.println("ID del documento seleccionado: " + selectedId);
            }
        });

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<Cliente> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Cliente", new ColumnInfo("dniruc", 60.0));
        columns.put("Nombre", new ColumnInfo("nombres", 100.0));
        columns.put("Representante Legal", new ColumnInfo("repLegal", 100.0));
        columns.put("Tipo Documento", new ColumnInfo("tipoDocumento", 100.0));

        Consumer<Cliente> updateAction = (Cliente cliente) -> {
            System.out.println("Actualizar: " + cliente);
            editForm(cliente);
        };
        Consumer<Cliente> deleteAction = (Cliente cliente) -> {
            System.out.println("Eliminar: " + cliente);
            ms.delete(Long.valueOf(cliente.getDniruc()));
            double width = stage.getWidth() / 1.5;
            double height = stage.getHeight() / 2;
            showToast(stage, "Se eliminó correctamente!!", 2000, width, height);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarCliente = FXCollections.observableArrayList(ms.List());
            tableView.getItems().addAll(listarCliente);

            txtFiltroDato.textProperty().addListener((observable, oldValue, newValue) -> {
                filtrarClientes(newValue);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void limpiarError() {
        txtnombres.getStyleClass().remove("text-field-error");
        txtdniruc.getStyleClass().remove("text-field-error");
        txtrepLegal.getStyleClass().remove("text-field-error");
        cbxtipoDocumento.getStyleClass().remove("text-field-error");
    }

    public void clearForm() {
        txtnombres.setText("");
        txtdniruc.setText("");
        txtrepLegal.setText("");
        cbxtipoDocumento.getSelectionModel().select(null);
        idProductoCE = 0L;
        limpiarError();
    }

    @FXML
    public void cancelarAccion() {
        clearForm();
        limpiarError();
    }

    void validarCampos(List<ConstraintViolation<Cliente>> violacionesOrdenadasPorPropiedad) {
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        for (ConstraintViolation<Cliente> violacion : violacionesOrdenadasPorPropiedad) {
            String campo = violacion.getPropertyPath().toString();
            if (campo.equals("nombres")) {
                erroresOrdenados.put("nombres", violacion.getMessage());
                txtnombres.getStyleClass().add("text-field-error");
            } else if (campo.equals("dniruc")) {
                erroresOrdenados.put("dniruc", violacion.getMessage());
                txtdniruc.getStyleClass().add("text-field-error");
            } else if (campo.equals("repLegal")) {
                erroresOrdenados.put("repLegal", violacion.getMessage());
                txtrepLegal.getStyleClass().add("text-field-error");
            } else if (campo.equals("tipoDocumento")) {
                erroresOrdenados.put("tipoDocumento", violacion.getMessage());
                cbxtipoDocumento.getStyleClass().add("text-field-error");
            }
        }
        Map.Entry<String, String> primerError = erroresOrdenados.entrySet().iterator().next();
        lbnMsg.setText(primerError.getValue());
        lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
    }

    @FXML
    public void validarFormulario() {
        formulario = new Cliente();
        formulario.setNombres(txtnombres.getText());
        formulario.setDniruc(txtdniruc.getText());
        formulario.setRepLegal(txtrepLegal.getText());
        String idTipoDoc = cbxtipoDocumento.getSelectionModel().getSelectedItem() == null ? "0" : cbxtipoDocumento.getSelectionModel().getSelectedItem().getKey();
        formulario.setTipoDocumento(idTipoDoc);

        Set<ConstraintViolation<Cliente>> violaciones = validator.validate(formulario);
        List<ConstraintViolation<Cliente>> violacionesOrdenadasPorPropiedad = violaciones.stream()
                .sorted((v1, v2) -> v1.getPropertyPath().toString().compareTo(v2.getPropertyPath().toString()))
                .collect(Collectors.toList());

        if (violacionesOrdenadasPorPropiedad.isEmpty()) {
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarError();

            double width = stage.getWidth() / 1.5;
            double height = stage.getHeight() / 2;
            if (idProductoCE != 0L) {
                ms.update(formulario);
                showToast(stage, "Se actualizó correctamente!!", 2000, width, height);
            } else {
                ms.save(formulario);
                showToast(stage, "Se guardó correctamente!!", 2000, width, height);
            }
            clearForm();
            listar();
        } else {
            validarCampos(violacionesOrdenadasPorPropiedad);
        }
    }

    private void filtrarClientes(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tableView.getItems().clear();
            tableView.getItems().addAll(listarCliente);
        } else {
            String lowerCaseFilter = filtro.toLowerCase();
            List<Cliente> clientesFiltrados = listarCliente.stream()
                    .filter(cliente -> cliente.getNombres().toLowerCase().contains(lowerCaseFilter)
                            || cliente.getDniruc().toLowerCase().contains(lowerCaseFilter)
                            || cliente.getRepLegal().toLowerCase().contains(lowerCaseFilter)
                            || cliente.getTipoDocumento().toLowerCase().contains(lowerCaseFilter))
                    .collect(Collectors.toList());
            tableView.getItems().clear();
            tableView.getItems().addAll(clientesFiltrados);
        }
    }

    public void editForm(Cliente cliente) {
        txtnombres.setText(cliente.getNombres());
        txtdniruc.setText(cliente.getDniruc());
        txtrepLegal.setText(cliente.getRepLegal());
        cbxtipoDocumento.getSelectionModel().select(
                cbxtipoDocumento.getItems().stream()
                        .filter(doc -> doc.getKey().equals(cliente.getTipoDocumento()))
                        .findFirst()
                        .orElse(null)
        );
        idProductoCE = Long.parseLong(cliente.getDniruc());
        limpiarError();
    }
}
