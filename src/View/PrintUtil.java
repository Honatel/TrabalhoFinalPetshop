/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javax.swing.JOptionPane;

/**
 *
 * @author lhries
 */
public class PrintUtil {
    //Se for JavaFX 8, alterar para Alert
    public static void printMessageError(String msg) {
            JOptionPane.showMessageDialog(null, 
                    msg,
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);        
    }

    public static void printMessageSucesso(String msg) {
            JOptionPane.showMessageDialog(null, 
                    msg,
                    "Sucesso",
                    JOptionPane.PLAIN_MESSAGE);        
    }       
}
