package com.guigoh.primata.entity;

import com.guigoh.primata.entity.City;
import com.guigoh.primata.entity.Country;
import com.guigoh.primata.entity.EducationsLocation;
import com.guigoh.primata.entity.EducationsName;
import com.guigoh.primata.entity.EducationsPK;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.State;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(Educations.class)
public class Educations_ { 

    public static volatile SingularAttribute<Educations, Country> countryId;
    public static volatile SingularAttribute<Educations, EducationsPK> educationsPK;
    public static volatile SingularAttribute<Educations, Date> dataEnd;
    public static volatile SingularAttribute<Educations, SocialProfile> socialProfile;
    public static volatile SingularAttribute<Educations, City> cityId;
    public static volatile SingularAttribute<Educations, State> stateId;
    public static volatile SingularAttribute<Educations, EducationsLocation> locationId;
    public static volatile SingularAttribute<Educations, Date> dataBegin;
    public static volatile SingularAttribute<Educations, EducationsName> nameId;

}