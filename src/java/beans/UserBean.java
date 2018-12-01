/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.UsersDAO;
import dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Users;

/**
 *
 * @author kenji
 */
@ManagedBean
@ViewScoped
public class UserBean {

    /**
     * Creates a new instance of UserBean
     */
    
    private Users user;
    private UsersDAO dao;
    private List<Users> users;

    public UserBean() {
        user = new Users();
        users = new ArrayList<>();
        dao = new UsersDAO(javax.persistence.Persistence.createEntityManagerFactory("ControleEntradaSaida2PU"));
    }
    
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public UsersDAO getDao() {
        return dao;
    }

    public void setDao(UsersDAO dao) {
        this.dao = dao;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }
    
        public void inserir() {
        dao.create(user);
    }
    
    public List<Users> listar() {
        users = dao.findUsersEntities();
        return users;
    }
    
    public void excluir(Users c) {
        try {
            dao.destroy(c.getId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
