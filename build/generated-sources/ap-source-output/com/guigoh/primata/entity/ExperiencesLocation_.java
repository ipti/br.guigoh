package com.guigoh.primata.entity;

import com.guigoh.primata.entity.Experiences;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(ExperiencesLocation.class)
public class ExperiencesLocation_ { 

    public static volatile SingularAttribute<ExperiencesLocation, Integer> id;
    public static volatile CollectionAttribute<ExperiencesLocation, Experiences> experiencesCollection;
    public static volatile SingularAttribute<ExperiencesLocation, String> name;

}