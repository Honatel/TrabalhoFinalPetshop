/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import PetShopNegocio.ClienteNegocio;
import PetShopNegocio.NegocioException;
import View.PrintUtil;
import dominio.Cliente;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import trabalhopetshop.PetShopMain;
import util.DateUtil;

/**
 *
 * @author silva
 */
public class ClienteController implements Initializable {

    @FXML
    private VBox painelTabelaCliente;
    @FXML
    private TableView<Cliente> tableViewClientes;
    @FXML
    private TableColumn<Cliente, String> tableColumnRg;
    @FXML
    private TableColumn<Cliente, String> tableColumnNome;
    @FXML
    private TableColumn<Cliente, String> tableColumnTelfone;
    @FXML
    //Formato de data dd/MM/yyyy
    private TableColumn<Cliente, String> tableColumnDataNascimento;

    @FXML
    private AnchorPane painelFormularioCliente;
    @FXML
    private TextField textFieldRg;
    @FXML
    private TextField textFieldNome;
    @FXML
    private TextField textFieldTelefone;
    @FXML
    private DatePicker datePickerDataNascimento;

    private int tela;
    private List<Cliente> listaClientes;
    private Cliente clienteSelelcionado;

    private ObservableList<Cliente> observableListaCliente;
    private ClienteNegocio clientenegocio;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clientenegocio = new ClienteNegocio();

        //Codigo meio redundante - por isso as vezes é melhor um controller para cada view 
        if (tableViewClientes != null) {
            carregarTableViewClientes();
        }
    }

    private void carregarTableViewClientes() {
        tableColumnRg.setCellValueFactory(new PropertyValueFactory<>("rg"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnTelfone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        tableColumnDataNascimento.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Cliente, String> cell) {
                final Cliente cliente = cell.getValue();
                final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty(
                        DateUtil.dateToString(cliente.getDataNascimento())
                );
                return simpleObject;
            }
        });
        listaClientes = clientenegocio.listar();
        observableListaCliente = FXCollections.observableArrayList(listaClientes);
        tableViewClientes.setItems(observableListaCliente);
    }

    @FXML
    public void tratarBotaoCadastrar(ActionEvent event) throws IOException {
        clienteSelelcionado = null;
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(PetShopMain.class.getResource("/View/FormularioCliente.fxml"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(painelTabelaCliente.getScene().getWindow());
        stage.showAndWait();
        carregarTableViewClientes();
    }

    @FXML
    public void tratarBotaoEditar(ActionEvent event) throws IOException {
        Cliente pacienteSelec = tableViewClientes.getSelectionModel().getSelectedItem();
        if (pacienteSelec != null) {
            FXMLLoader loader = new FXMLLoader(PetShopMain.class.getResource("/View/FormularioCliente.fxml"));
            Parent root = (Parent) loader.load();

            ClienteController controller = (ClienteController) loader.getController();
            controller.setClienteSelecionado(pacienteSelec);

            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(painelTabelaCliente.getScene().getWindow());
            dialogStage.showAndWait();
            carregarTableViewClientes();
        } else {
            PrintUtil.printMessageError("Precisa selecionar um cliente para esta opção");
        }
    }

    @FXML
    public void tratarBotaoRemover(ActionEvent event) throws IOException, NegocioException {
        Cliente clienteSelec = tableViewClientes.getSelectionModel().getSelectedItem();
        if (clienteSelec != null) {
            try {
                clientenegocio.deletar(clienteSelec);
                this.carregarTableViewClientes();
            } catch (NegocioException ex) {
                PrintUtil.printMessageError(ex.getMessage());
            }
        } else {
            PrintUtil.printMessageError("Precisa selecionar um paciente para esta opcao");
        }
    }

    @FXML
    public void tratarBotaoSalvar(ActionEvent event) throws IOException {
        Stage stage = (Stage) painelFormularioCliente.getScene().getWindow();

        if (clienteSelelcionado == null) //Se for cadastrar
        {
            try {
                clientenegocio.salvar(new Cliente(
                        textFieldRg.getText(),
                        textFieldNome.getText(),
                        textFieldTelefone.getText(),
                        datePickerDataNascimento.getValue()
                ));
                stage.close();
            } catch (NegocioException ex) {
                PrintUtil.printMessageError(ex.getMessage());
            }

        } else //Se for editar
        {
            try {
                clienteSelelcionado.setNome(textFieldNome.getText());
                clienteSelelcionado.setTelefone(textFieldTelefone.getText());
                clienteSelelcionado.setDataNascimento(
                        datePickerDataNascimento.getValue()
                );
                clientenegocio.atualizar(clienteSelelcionado);
                stage.close();
            } catch (NegocioException ex) {
                PrintUtil.printMessageError(ex.getMessage());
            }
        }
    }

    @FXML
    public void tratarBotaoCancelar(ActionEvent event) throws IOException {
        Stage stage = (Stage) painelFormularioCliente.getScene().getWindow();
        stage.close();

    }

    public Cliente getPacienteSelecionado() {
        return clienteSelelcionado;
    }

    public void setClienteSelecionado(Cliente clienteteSelecionado) {
        this.clienteSelelcionado = clienteteSelecionado;
        textFieldRg.setText(clienteSelelcionado.getRg());
        textFieldRg.setEditable(false);
        textFieldNome.setText(clienteSelelcionado.getNome());
        textFieldTelefone.setText(clienteSelelcionado.getTelefone());
        datePickerDataNascimento.setValue(clienteSelelcionado.getDataNascimento());
    }
}
