/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.CityDAO;
import com.guigoh.entity.City;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class CityBO implements Serializable {
    
    public static List<City> getAll() {
        try {
            CityDAO cityDAO = new CityDAO();
            return cityDAO.findCityEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<City> findCitiesByStateId(Integer id) {
        try {
            CityDAO cityDAO = new CityDAO();
            return cityDAO.findCitysByCountryId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static City getCityByName(String cityName) {
        try {
            CityDAO cityDAO = new CityDAO();
            return cityDAO.findCityByName(cityName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new City();
    }
}
