/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.City;
import br.org.ipti.guigoh.model.entity.Country;
import br.org.ipti.guigoh.model.entity.Doc;
import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.Educations;
import br.org.ipti.guigoh.model.entity.EducationsLocation;
import br.org.ipti.guigoh.model.entity.EducationsName;
import br.org.ipti.guigoh.model.entity.Experiences;
import br.org.ipti.guigoh.model.entity.ExperiencesLocation;
import br.org.ipti.guigoh.model.entity.Friends;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.Occupations;
import br.org.ipti.guigoh.model.entity.Scholarity;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.State;
import br.org.ipti.guigoh.model.jpa.controller.CityJpaController;
import br.org.ipti.guigoh.model.jpa.controller.CountryJpaController;
import br.org.ipti.guigoh.model.jpa.controller.DocJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationsLocationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationsNameJpaController;
import br.org.ipti.guigoh.model.jpa.controller.ExperiencesJpaController;
import br.org.ipti.guigoh.model.jpa.controller.ExperiencesLocationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.FriendsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.OccupationsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.ScholarityJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.StateJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UserAuthorizationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.DownloadService;
import br.org.ipti.guigoh.util.UploadService;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Part;

/**
 *
 * @author IPTI
 */
@ViewScoped
@Named
public class ProfileViewBean implements Serializable {

    private SocialProfile socialProfile, mySocialProfile;

    private List<String> editFieldList;
    private List<Interests> interestList;
    private List<City> cityList;
    private List<State> stateList;
    private List<Country> countryList;
    private List<Scholarity> scholarityList;
    private List<EducationalObject> educationalObjectList;
    private List<Doc> docList;

    private SocialProfileJpaController socialProfileJpaController;
    private FriendsJpaController friendsJpaController;
    private EducationalObjectJpaController educationalObjectJpaController;
    private OccupationsJpaController occupationsJpaController;
    private EducationsJpaController educationsJpaController;
    private EducationsNameJpaController educationsNameJpaController;
    private EducationsLocationJpaController educationsLocationJpaController;
    private ExperiencesJpaController experiencesJpaController;
    private InterestsJpaController interestsJpaController;
    private CityJpaController cityJpaController;
    private StateJpaController stateJpaController;
    private CountryJpaController countryJpaController;
    private ScholarityJpaController scholarityJpaController;
    private ExperiencesLocationJpaController experiencesLocationJpaController;
    private DocJpaController docJpaController;

    private Part uploadedPhoto;

    private Integer[] cropCoordinates;
    private Object[] editableFieldValues;

