/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.CityDAO;
import com.guigoh.primata.entity.City;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class CityBO implements Serializable {

    public List<City> getAll() {
        CityDAO cityDAO = new CityDAO();
        return cityDAO.findCityEntities();
    }

    public List<City> findCitiesByStateId(Integer id) {
        CityDAO cityDAO = new CityDAO();
        return cityDAO.findCitysByCountryId(id);
    }
    

    public City getCityByName(String cityName) {
        try {
            CityDAO cityDAO = new CityDAO();
            return cityDAO.findCityByName(cityName);
        } catch (Exception e) {
            e.printStackTrace();
            return new City();
        }
    }
}
