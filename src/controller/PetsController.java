/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import PetShopNegocio.ClienteNegocio;
import PetShopNegocio.NegocioException;
import PetShopNegocio.PetsNegocio;
import View.PrintUtil;
import dominio.Cliente;
import dominio.Pets;
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
public class PetsController implements Initializable {
    
    @FXML
    private VBox painelTabelaPet;
    @FXML
    private TableView<Pets> tableViewPet;
    @FXML
    private TableColumn<Pets, String> tableColumnRg;
    @FXML
    private TableColumn<Pets, String> tableColumnNome;
    @FXML
    private TableColumn<Pets, String> tableColumnTipo;


    @FXML
    private AnchorPane painelFormularioPets1;
    @FXML
    private TextField textFieldRg;
    @FXML
    private TextField textFieldNomePets;
    @FXML
    private TextField textFieldTipoAnimal;
    
    private int tela;
    private List<Pets> listaPets;
    private Pets petsSelecionado;

    private ObservableList<Pets> observableListaPets;
    private PetsNegocio petsNegocio;
    private ClienteNegocio clienteNegocio;  
    private Cliente clientePet;
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        petsNegocio = new PetsNegocio();
        clienteNegocio = new ClienteNegocio();

        //Codigo meio redundante - por isso as vezes é melhor um controller para cada view 
        if (tableViewPet != null) {
            carregarTableViewPets();
        }
    }

    private void carregarTableViewPets() {
        tableColumnRg.setCellValueFactory(new PropertyValueFactory<>("rgDononoAnimal"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnTipo.setCellValueFactory(new PropertyValueFactory<>("tipoAnimal"));
        

        listaPets = petsNegocio.listar();
        observableListaPets = FXCollections.observableArrayList(listaPets);
        tableViewPet.setItems(observableListaPets);
    }
    
        @FXML
    public void tratarBotaoCadastrar(ActionEvent event) throws IOException {
        petsSelecionado = null;
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(PetShopMain.class.getResource("/View/PainelFormularioPets.fxml"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(painelTabelaPet.getScene().getWindow());
        stage.showAndWait();
        carregarTableViewPets();
    }
    
        @FXML
    public void tratarBotaoEditar(ActionEvent event) throws IOException {
        Pets petsSelec = tableViewPet.getSelectionModel().getSelectedItem();
        if (petsSelec != null) {
            FXMLLoader loader = new FXMLLoader(PetShopMain.class.getResource("/View/PainelFormularioPets.fxml"));
            Parent root = (Parent) loader.load();

            PetsController controller = (PetsController) loader.getController();
            controller.setPetsSelecionado(petsSelec);

            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(painelTabelaPet.getScene().getWindow());
            dialogStage.showAndWait();
            carregarTableViewPets();
        } else {
            PrintUtil.printMessageError("Precisa selecionar um cliente para esta opção");
        }
    }
    
        @FXML
    public void tratarBotaoRemover(ActionEvent event) throws IOException, NegocioException {
        Pets petsSeleci = tableViewPet.getSelectionModel().getSelectedItem();
        if (petsSeleci != null) {
            try {
                petsNegocio.deletar(petsSeleci);
                this.carregarTableViewPets();
            } catch (NegocioException ex) {
                PrintUtil.printMessageError(ex.getMessage());
            }
        } else {
            PrintUtil.printMessageError("Precisa selecionar um Pets para esta opcao");
        }
    }
    
        @FXML
    public void tratarBotaoSalvar(ActionEvent event) throws IOException {
        Stage stage = (Stage) painelFormularioPets1.getScene().getWindow();
        try {
            clientePet = clienteNegocio.procurarPorRg(textFieldRg.getText());

        }catch (NegocioException e) {}
        
        if(petsSelecionado == null) //Se for cadastrar
        {

            try {
                petsNegocio.salvar(new Pets(
                        textFieldNomePets.getText(), 
                        textFieldTipoAnimal.getText(), 
                        clientePet
                ));                
                stage.close();
            } catch (NegocioException ex) {
                PrintUtil.printMessageError(ex.getMessage());
            }
            
        }
        else //Se for editar
        {
            try {
                petsSelecionado.setNome(textFieldNomePets.getText());
                petsSelecionado.setTipoAnimal(textFieldTipoAnimal.getText());
                petsSelecionado.setDonoAnimal(clientePet);
                
                petsNegocio.atualizar(petsSelecionado);
                stage.close();
            } catch (NegocioException ex) {
                PrintUtil.printMessageError(ex.getMessage());
            }
        } 
    }
   @FXML
    public void tratarBotaoCancelar(ActionEvent event) throws IOException {
        Stage stage = (Stage) painelFormularioPets1.getScene().getWindow();
        stage.close();

    }

    public Pets getPetsSelecionado() {
        return petsSelecionado;
    }

    public void setPetsSelecionado(Pets petsSeleciona) {
        this.petsSelecionado = petsSeleciona;
        textFieldRg.setText(petsSelecionado.getDonoAnimal().getRg());
        textFieldRg.setEditable(false);
        textFieldNomePets.setText(petsSelecionado.getNome());
        textFieldTipoAnimal.setText(petsSelecionado.getTipoAnimal());

    }
}
