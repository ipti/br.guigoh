/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.CountryDAO;
import com.guigoh.entity.Country;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class CountryBO implements Serializable {

    private CountryDAO countryDAO;
    
    public CountryBO(){
        countryDAO = new CountryDAO();
    }
    
    public List<Country> getAll() {
        try {
            return countryDAO.findCountryEntities();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Country>();
        }
    }

    public Country getCountryByName(String countryName) {
        try {
            return countryDAO.findCountryByName(countryName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Country();
        }
    }
}
