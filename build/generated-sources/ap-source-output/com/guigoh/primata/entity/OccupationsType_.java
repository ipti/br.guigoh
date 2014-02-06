package com.guigoh.primata.entity;

import com.guigoh.primata.entity.Occupations;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(OccupationsType.class)
public class OccupationsType_ { 

    public static volatile SingularAttribute<OccupationsType, Integer> id;
    public static volatile CollectionAttribute<OccupationsType, Occupations> occupationsCollection;
    public static volatile SingularAttribute<OccupationsType, String> name;

}