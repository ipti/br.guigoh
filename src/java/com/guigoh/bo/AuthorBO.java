/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.AuthorJpaController;
import com.ipti.guigoh.model.entity.Author;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ipti008
 */
public class AuthorBO implements Serializable{
    
    public static void create(Author author){
        try{
            AuthorJpaController authorDAO = new AuthorJpaController();
            authorDAO.create(author);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static void edit(Author author){
        try{
            AuthorJpaController authorDAO = new AuthorJpaController();
            authorDAO.edit(author);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static List<Author> getAuthorsByEducationalObject(Integer educationalObjectId){
        try{
            AuthorJpaController authorDAO = new AuthorJpaController();
            return authorDAO.getAuthorsByEducationalObject(educationalObjectId);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
