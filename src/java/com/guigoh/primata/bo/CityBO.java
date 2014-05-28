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

    private CityDAO cityDAO;

    public CityBO() {
        cityDAO = new CityDAO();
    }

    public List<City> getAll() {
        try {
            return cityDAO.findCityEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<City> findCitiesByStateId(Integer id) {
        try {
            return cityDAO.findCitysByCountryId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public City getCityByName(String cityName) {
        try {
            return cityDAO.findCityByName(cityName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new City();
    }
}
