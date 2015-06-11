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
    
    public static List<Country> getAll() {
        try {
            CountryDAO countryDAO = new CountryDAO();
            return countryDAO.findCountryEntities();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Country>();
        }
    }

    public static Country getCountryByName(String countryName) {
        try {
            CountryDAO countryDAO = new CountryDAO();
            return countryDAO.findCountryByName(countryName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Country();
        }
    }
}
