package com.guigoh.primata.entity;

import com.guigoh.primata.entity.City;
import com.guigoh.primata.entity.Country;
import com.guigoh.primata.entity.ExperiencesLocation;
import com.guigoh.primata.entity.ExperiencesPK;
import com.guigoh.primata.entity.Occupations;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.State;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(Experiences.class)
public class Experiences_ { 

    public static volatile SingularAttribute<Experiences, Country> countryId;
    public static volatile SingularAttribute<Experiences, Date> dataEnd;
    public static volatile SingularAttribute<Experiences, SocialProfile> socialProfile;
    public static volatile SingularAttribute<Experiences, City> cityId;
    public static volatile SingularAttribute<Experiences, State> stateId;
    public static volatile SingularAttribute<Experiences, ExperiencesPK> experiencesPK;
    public static volatile SingularAttribute<Experiences, ExperiencesLocation> locationId;
    public static volatile SingularAttribute<Experiences, Date> dataBegin;
    public static volatile SingularAttribute<Experiences, Occupations> nameId;

}