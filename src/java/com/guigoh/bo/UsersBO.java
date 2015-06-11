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

    public static void create(Users users) {
        try {
            UsersDAO usersDAO = new UsersDAO();
            usersDAO.create(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Users findUsers(Users users) {
        try {
            UsersDAO usersDAO = new UsersDAO();
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

    public static Users findUsers(String username) {
        try {
            UsersDAO usersDAO = new UsersDAO();
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

    public static Integer getRegisteredUsersQuantity() {
        try {
            UsersDAO usersDAO = new UsersDAO();
            return usersDAO.getUsersCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void edit(Users user) {
        try {
            UsersDAO usersDAO = new UsersDAO();
            usersDAO.edit(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Users> getAll() {
        try {
            UsersDAO usersDAO = new UsersDAO();
            return usersDAO.findUsersEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
