/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo.util;


import com.guigoh.bo.TagsBO;
import com.guigoh.entity.Tags;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Joe
 */
@FacesConverter(value = "TagsConverter", forClass = TagsConverter.class)
public class TagsConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        TagsBO tagsBO = new TagsBO();

        Tags tags = tagsBO.findTagsByName(value);
        tags.setName(value);
        return tags;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Tags) {
            Tags o = (Tags) object;
            if (o.getName() == null || o.getName().length() == 0) {
                return "";
            } else {
                return String.valueOf(o.getName());
            }
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Tags.class.getName());
        }
    }
}
