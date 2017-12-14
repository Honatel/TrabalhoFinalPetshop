/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PetShopDao;

import dominio.Cliente;
import dominio.Pets;
import java.util.List;

/**
 *
 * @author silva
 */
public interface Dao extends DomainCrudDao<Pets>{
    public Cliente procurarPorRGCliente(String rg);
    public Pets procurarPorRg(String rg);
    public List<Pets> listarPorNome(String nome);

}
