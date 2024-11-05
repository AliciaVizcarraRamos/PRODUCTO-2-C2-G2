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
import pe.edu.upeu.sysalmacenfx.dto.ComboBoxOption;
import pe.edu.upeu.sysalmacenfx.modelo.Usuario;
import pe.edu.upeu.sysalmacenfx.servicio.PerfilService;
import pe.edu.upeu.sysalmacenfx.servicio.UsuarioService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static pe.edu.upeu.sysalmacenfx.componente.Toast.showToast;


@Component
public class AccountCreationControl {
    @FXML
    TextField txtNombre,txtApellido,txtDNI,txtTelf,txtContra,txtUserName,txtFiltroDato;



    @FXML
    ComboBox<ComboBoxOption> cbxRol;

    @FXML
    private TableView<Usuario> tableView;

    @FXML
    private AnchorPane miContenedor;
    Stage stage;

    @Autowired
    PerfilService ps;

    @Autowired
    UsuarioService us;

    private Validator validator;
    ObservableList<Usuario> listarUsuario;
    Usuario formulario;
    Long idUsuarioCE=0L;
    private Labeled lbnMsg;

    public void initialize(){
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



        cbxRol.setTooltip(new Tooltip());
        cbxRol.getItems().addAll(ps.listarCombobox());
        cbxRol.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxRol.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del producto seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxRol);


        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();


