/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import PetShopNegocio.NegocioException;
import PetShopNegocio.ServicosNegocio;
import View.PrintUtil;
import dominio.ServicosPets;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import trabalhopetshop.PetShopMain;

/**
 *
 * @author silva
 */
public class ServicosController implements Initializable {
    
    @FXML
    private VBox painelTabelaServico;
    @FXML
    private TableView<ServicosPets> tableViewServicos;
    @FXML
    private TableColumn<ServicosPets, String> tableColumnPreco;
    @FXML
    private TableColumn<ServicosPets, String> tableColumnTipo;

    @FXML
    private AnchorPane painelFormularioServico;
    @FXML
    private TextField textFieldTipo;
    @FXML
    private TextField textFieldPreco;


    private int tela;
    private List<ServicosPets> listaServicos;
    private ServicosPets servicosSelecionados;

    private ObservableList<ServicosPets> observableListaServicos;
    private ServicosNegocio servicosNegocio; 
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servicosNegocio = new ServicosNegocio();

        //Codigo meio redundante - por isso as vezes é melhor um controller para cada view 
        if (tableViewServicos != null) {
            carregarTableViewServicos();
        }
    }

    private void carregarTableViewServicos() {
        tableColumnTipo.setCellValueFactory(new PropertyValueFactory<>("tipoAtendimento"));
        tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("precoServico"));
        

        listaServicos = servicosNegocio.listar();
        observableListaServicos = FXCollections.observableArrayList(listaServicos);
        tableViewServicos.setItems(observableListaServicos);
    }
    
        @FXML
    public void tratarBotaoCadastrar(ActionEvent event) throws IOException {
        servicosSelecionados = null;
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(PetShopMain.class.getResource("/View/PainelFormularioServico.fxml"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(painelTabelaServico.getScene().getWindow());
        stage.showAndWait();
        carregarTableViewServicos();
    }
    
        @FXML
    public void tratarBotaoEditar(ActionEvent event) throws IOException {
        ServicosPets servicoSel = tableViewServicos.getSelectionModel().getSelectedItem();
        if (servicoSel != null) {
            FXMLLoader loader = new FXMLLoader(PetShopMain.class.getResource("/View/PainelFormularioServico.fxml"));
            Parent root = (Parent) loader.load();

            ServicosController controller = (ServicosController) loader.getController();
            controller.setServicoSelecionado(servicoSel);

            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(painelTabelaServico.getScene().getWindow());
            dialogStage.showAndWait();
            carregarTableViewServicos();
        } else {
            PrintUtil.printMessageError("Precisa selecionar um Servico para esta opção");
        }
    }
    
        @FXML
    public void tratarBotaoRemover(ActionEvent event) throws IOException, NegocioException {
        ServicosPets servicoSelec = tableViewServicos.getSelectionModel().getSelectedItem();
        
        if (servicoSelec != null) {
            try {
                servicosNegocio.deletar(servicoSelec);
                this.carregarTableViewServicos();
            } catch (NegocioException ex) {
                PrintUtil.printMessageError(ex.getMessage());
            }
        } else {
            PrintUtil.printMessageError("Precisa selecionar um servico para esta opcao");
        }
    }
    
        @FXML
    public void tratarBotaoSalvar(ActionEvent event) throws IOException {
        Stage stage = (Stage) painelFormularioServico.getScene().getWindow();
        
        if(servicosSelecionados == null) //Se for cadastrar
        {
            try {
                servicosNegocio.salvar(new ServicosPets(
                        Double.parseDouble(textFieldPreco.getText()), 
                        textFieldTipo.getText() 
                ));                
                stage.close();
            } catch (NegocioException ex) {
                PrintUtil.printMessageError(ex.getMessage());
            }
            
        }
        else //Se for editar
        {
            try {
                servicosSelecionados.setTipoAtendimento(textFieldTipo.getText());
                servicosSelecionados.setPrecoServico(Double.parseDouble(textFieldPreco.getText()));  
                
                servicosNegocio.atualizar(servicosSelecionados);
                stage.close();
            } catch (NegocioException ex) {
                PrintUtil.printMessageError(ex.getMessage());
            }
        } 
    }
   @FXML
    public void tratarBotaoCancelar(ActionEvent event) throws IOException {
        Stage stage = (Stage) painelFormularioServico.getScene().getWindow();
        stage.close();

    }

    public ServicosPets getServicoSelecionado() {
        return servicosSelecionados;
    }

    public void setServicoSelecionado(ServicosPets servicoSele) {
        this.servicosSelecionados = servicoSele;
        textFieldTipo.setText(servicosSelecionados.getTipoAtendimento());
        textFieldPreco.setText(String.valueOf(servicosSelecionados.getPrecoServico()));
    }
}
