/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PetShopDao;

import dominio.Cliente;
import java.util.List;

/**
 *
 * @author silva
 */
public interface ClienteDao extends DomainCrudDao<Cliente>{
    public Cliente procurarPorRg(String rg);
    public List<Cliente> listarPorNome(String nome);
}
