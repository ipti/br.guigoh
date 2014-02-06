package com.guigoh.primata.entity;

import com.guigoh.primata.entity.Experiences;
import com.guigoh.primata.entity.OccupationsType;
import com.guigoh.primata.entity.SocialProfile;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(Occupations.class)
public class Occupations_ { 

    public static volatile SingularAttribute<Occupations, Integer> id;
    public static volatile SingularAttribute<Occupations, OccupationsType> occupationsTypeId;
    public static volatile CollectionAttribute<Occupations, SocialProfile> socialProfileCollection;
    public static volatile CollectionAttribute<Occupations, Experiences> experiencesCollection;
    public static volatile SingularAttribute<Occupations, String> name;

}