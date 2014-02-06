/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo.util;

import com.guigoh.primata.bo.InterestsBO;
import com.guigoh.primata.entity.Interests;
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
