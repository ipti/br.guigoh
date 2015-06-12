/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.ipti.guigoh.model.jpa.controller.CityJpaController;
import com.ipti.guigoh.model.entity.City;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class CityBO implements Serializable {
    
    public static List<City> getAll() {
        try {
            CityJpaController cityDAO = new CityJpaController();
            return cityDAO.findCityEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<City> findCitiesByStateId(Integer id) {
        try {
            CityJpaController cityDAO = new CityJpaController();
            return cityDAO.findCitysByCountryId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static City getCityByName(String cityName) {
        try {
            CityJpaController cityDAO = new CityJpaController();
            return cityDAO.findCityByName(cityName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new City();
    }
}
