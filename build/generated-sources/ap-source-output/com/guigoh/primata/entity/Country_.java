package com.guigoh.primata.entity;

import com.guigoh.primata.entity.Educations;
import com.guigoh.primata.entity.Experiences;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.State;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(Country.class)
public class Country_ { 

    public static volatile SingularAttribute<Country, Integer> id;
    public static volatile CollectionAttribute<Country, State> stateCollection;
    public static volatile CollectionAttribute<Country, Educations> educationsCollection;
    public static volatile CollectionAttribute<Country, SocialProfile> socialProfileCollection;
    public static volatile SingularAttribute<Country, String> acronyms;
    public static volatile CollectionAttribute<Country, Experiences> experiencesCollection;
    public static volatile SingularAttribute<Country, String> name;

}