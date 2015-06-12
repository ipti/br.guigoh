package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.UsersJpaController;
import com.ipti.guigoh.model.entity.Users;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joerlan Lima
 */
public class UsersBO implements Serializable {

    public static void create(Users users) {
        try {
            UsersJpaController usersDAO = new UsersJpaController();
            usersDAO.create(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Users findUsers(Users users) {
        try {
            UsersJpaController usersDAO = new UsersJpaController();
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
            UsersJpaController usersDAO = new UsersJpaController();
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
            UsersJpaController usersDAO = new UsersJpaController();
            return usersDAO.getUsersCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void edit(Users user) {
        try {
            UsersJpaController usersDAO = new UsersJpaController();
            usersDAO.edit(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Users> getAll() {
        try {
            UsersJpaController usersDAO = new UsersJpaController();
            return usersDAO.findUsersEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
