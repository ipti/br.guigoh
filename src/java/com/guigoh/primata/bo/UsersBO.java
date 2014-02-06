package com.guigoh.primata.bo;

import com.guigoh.primata.dao.UsersDAO;
import com.guigoh.primata.entity.Users;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joerlan Lima
 */
public class UsersBO implements Serializable {

    public void create(Users users) {
        try {
            UsersDAO usersDAO = new UsersDAO();
            usersDAO.create(users);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Users findUsers(Users users) {
        try {
            UsersDAO usersDAO = new UsersDAO();
            Users user = usersDAO.findUsers(users.getUsername());
            if (user == null) {
                return new Users();
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Users();
    }

    public Users findUsers(String username) {
        try {
            UsersDAO usersDAO = new UsersDAO();
            Users user = usersDAO.findUsers(username);
            if (user == null) {
                return new Users();
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Users();
    }
    
    public Integer getRegisteredUsersQuantity(){
        try {
            UsersDAO usersDAO = new UsersDAO();
            return usersDAO.getUsersCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void edit(Users user) {
        try {
            UsersDAO usersDAO = new UsersDAO();
            usersDAO.edit(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     public List<Users> getAll(){
        UsersDAO usersDAO = new UsersDAO();
        return usersDAO.findUsersEntities();
    }
}
