/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipti.guigoh.util;

import com.guigoh.bo.InterestsBO;
import com.ipti.guigoh.model.entity.Interests;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Joe
 */
@FacesConverter(value = "InterestsConverter", forClass = InterestsConverter.class)
public class InterestsConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        try {
            value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(InterestsConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (value == null || value.length() == 0) {
            return null;
        }
        InterestsBO interestsBO = new InterestsBO();

        Interests interests = interestsBO.findInterestsByInterestsName(value);
        interests.setName(value);
        return interests;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Interests) {
            Interests o = (Interests) object;
            if (o.getName() == null || o.getName().length() == 0) {
                return "";
            } else {
                return String.valueOf(o.getName());
            }
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Interests.class.getName());
        }
    }
}

