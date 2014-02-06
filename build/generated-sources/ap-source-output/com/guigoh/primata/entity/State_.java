package com.guigoh.primata.entity;

import com.guigoh.primata.entity.City;
import com.guigoh.primata.entity.Country;
import com.guigoh.primata.entity.Educations;
import com.guigoh.primata.entity.Experiences;
import com.guigoh.primata.entity.SocialProfile;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(State.class)
public class State_ { 

    public static volatile SingularAttribute<State, Integer> id;
    public static volatile SingularAttribute<State, Country> countryId;
    public static volatile CollectionAttribute<State, Educations> educationsCollection;
    public static volatile CollectionAttribute<State, SocialProfile> socialProfileCollection;
    public static volatile SingularAttribute<State, String> acronyms;
    public static volatile CollectionAttribute<State, Experiences> experiencesCollection;
    public static volatile SingularAttribute<State, String> name;
    public static volatile CollectionAttribute<State, City> cityCollection;

}