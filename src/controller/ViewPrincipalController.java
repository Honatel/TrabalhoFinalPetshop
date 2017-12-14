/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import PetShopNegocio.NegocioException;
import View.PrintUtil;
import dominio.ServicosPets;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author silva
 */
public class ViewPrincipalController implements Initializable {
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

        @FXML
    public void tratarBotaoCadastrarCliente(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("/View/PainelTabelaCliente.fxml"));

        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
       
    }
    
        @FXML
    public void tratarBotaoCadastrarPets(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("/View/PainelTabelaPet.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
       
    }
    
        @FXML
    public void tratarBotaoCadastrarServios(ActionEvent event) throws IOException, NegocioException {
        Stage stage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("/View/PainelTabelaServicos.fxml"));

        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
       
    }
   
}
