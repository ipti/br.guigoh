package com.guigoh.bo;

import com.guigoh.dao.UsersDAO;
import com.guigoh.entity.Users;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joerlan Lima
 */
public class UsersBO implements Serializable {

    private UsersDAO usersDAO;
    
    public UsersBO(){
        usersDAO = new UsersDAO();
    }

    public void create(Users users) {
        try {
            usersDAO.create(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Users findUsers(Users users) {
        try {
            Users user = usersDAO.findUsers(users.getUsername());
            if (user == null) {
                return new Users();
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return new Users();
        }
    }

    public Users findUsers(String username) {
        try {
            Users user = usersDAO.findUsers(username);
            if (user == null) {
                return new Users();
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return new Users();
        }
    }

    public Integer getRegisteredUsersQuantity() {
        try {
            return usersDAO.getUsersCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void edit(Users user) {
        try {
            usersDAO.edit(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Users> getAll() {
        try {
            return usersDAO.findUsersEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
