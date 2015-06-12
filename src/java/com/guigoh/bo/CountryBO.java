/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.CountryJpaController;
import com.ipti.guigoh.model.entity.Country;
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
            CountryJpaController countryDAO = new CountryJpaController();
            return countryDAO.findCountryEntities();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Country>();
        }
    }

    public static Country getCountryByName(String countryName) {
        try {
            CountryJpaController countryDAO = new CountryJpaController();
            return countryDAO.findCountryByName(countryName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Country();
        }
    }
}