    private Boolean himself;
    private String editableFieldValue;
    private Integer socialProfileId;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }

    public void uploadPhoto(String type) throws IOException, NonexistentEntityException, RollbackFailureException, Exception {
        String basePath = File.separator + "home" + File.separator + "www" + File.separator
                + "com.guigoh.cdn" + File.separator + "guigoh" + File.separator + "users" + File.separator
                + socialProfile.getSocialProfileId() + File.separator + type + File.separator;
        String linkPath = "http://cdn.guigoh.com/guigoh/users/" + socialProfile.getSocialProfileId() + "/" + type + "/" + uploadedPhoto.getSubmittedFileName();
        UploadService.uploadFile(uploadedPhoto, basePath, cropCoordinates);
        switch (type) {
            case "photo":
                socialProfile.setPhoto(linkPath);
                break;
            case "cover":
                socialProfile.setCoverPhoto(linkPath);
                break;
        }
        socialProfileJpaController.edit(socialProfile);
    }

    public Friends isFriend() {
        return friendsJpaController.isFriend(socialProfile.getTokenId(), CookieService.getCookie("token"));
    }

    public void addFriend() throws RollbackFailureException, Exception {
        friendsJpaController.addFriend(socialProfileJpaController.findSocialProfile(CookieService.getCookie("token")).getUsers(), socialProfile.getSocialProfileId());
    }

    public void removeFriend() throws RollbackFailureException, Exception {
        friendsJpaController.removeFriend(socialProfileJpaController.findSocialProfile(CookieService.getCookie("token")).getUsers(), socialProfile.getSocialProfileId());
    }

    public void showEditField(String field) {
        switch (field) {
            case "occupation":
                editableFieldValue = (socialProfile.getOccupationsId() == null) ? "" : socialProfile.getOccupationsId().getName();
                break;
            case "description":
                editableFieldValue = socialProfile.getDescription();
                break;
            case "mattersOfInterest":
                editableFieldValue = socialProfile.getMattersOfInterest();
                break;
            case "musics":
                editableFieldValue = socialProfile.getMusics();
                break;
            case "books":
                editableFieldValue = socialProfile.getBooks();
                break;
            case "movies":
                editableFieldValue = socialProfile.getMovies();
                break;
            case "sports":
                editableFieldValue = socialProfile.getSports();
                break;
            case "hobbies":
                editableFieldValue = socialProfile.getHobbies();
                break;
            case "birthDate":
                editableFieldValue = socialProfile.getBirthDate();
                break;
            case "phone":
                editableFieldValue = socialProfile.getPhone();
                break;
            case "address":
                editableFieldValues[0] = socialProfile.getAddress();
                editableFieldValues[1] = socialProfile.getNumber();
                editableFieldValues[2] = socialProfile.getNeighborhood();
                editableFieldValues[3] = socialProfile.getZipcode();
                editableFieldValues[4] = (socialProfile.getCityId() == null) ? null : String.valueOf(socialProfile.getCityId().getId());
                editableFieldValues[5] = (socialProfile.getStateId() == null) ? null : String.valueOf(socialProfile.getStateId().getId());
                editableFieldValues[6] = (socialProfile.getCountryId() == null) ? null : String.valueOf(socialProfile.getCountryId().getId());
                loadStates();
                loadCities();
                break;
            case "education":
                editableFieldValues = new Object[7];
                break;
            case "experience":
                editableFieldValues = new Object[7];
                break;
        }
        editFieldList.add(field);
    }

    public void loadStates() {
        stateList = (editableFieldValues[6] != null) ? stateJpaController.findStatesByCountryId(Integer.parseInt((String) editableFieldValues[6])) : new ArrayList<>();
        cityList = new ArrayList<>();
    }

    public void loadCities() {
        cityList = (editableFieldValues[5] != null) ? cityJpaController.findCitiesByStateId(Integer.parseInt((String) editableFieldValues[5])) : new ArrayList<>();
    }

    public void removeEducation(Educations education) throws NonexistentEntityException, RollbackFailureException, Exception {
        socialProfile.getEducationsCollection().remove(education);
        educationsJpaController.destroy(education.getEducationsPK());
    }

    public void removeExperience(Experiences experience) throws RollbackFailureException, Exception {
        socialProfile.getExperiencesCollection().remove(experience);
        experiencesJpaController.destroy(experience.getExperiencesPK());
    }

    public void editField(String field) throws Exception {
        if (editableFieldValue != null && editableFieldValue.equals("")) {
            editableFieldValue = null;
        }
        for (int i = 0; i < editableFieldValues.length; i++) {
            if (editableFieldValues[i] != null && editableFieldValues[i].equals("")) {
                editableFieldValues[i] = null;
            }
        }
        switch (field) {
            case "occupation":
                Occupations occupation = null;
                if (editableFieldValue != null) {
                    occupation = occupationsJpaController.findOccupationByName(editableFieldValue);
                    if (occupation == null) {
                        occupation = new Occupations();
                        occupation.setName(editableFieldValue);
                        occupationsJpaController.create(occupation);
                    }
                }
                socialProfile.setOccupationsId(occupation);
                break;
            case "description":
                socialProfile.setDescription(editableFieldValue);
                break;
            case "mattersOfInterest":
                socialProfile.setMattersOfInterest(editableFieldValue);
                break;
            case "musics":
                socialProfile.setMusics(editableFieldValue);
                break;
            case "books":
                socialProfile.setBooks(editableFieldValue);
                break;
            case "movies":
                socialProfile.setMovies(editableFieldValue);
                break;
            case "sports":
                socialProfile.setSports(editableFieldValue);
                break;
            case "hobbies":
                socialProfile.setHobbies(editableFieldValue);
                break;
            case "birthDate":
                socialProfile.setBirthDate(editableFieldValue);
                break;
            case "phone":
                socialProfile.setPhone(editableFieldValue);
                break;
            case "address":
                socialProfile.setAddress((editableFieldValues[0] != null) ? (String) editableFieldValues[0] : null);
                socialProfile.setNumber((editableFieldValues[1] != null) ? (String) editableFieldValues[1] : null);
                socialProfile.setNeighborhood((editableFieldValues[2] != null) ? (String) editableFieldValues[2] : null);
                socialProfile.setZipcode((editableFieldValues[3] != null) ? (String) editableFieldValues[3] : null);
                socialProfile.setCityId((editableFieldValues[4] != null) ? cityJpaController.findCity(Integer.parseInt((String) editableFieldValues[4])) : null);
                socialProfile.setStateId((editableFieldValues[5] != null) ? stateJpaController.findState(Integer.parseInt((String) editableFieldValues[5])) : null);
                socialProfile.setCountryId((editableFieldValues[6] != null) ? countryJpaController.findCountry(Integer.parseInt((String) editableFieldValues[6])) : null);
                break;
            case "education":
                if (editableFieldValues[0] != null && editableFieldValues[2] != null && editableFieldValues[3] != null && editableFieldValues[4] != null) {
                    Educations education = new Educations();
                    EducationsName educationName = educationsNameJpaController.findEducationsNameByName((String) editableFieldValues[3]);
                    if (educationName == null) {
                        educationName = new EducationsName();
                        educationName.setName((String) editableFieldValues[3]);
                        educationName.setScholarityId(scholarityJpaController.findScholarity(Integer.parseInt((String) editableFieldValues[2])));
                        educationsNameJpaController.create(educationName);
                    }
                    education.setNameId(educationName);
                    EducationsLocation educationLocation = educationsLocationJpaController.findEducationsLocationByName((String) editableFieldValues[4]);
                    if (educationLocation == null) {
                        educationLocation = new EducationsLocation();
                        educationLocation.setName((String) editableFieldValues[4]);
                        educationsLocationJpaController.create(educationLocation);
                    }
                    education.setLocationId(educationLocation);
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date initialDate = new Date(format.parse((String) editableFieldValues[0]).getTime());
                    education.setDataBegin(initialDate);
                    if (editableFieldValues[1] != null) {
                        Date finalDate = new Date(format.parse((String) editableFieldValues[1]).getTime());
                        education.setDataEnd(finalDate);
                    }
                    education.setSocialProfile(socialProfile);
                    educationsJpaController.create(education);
                    socialProfile.getEducationsCollection().add(education);
                }
                break;
            case "experience":
                if (editableFieldValues[0] != null && editableFieldValues[2] != null && editableFieldValues[3] != null) {
                    Experiences experience = new Experiences();
                    Occupations experienceOccupation = occupationsJpaController.findOccupationByName((String) editableFieldValues[2]);
                    if (experienceOccupation == null) {
                        experienceOccupation = new Occupations();
                        experienceOccupation.setName((String) editableFieldValues[2]);
                        occupationsJpaController.create(experienceOccupation);
                    }
                    experience.setNameId(experienceOccupation);
                    ExperiencesLocation experienceLocation = experiencesLocationJpaController.findExperiencesLocationByName((String) editableFieldValues[3]);
                    if (experienceLocation == null) {
                        experienceLocation = new ExperiencesLocation();
                        experienceLocation.setName((String) editableFieldValues[3]);
                        experiencesLocationJpaController.create(experienceLocation);
                    }
                    experience.setLocationId(experienceLocation);
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date initialDate = new Date(format.parse((String) editableFieldValues[0]).getTime());
                    experience.setDataBegin(initialDate);
                    if (editableFieldValues[1] != null) {
                        Date finalDate = new Date(format.parse((String) editableFieldValues[1]).getTime());
                        experience.setDataEnd(finalDate);
                    }
                    experience.setSocialProfile(socialProfile);
                    experiencesJpaController.create(experience);
                    socialProfile.getExperiencesCollection().add(experience);
                }
                break;
        }
        socialProfileJpaController.edit(socialProfile);
        editableFieldValue = "";
        editableFieldValues = new Object[7];
        editFieldList.remove(field);
    }
    
    public void deactivateUser() throws IOException, NonexistentEntityException, RollbackFailureException, Exception {
        UserAuthorizationJpaController userAuthorizationJpaController = new UserAuthorizationJpaController();
        userAuthorizationJpaController.deactivateUser(socialProfile.getTokenId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("/admin/view.xhtml");
    }

    public void downloadPDF() throws FileNotFoundException, DocumentException, IOException {
        Document doc = null;
        OutputStream os = null;
        File file = new File("curriculum.pdf");
        try {
            //cria o documento tamanho A4, margens de 2,54cm
            doc = new Document(PageSize.A4, 72, 72, 72, 72);
            //cria a stream de saída
            os = new FileOutputStream(file);
            //associa a stream de saída
            PdfWriter.getInstance(doc, os);
            //abre o documento
            doc.open();
            doc.add(new Chunk(""));
            //corpo do pdf
            Font f1 = new Font(Font.FontFamily.TIMES_ROMAN, 28, Font.BOLD);
            Font f2 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
            Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 14);
            Font f4 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Font f5 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            String occupation = (socialProfile.getOccupationsId() == null) ? "" : socialProfile.getOccupationsId().getName();

            Paragraph p1 = new Paragraph("Curriculum Vitae", f1);
            Paragraph p2 = new Paragraph(socialProfile.getName(), f2);
            Paragraph p3 = new Paragraph(occupation, f3);
            Paragraph p4 = new Paragraph(socialProfile.getUsers().getUsername(), f3);
            Paragraph p5 = new Paragraph("Endereço", f4);
            Paragraph p6 = new Paragraph("Educação", f4);
            Paragraph p7 = new Paragraph("Experiências", f4);

            String addressParagraph1 = "";
            addressParagraph1 += (socialProfile.getAddress() != null) ? socialProfile.getAddress() : "";
            addressParagraph1 += (socialProfile.getAddress() != null && socialProfile.getNumber() != null) ? ", " : "";
            addressParagraph1 += (socialProfile.getNumber() != null) ? socialProfile.getNumber() : "";
            addressParagraph1 += (!"".equals(addressParagraph1) && socialProfile.getNeighborhood() != null) ? " - Bairro " + socialProfile.getNeighborhood() : "";

            String addressParagraph2 = "";
            addressParagraph2 += (socialProfile.getCityId() != null) ? socialProfile.getCityId().getName() + ", " : "";
            addressParagraph2 += (socialProfile.getStateId() != null) ? socialProfile.getStateId().getName() + ", " : "";
            addressParagraph2 += (socialProfile.getCountryId() != null) ? socialProfile.getCountryId().getName() : "";

            Paragraph pEnd1 = new Paragraph(addressParagraph1, f5);
            Paragraph pEnd2 = new Paragraph(addressParagraph2, f5);
            Paragraph pEnd3 = new Paragraph(socialProfile.getZipcode(), f5);

            p1.setAlignment(Element.ALIGN_CENTER);
            p1.setSpacingAfter(20);
            p2.setAlignment(Element.ALIGN_CENTER);
            p3.setAlignment(Element.ALIGN_CENTER);
            p4.setAlignment(Element.ALIGN_CENTER);
            p4.setSpacingAfter(30);
            p5.setSpacingAfter(20);
            p6.setSpacingBefore(10);
            p6.setSpacingAfter(20);
            p7.setSpacingBefore(10);
            p7.setSpacingAfter(20);

            doc.add(p1);
            doc.add(p2);
            doc.add(p3);
            doc.add(p4);
            doc.add(p5);

            doc.add(pEnd1);
            doc.add(pEnd2);
            doc.add(pEnd3);

            if (!socialProfile.getEducationsCollection().isEmpty()) {
                doc.add(p6);
            }

            for (Educations education : socialProfile.getEducationsCollection()) {
                DateFormat data = new SimpleDateFormat("dd/MM/yyyy");
                String dataBegin = data.format(education.getDataBegin().getTime());
                String dataEnd = (education.getDataEnd() != null) ? data.format(education.getDataEnd().getTime()) : "Hoje";
                Paragraph pEdu1 = new Paragraph(education.getNameId().getScholarityId().getDescription(), f5);
                Paragraph pEdu2 = new Paragraph(education.getNameId().getName() + "     " + dataBegin + " - " + dataEnd, f5);
                Paragraph pEdu3 = new Paragraph(education.getLocationId().getName(), f5);
                pEdu3.setSpacingAfter(10);
                doc.add(pEdu1);
                doc.add(pEdu2);
                doc.add(pEdu3);
            }

            if (!socialProfile.getExperiencesCollection().isEmpty()) {
                doc.add(p7);
            }

            for (Experiences experience : socialProfile.getExperiencesCollection()) {
                DateFormat data = new SimpleDateFormat("dd/MM/yyyy");
                String dataBegin = data.format(experience.getDataBegin().getTime());
                String dataEnd = (experience.getDataEnd() != null) ? data.format(experience.getDataEnd().getTime()) : "Hoje";
                Paragraph pExp1 = new Paragraph(experience.getNameId().getName() + "     " + dataBegin + " - " + dataEnd, f5);
                Paragraph pExp2 = new Paragraph(experience.getLocationId().getName(), f5);
                pExp2.setSpacingAfter(10);
                doc.add(pExp1);
                doc.add(pExp2);
            }

        } finally {
            if (doc != null) {
                //fechamento do documento
                doc.close();
            }
            if (os != null) {
                //fechamento da stream de saída
                os.close();
            }
        }
        DownloadService.downloadFile(file, "pdf");
    }

    private void initGlobalVariables() {
        socialProfileJpaController = new SocialProfileJpaController();
        friendsJpaController = new FriendsJpaController();
        educationalObjectJpaController = new EducationalObjectJpaController();
        occupationsJpaController = new OccupationsJpaController();
        interestsJpaController = new InterestsJpaController();
        cityJpaController = new CityJpaController();
        stateJpaController = new StateJpaController();
        countryJpaController = new CountryJpaController();
        scholarityJpaController = new ScholarityJpaController();
        educationsJpaController = new EducationsJpaController();
        educationsNameJpaController = new EducationsNameJpaController();
        educationsLocationJpaController = new EducationsLocationJpaController();
        experiencesJpaController = new ExperiencesJpaController();
        experiencesLocationJpaController = new ExperiencesLocationJpaController();
        docJpaController = new DocJpaController();

        if (socialProfileId == null) {
            socialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
        } else {
            socialProfile = socialProfileJpaController.findSocialProfileBySocialProfileId(socialProfileId);
        }
        educationalObjectList = educationalObjectJpaController.findParticipationInEducationalObjects(socialProfile.getUsers().getUsername());
        docList = docJpaController.findBySocialProfileId(socialProfile.getSocialProfileId(), true);
        editFieldList = new ArrayList<>();
        interestList = interestsJpaController.findInterestsEntities();
        countryList = countryJpaController.findCountryEntities();
        stateList = new ArrayList<>();
        cityList = new ArrayList<>();
        scholarityList = scholarityJpaController.findScholarityEntities();

        cropCoordinates = new Integer[6];
        editableFieldValues = new Object[7];

        mySocialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
        himself = socialProfile.getTokenId().equals(mySocialProfile.getTokenId());
    }

    public SocialProfile getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }

    public Part getUploadedPhoto() {
        return uploadedPhoto;
    }

    public void setUploadedPhoto(Part uploadedPhoto) {
        this.uploadedPhoto = uploadedPhoto;
    }

    public Integer[] getCropCoordinates() {
        return cropCoordinates;
    }

    public void setCropCoordinates(Integer[] cropCoordinates) {
        this.cropCoordinates = cropCoordinates;
    }

    public Boolean getHimself() {
        return himself;
    }

    public void setHimself(Boolean himself) {
        this.himself = himself;
    }

    public List<String> getEditFieldList() {
        return editFieldList;
    }

    public void setEditFieldList(List<String> editFieldList) {
        this.editFieldList = editFieldList;
    }

    public String getEditableFieldValue() {
        return editableFieldValue;
    }

    public void setEditableFieldValue(String editableFieldValue) {
        this.editableFieldValue = editableFieldValue;
    }

    public List<Interests> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interests> interestList) {
        this.interestList = interestList;
    }

    public Object[] getEditableFieldValues() {
        return editableFieldValues;
    }

    public void setEditableFieldValues(Object[] editableFieldValues) {
        this.editableFieldValues = editableFieldValues;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public List<State> getStateList() {
        return stateList;
    }

    public void setStateList(List<State> stateList) {
        this.stateList = stateList;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public List<Scholarity> getScholarityList() {
        return scholarityList;
    }

    public void setScholarityList(List<Scholarity> scholarityList) {
        this.scholarityList = scholarityList;
    }

    public Integer getSocialProfileId() {
        return socialProfileId;
    }

    public void setSocialProfileId(Integer socialProfileId) {
        this.socialProfileId = socialProfileId;
    }

    public SocialProfile getMySocialProfile() {
        return mySocialProfile;
    }

    public void setMySocialProfile(SocialProfile mySocialProfile) {
        this.mySocialProfile = mySocialProfile;
    }

    public List<EducationalObject> getEducationalObjectList() {
        return educationalObjectList;
    }

    public void setEducationalObjectList(List<EducationalObject> educationalObjectList) {
        this.educationalObjectList = educationalObjectList;
    }

    public List<Doc> getDocList() {
        return docList;
    }

    public void setDocList(List<Doc> docList) {
        this.docList = docList;
    }
}