        // Crear instancia de la clase genérica TableViewHelper
        TableViewHelper<Usuario> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("idUsuario", 60.0)); // Columna visible "Columna 1" mapea al campo "campo1"
        columns.put("Nombre", new ColumnInfo("nombre", 180.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("Apellido", new ColumnInfo("apellido", 180.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("DNI", new ColumnInfo("dni", 100.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("UserName", new ColumnInfo("user", 180.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("Rol", new ColumnInfo("Perfil.nombre", 120.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("Telf", new ColumnInfo("telf", 100.0));
        columns.put("Contraseña", new ColumnInfo("clave", 180.0));

        // Definir las acciones de actualizar y eliminar
        Consumer<Usuario> updateAction = (Usuario usuario) -> {
            System.out.println("Actualizar: " + usuario);
            editForm(usuario);
        };
        Consumer<Usuario> deleteAction = (Usuario usuario) -> {System.out.println("Actualizar: " + usuario);
            us.delete(usuario.getIdUsuario()); //deletePerson(usuario);/
            double with=stage.getWidth()/1.5;
            double h=stage.getHeight()/2;
            showToast(stage, "Se eliminó correctamente!!", 2000, with, h);
            listar();
        };


        // Usar el helper para agregar las columnas en el orden correcto
        tableViewHelper.addColumnsInOrderWithSize(tableView, columns,updateAction, deleteAction );
        // Agregar botones de eliminar y modificar
        tableView.setTableMenuButtonVisible(true);
        listar();

    }

    public void listar(){
        try {
            tableView.getItems().clear();
            listarUsuario = FXCollections.observableArrayList(us.List());
            tableView.getItems().addAll(listarUsuario);
            // Agregar un listener al campo de texto txtFiltroDato para filtrar los productos
            txtFiltroDato.textProperty().addListener((observable, oldValue, newValue) -> {
                filtrarProductos(newValue);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void limpiarError(){
        txtNombre.getStyleClass().remove("text-field-error");
        txtApellido.getStyleClass().remove("text-field-error");
        txtContra.getStyleClass().remove("text-field-error");
        txtTelf.getStyleClass().remove("text-field-error");
        txtUserName.getStyleClass().remove("text-field-error");
        txtDNI.getStyleClass().remove("text-field-error");
        cbxRol.getStyleClass().remove("text-field-error");
    }

    public void clearForm(){
        txtNombre.setText("");
        txtApellido.setText("");
        txtContra.setText("");
        txtTelf.setText("");
        txtUserName.setText("");
        txtDNI.setText("");
        cbxRol.getSelectionModel().select(null);
        idUsuarioCE=0L;
        limpiarError();
    }

    @FXML
    public void cancelarAccion(){
        clearForm();
        limpiarError();
    }

    void validarCampos(List<ConstraintViolation<Usuario>> violacionesOrdenadasPorPropiedad) {
        // Crear un LinkedHashMap para ordenar las violaciones
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        // Mostrar el primer mensaje de error
        for (ConstraintViolation<Usuario> violacion : violacionesOrdenadasPorPropiedad) {
            String campo = violacion.getPropertyPath().toString();
            if (campo.equals("nombre")) {
                erroresOrdenados.put("nombre", violacion.getMessage());
                txtNombre.getStyleClass().add("text-field-error");
            } else if (campo.equals("apellido")) {
                erroresOrdenados.put("apellido", violacion.getMessage());
                txtApellido.getStyleClass().add("text-field-error");
            } else if (campo.equals("user")) {
                erroresOrdenados.put("user", violacion.getMessage());
                txtUserName.getStyleClass().add("text-field-error");
            } else if (campo.equals("telf")) {
                erroresOrdenados.put("telf", violacion.getMessage());
                txtTelf.getStyleClass().add("text-field-error");
            } else if (campo.equals("clave")) {
                erroresOrdenados.put("clave", violacion.getMessage());
                txtContra.getStyleClass().add("text-field-error");
            } else if (campo.equals("Perfil")) {
                erroresOrdenados.put("Perfil", violacion.getMessage());
                cbxRol.getStyleClass().add("text-field-error");
            }
            // Mostrar el primer error en el orden deseado
            Map.Entry<String, String> primerError = erroresOrdenados.entrySet().iterator().next();
            lbnMsg.setText(primerError.getValue()); // Mostrar el mensaje del primer error
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        }

    }

    @FXML
    public void validarFormulario() {
        formulario = new Usuario();
        formulario.setNombre(txtNombre.getText());
        formulario.setApellido(txtApellido.getText());
        formulario.setUser(txtUserName.getText());
        formulario.setClave(txtContra.getText());
        formulario.setDni(txtDNI.getText()==""?"0":txtDNI.getText());
        formulario.setNumero(txtTelf.getText()==""?"0":txtTelf.getText());
        String idxM=cbxRol.getSelectionModel().getSelectedItem()==null?"0":cbxRol.getSelectionModel().getSelectedItem().getKey();
        formulario.setIdPerfil(ps.searchById(Long.parseLong(idxM)));
        Set<ConstraintViolation<Usuario>> violaciones = validator.validate(formulario);
        // Si prefieres ordenarlo por el nombre de la propiedad que violó la restricción, podrías usar:
        List<ConstraintViolation<Usuario>> violacionesOrdenadasPorPropiedad = violaciones.stream()
                .sorted((v1, v2) -> v1.getPropertyPath().toString().compareTo(v2.getPropertyPath().toString()))
                .collect(Collectors.toList());
        if (violacionesOrdenadasPorPropiedad.isEmpty()) {
            // Los datos son válidos
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarError();
            double with=stage.getWidth()/1.5;
            double h=stage.getHeight()/2;
            if(idUsuarioCE!=0L && idUsuarioCE>0L){
                formulario.setIdUsuario(idUsuarioCE);
                us.update(formulario);
                showToast(stage, "Se actualizó correctamente!!", 2000, with, h);
                clearForm();
            }else{
                us.save(formulario);
                showToast(stage, "Se guardo correctamente!!", 2000, with, h);
                clearForm();
            }
            listar();
        } else {
            validarCampos(violacionesOrdenadasPorPropiedad);
        }

    }

    private void filtrarProductos(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            // Si el filtro está vacío, volver a mostrar la lista completa
            tableView.getItems().clear();
            tableView.getItems().addAll(listarUsuario);
        } else {
            // Aplicar el filtro
            String lowerCaseFilter = filtro.toLowerCase();
            List<Usuario> UsuariosFiltrados = listarUsuario.stream()
                    .filter(usuario -> {
                        // Verificar si el filtro coincide con alguno de los campos
                        if (usuario.getUser().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (usuario.getApellido().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (usuario.getUser().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (usuario.getClave().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (usuario.getIdPerfil().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (String.valueOf(usuario.getDni()).contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (String.valueOf(usuario.getNumero()).contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false; // Si no coincide con ningún campo
                    })
                    .collect(Collectors.toList());
            // Actualizar los items del TableView con los productos filtrados
            tableView.getItems().clear();
            tableView.getItems().addAll(UsuariosFiltrados);
        }
    }

    public void editForm(Usuario usuario){
        txtNombre.setText(usuario.getNombre());
        txtApellido.setText(usuario.getApellido());
        txtContra.setText(usuario.getClave());
        txtUserName.setText(usuario.getUser());
        txtDNI.setText(usuario.getDni());
        txtTelf.setText(usuario.getNumero());
        // Seleccionar el ítem en cbxMarca según el ID de Marca
        cbxRol.getSelectionModel().select(
                cbxRol.getItems().stream()
                        .filter(perfil -> Long.parseLong(perfil.getKey())==usuario.getIdPerfil().getIdPerfil())
                        .findFirst()
                        .orElse(null)
        );
        idUsuarioCE=usuario.getIdUsuario();
        limpiarError();
    }
}
