/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.EducationsBO;
import com.guigoh.primata.bo.ExperiencesBO;
import com.guigoh.primata.bo.FriendsBO;
import com.guigoh.primata.bo.InterestsBO;
import com.guigoh.primata.bo.InterestsTypeBO;
import com.guigoh.primata.bo.OccupationsBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.util.CookieService;
import com.guigoh.primata.bo.util.DownloadService;
import com.guigoh.primata.bo.util.translator.Translator;
import com.guigoh.primata.dao.exceptions.PreexistingEntityException;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.Availability;
import com.guigoh.primata.entity.Educations;
import com.guigoh.primata.entity.Experiences;
import com.guigoh.primata.entity.Friends;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.InterestsType;
import com.guigoh.primata.entity.Occupations;
import com.guigoh.primata.entity.OccupationsType;
import com.guigoh.primata.entity.Scholarity;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Users;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author IPTI
 */
@SessionScoped
@ManagedBean(name = "profileBean")
public class ProfileBean implements Serializable {

    private Users user;
    private SocialProfile socialProfile;
    private List<Interests> interestsList;
    private List<Interests> themesList;
    private List<Interests> moviesList;
    private List<Interests> musicsList;
    private List<Interests> booksList;
    private List<Interests> hobbiesList;
    private List<Interests> sportsList;
    private List<InterestsType> interestsTypeList;
    private Integer id;
    private List<Experiences> experiencesList;
    private List<Educations> educationsList;
    private String friendStatus;
    private List<Friends> friendList;
    private Boolean recommendPanel;
    private String recommenderMessage;
    private String receiver;
    private String friendInputSearch;
    private Boolean profileEdit1;
    private Boolean profileEdit2;
    private Boolean profileEdit3;
    private Boolean profileEdit4;
    private Boolean curriculumEdit1;
    private Boolean curriculumEdit2;
    private Boolean curriculumEdit3;
    private Boolean curriculumEdit4;
    private Boolean edit;
    private WizardProfileBean wizardProfileBean;
    private Translator trans;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            user = new Users();
            wizardProfileBean = new WizardProfileBean();
            wizardProfileBean.init();
            socialProfile = new SocialProfile();
            trans = new Translator();
            trans.setLocale(CookieService.getCookie("locale"));
            interestsList = new ArrayList<Interests>();
            interestsTypeList = new ArrayList<InterestsType>();
            themesList = new ArrayList<Interests>();
            booksList = new ArrayList<Interests>();
            musicsList = new ArrayList<Interests>();
            moviesList = new ArrayList<Interests>();
            hobbiesList = new ArrayList<Interests>();
            sportsList = new ArrayList<Interests>();
            experiencesList = new ArrayList<Experiences>();
            educationsList = new ArrayList<Educations>();
            recommendPanel = false;
            recommenderMessage = "";
            friendList = new ArrayList<Friends>();
            friendInputSearch = "";
            receiver = "";
            profileEdit1 = false;
            profileEdit2 = false;
            profileEdit3 = false;
            profileEdit4 = false;
            curriculumEdit1 = false;
            curriculumEdit2 = false;
            curriculumEdit3 = false;
            curriculumEdit4 = false;
            edit = false;
            loadProfile(id);
            if (id != null) {
                checkFriendStatus();
            }

        }
    }

    private void loadProfile(Integer id) {
        loadUserCookie();
        if (id == null) {
            loadSocialProfile();
            loadInterests();
            loadEducations(socialProfile.getTokenId());
            loadExperiencies(socialProfile.getTokenId());
        } else {
            loadUsers(id);
            loadInterests();
            loadEducations(socialProfile.getTokenId());
            loadExperiencies(socialProfile.getTokenId());
        }
    }

    private void loadUsers(Integer id) {
        SocialProfileBO socialProfileBO = new SocialProfileBO();
        socialProfile = socialProfileBO.findSocialProfileBySocialProfileId(id);
    }

    private void loadUserCookie() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equalsIgnoreCase("user")) {
                    user.setUsername(cookie.getValue());
                } else if (cookie.getName().trim().equalsIgnoreCase("token")) {
                    user.setToken(cookie.getValue());
                }
            }
        }
    }

    private void loadSocialProfile() {
        SocialProfileBO socialProfileBO = new SocialProfileBO();

        socialProfile = socialProfileBO.findSocialProfile(user.getToken());
        if (socialProfile.getOccupationsId() == null) {
            Occupations occupations = new Occupations();
            OccupationsType occupationsType = new OccupationsType();
            occupationsType.setId(0);
            occupations.setOccupationsTypeId(occupationsType);
            socialProfile.setOccupationsId(occupations);
        }
        if (socialProfile.getAvailabilityId() == null) {
            Availability availability = new Availability();
            availability.setId(0);
            socialProfile.setAvailabilityId(availability);
        }
        if (socialProfile.getScholarityId() == null) {
            Scholarity scholarity = new Scholarity();
            scholarity.setId(0);
            socialProfile.setScholarityId(scholarity);
        }

    }

    private void loadInterests() {
        if (socialProfile != null) {
            InterestsBO interestsBO = new InterestsBO();
            interestsList = interestsBO.findInterests(socialProfile.getSocialProfileId());
            InterestsTypeBO interestsTypeBO = new InterestsTypeBO();
            interestsTypeList = interestsTypeBO.findInterestsType();

            musicsList.clear();
            moviesList.clear();
            booksList.clear();
            sportsList.clear();
            hobbiesList.clear();
            themesList.clear();

            for (Interests interests : interestsList) {
                if (findInterestTypeById(interests.getTypeId().getId()).equals("CI")) {
                    if (interests.getTypeId().getName().equals("Musics")) {
                        musicsList.add(interests);
                    }
                    if (interests.getTypeId().getName().equals("Movies")) {
                        moviesList.add(interests);
                    }
                    if (interests.getTypeId().getName().equals("Books")) {
                        booksList.add(interests);
                    }
                }
                if (findInterestTypeById(interests.getTypeId().getId()).equals("SH")) {
                    if (interests.getTypeId().getName().equals("Sports")) {
                        sportsList.add(interests);
                    }
                    if (interests.getTypeId().getName().equals("Hobbies")) {
                        hobbiesList.add(interests);
                    }
                }
                if (findInterestTypeById(interests.getTypeId().getId()).equals("TH")) {
                    themesList.add(interests);
                }
            }
            musicsList = wizardProfileBean.completeInterests(musicsList);
            moviesList = wizardProfileBean.completeInterests(moviesList);
            booksList = wizardProfileBean.completeInterests(booksList);
            sportsList = wizardProfileBean.completeInterests(sportsList);
            hobbiesList = wizardProfileBean.completeInterests(hobbiesList);
        }
    }

    private String findInterestTypeById(Integer id) {
        for (InterestsType interestsType : interestsTypeList) {
            if (interestsType.getId() == id) {
                return interestsType.getType();
            }
        }
        return "";
    }

    public void removeExperience(Experiences exp) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            experiencesList.remove(exp);
            ExperiencesBO experiencesBO = new ExperiencesBO();
            experiencesBO.removeExperience(exp);            
            String message = trans.getWord("Experiência removida com sucesso!");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeEducation(Educations edu) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            educationsList.remove(edu);
            EducationsBO educationsBO = new EducationsBO();
            educationsBO.removeEducation(edu);
            String message = trans.getWord("Educação removida com sucesso!");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadExperiencies(String token_id) {
        ExperiencesBO experiencesBO = new ExperiencesBO();
        experiencesList = experiencesBO.findExperiencesByTokenId(token_id);
    }

    private void loadEducations(String token_id) {
        EducationsBO educationsBO = new EducationsBO();
        educationsList = educationsBO.findEducationsByTokenId(token_id);
    }

    public void addFriend() throws PreexistingEntityException, RollbackFailureException, Exception {
        FriendsBO friendsBO = new FriendsBO();
        friendStatus = "Invited";
        friendsBO.addFriend(user, id);
    }

    public void searchFriendEvent() {
        try {
            FriendsBO friendBO = new FriendsBO();
            friendList = new ArrayList<Friends>();
            friendList = friendBO.loadFriendSearchList(user.getToken(), friendInputSearch);
            organizeFriendList(friendList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void organizeFriendList(List<Friends> list) {
        for (Friends friend : list) {
            if (user.getToken().equals(friend.getTokenFriend2().getToken())) {
                /*
                 * renomear user
                 */
                Users user = friend.getTokenFriend1();
                friend.setTokenFriend1(friend.getTokenFriend2());
                friend.setTokenFriend2(user);
            }
        }
    }

    public void recommendFriend() throws Exception {
        FriendsBO friendsBO = new FriendsBO();
        friendsBO.recommendFriend(user, id, receiver, recommenderMessage);
    }

    private void checkFriendStatus() {
        FriendsBO friendsBO = new FriendsBO();
        Friends friend = friendsBO.findFriends(user, id);
        if (friend == null) {
            friendStatus = "Uninvited";
        } else if (friend.getTokenFriend1().equals(friend.getTokenFriend2())) {
            friendStatus = "Myself";
        } else {
            if (friend.getStatus().equals("PE")) {
                friendStatus = "Invited";
            } else {
                friendStatus = "Accepted";
            }
        }

    }

    public void downloadPDF() throws FileNotFoundException, DocumentException, IOException {
        Document doc = null;
        OutputStream os = null;
        File file = new File("curriculum.pdf");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            //cria o documento tamanho A4, margens de 2,54cm
            doc = new Document(PageSize.A4, 72, 72, 72, 72);
            //cria a stream de saída
            os = new FileOutputStream(file);
            //associa a stream de saída
            PdfWriter.getInstance(doc, os);
            //abre o documento
            doc.open();
            //corpo do pdf
            Font f1 = new Font(Font.FontFamily.TIMES_ROMAN, 28, Font.BOLD);
            Font f2 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
            Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 14);
            Font f4 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Font f5 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            Paragraph p1 = new Paragraph("Curriculum Vitae", f1);
            Paragraph p2 = new Paragraph(socialProfile.getName(), f2);
            Paragraph p3 = new Paragraph(socialProfile.getOccupationsId().getName(), f3);
            Paragraph p4 = new Paragraph(socialProfile.getUsers().getUsername(), f3);
            Paragraph p5 = new Paragraph("Endereço", f4);
            Paragraph p6 = new Paragraph("Educação", f4);
            Paragraph p7 = new Paragraph("Experiências", f4);

            Paragraph pEnd1 = new Paragraph(socialProfile.getAddress() + ", Nº " + socialProfile.getNumber() + " - " + "Bairro " + socialProfile.getNeighborhood(), f5);
            Paragraph pEnd2 = new Paragraph(socialProfile.getCityId().getName() + ", " + socialProfile.getStateId().getName() + ", " + socialProfile.getCountryId().getName(), f5);
            Paragraph pEnd3 = new Paragraph("CEP: " + socialProfile.getZipcode(), f5);

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

            doc.add(p6);

            for (Educations education : educationsList) {
                DateFormat data = new SimpleDateFormat("dd/MM/yyyy");
                String dataBegin = data.format(education.getDataBegin().getTime());
                String dataEnd = data.format(education.getDataEnd().getTime());
                Paragraph pEdu1 = new Paragraph(education.getNameId().getName() + "     " + dataBegin + " - " + dataEnd, f5);
                Paragraph pEdu2 = new Paragraph(education.getLocationId().getName() + " - " + education.getStateId().getName() + ", " + education.getCountryId().getName(), f5);
                pEdu2.setSpacingAfter(10);
                doc.add(pEdu1);
                doc.add(pEdu2);
            }

            doc.add(p7);

            for (Experiences experience : experiencesList) {
                DateFormat data = new SimpleDateFormat("dd/MM/yyyy");
                String dataBegin = data.format(experience.getDataBegin().getTime());
                String dataEnd = data.format(experience.getDataEnd().getTime());
                Paragraph pExp1 = new Paragraph(experience.getNameId().getName() + "     " + dataBegin + " - " + dataEnd, f5);
                Paragraph pExp2 = new Paragraph(experience.getLocationId().getName() + " - " + experience.getStateId().getName() + ", " + experience.getCountryId().getName(), f5);
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
        DownloadService.downloadFile(file, "pdf", facesContext);
    }

    public void execute() {
    }

    public void saveSocialProfile() {
        try {
            OccupationsBO occupationsBO = new OccupationsBO();
            Occupations occupationst = occupationsBO.findOccupationsByNameByType(socialProfile.getOccupationsId());
            if (occupationst.getId() == null) {
                occupationsBO.createInsert(socialProfile.getOccupationsId());
                occupationst = occupationsBO.findOccupationsByNameByType(socialProfile.getOccupationsId());
            }
            if (occupationst.getName() == null) {
                socialProfile.setOccupationsId(null);
            } else {
                socialProfile.setOccupationsId(occupationst);
            }
            if (socialProfile.getAvailabilityId().getId() == 0) {
                socialProfile.setAvailabilityId(null);
            }
            SocialProfileBO socialProfileBO = new SocialProfileBO();
            socialProfileBO.edit(socialProfile);

            if (socialProfile.getAvailabilityId() == null) {
                Availability availability = new Availability();
                availability.setId(0);
                socialProfile.setAvailabilityId(availability);
            }
            if (socialProfile.getScholarityId() == null) {
                Scholarity scholarity = new Scholarity();
                scholarity.setId(0);
                socialProfile.setScholarityId(scholarity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveThemes() {
        try {
            InterestsBO interestsBO = new InterestsBO();
            interestsBO.destroyInterestsBySocialProfileInterestsType(socialProfile, "Themes");
            interestsBO.createInterestsBySocialProfileByIds(wizardProfileBean.getMultiThemeList(), socialProfile);
            loadInterests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCulture() {
        try {
            InterestsBO interestsBO = new InterestsBO();
            interestsBO.destroyInterestsBySocialProfileInterestsType(socialProfile, "Books");
            interestsBO.destroyInterestsBySocialProfileInterestsType(socialProfile, "Musics");
            interestsBO.destroyInterestsBySocialProfileInterestsType(socialProfile, "Movies");
            checkInterestsList(booksList, "Books");
            checkInterestsList(musicsList, "Musics");
            checkInterestsList(moviesList, "Movies");
            interestsBO.createInterestsBySocialProfileByInterest(booksList, socialProfile);
            interestsBO.createInterestsBySocialProfileByInterest(moviesList, socialProfile);
            interestsBO.createInterestsBySocialProfileByInterest(musicsList, socialProfile);
            loadInterests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkInterestsList(List<Interests> interestsList, String type) {
        InterestsBO interestsBO = new InterestsBO();
        InterestsTypeBO interestsTypeBO = new InterestsTypeBO();
        InterestsType interestsType = interestsTypeBO.findInterestsTypeByName(type);
        Interests interestsTemp;        
        for (Interests interests : interestsList) {
            if (interests != null) {
                interestsTemp = interestsBO.findInterestsByInterestsName(interests.getName());
                if (interestsTemp.getId() == null) {

                    interests.setId(0);
                    interests.setTypeId(interestsType);
                    interestsBO.create(interests);

                }
            }
        }
    }

    public void saveSportsHobbies() {
        try {
            InterestsBO interestsBO = new InterestsBO();
            interestsBO.destroyInterestsBySocialProfileInterestsType(socialProfile, "Sports");
            interestsBO.destroyInterestsBySocialProfileInterestsType(socialProfile, "Hobbies");
            checkInterestsList(sportsList, "Sports");
            checkInterestsList(hobbiesList, "Hobbies");
            interestsBO.createInterestsBySocialProfileByInterest(sportsList, socialProfile);
            interestsBO.createInterestsBySocialProfileByInterest(hobbiesList, socialProfile);
            loadInterests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAddress() {
        try {
            socialProfile.getCountryId().setId(wizardProfileBean.getCountryId());
            socialProfile.getStateId().setId(wizardProfileBean.getStateId());
            socialProfile.getCityId().setId(wizardProfileBean.getCityId());
            saveSocialProfile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addEducations() {
        try {
            wizardProfileBean.addEducations();
            loadEducations(socialProfile.getTokenId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addExperiences() {
        try {
            wizardProfileBean.addExperiences();
            loadExperiencies(socialProfile.getTokenId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openRecommendPanel() {
        recommendPanel = true;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public SocialProfile getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }

    public List<Interests> getInterestsList() {
        return interestsList;
    }

    public void setInterestsList(List<Interests> interestsList) {
        this.interestsList = interestsList;
    }

    public List<Interests> getThemesList() {
        return themesList;
    }

    public void setThemesList(List<Interests> themesList) {
        this.themesList = themesList;
    }

    public List<Interests> getSportsList() {
        return sportsList;
    }

    public void setSportsList(List<Interests> sportsList) {
        this.sportsList = sportsList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Experiences> getExperiencesList() {
        return experiencesList;
    }

    public void setExperiencesList(List<Experiences> experiencesList) {
        this.experiencesList = experiencesList;
    }

    public List<Educations> getEducationsList() {
        return educationsList;
    }

    public void setEducationsList(List<Educations> educationsList) {
        this.educationsList = educationsList;
    }

    public List<InterestsType> getInterestsTypeList() {
        return interestsTypeList;
    }

    public void setInterestsTypeList(List<InterestsType> interestsTypeList) {
        this.interestsTypeList = interestsTypeList;
    }

    public List<Interests> getMoviesList() {
        return moviesList;
    }

    public void setMoviesList(List<Interests> moviesList) {
        this.moviesList = moviesList;
    }

    public List<Interests> getMusicsList() {
        return musicsList;
    }

    public void setMusicsList(List<Interests> musicsList) {
        this.musicsList = musicsList;
    }

    public List<Interests> getBooksList() {
        return booksList;
    }

    public void setBooksList(List<Interests> booksList) {
        this.booksList = booksList;
    }

    public List<Interests> getHobbiesList() {
        return hobbiesList;
    }

    public void setHobbiesList(List<Interests> hobbiesList) {
        this.hobbiesList = hobbiesList;
    }

    public String getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        this.friendStatus = friendStatus;
    }

    public Boolean getRecommendPanel() {
        return recommendPanel;
    }

    public void setRecommendPanel(Boolean recommendPanel) {
        this.recommendPanel = recommendPanel;
    }

    public String getRecommenderMessage() {
        return recommenderMessage;
    }

    public void setRecommenderMessage(String recommenderMessage) {
        this.recommenderMessage = recommenderMessage;
    }

    public String getFriendInputSearch() {
        return friendInputSearch;
    }

    public void setFriendInputSearch(String friendInputSearch) {
        this.friendInputSearch = friendInputSearch;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public List<Friends> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friends> friendList) {
        this.friendList = friendList;
    }

    public Boolean getProfileEdit1() {
        return profileEdit1;
    }

    public void setProfileEdit1(Boolean profileEdit1) {
        this.profileEdit1 = profileEdit1;
    }

    public Boolean getProfileEdit2() {
        return profileEdit2;
    }

    public void setProfileEdit2(Boolean profileEdit2) {
        this.profileEdit2 = profileEdit2;
    }

    public Boolean getProfileEdit3() {
        return profileEdit3;
    }

    public void setProfileEdit3(Boolean profileEdit3) {
        this.profileEdit3 = profileEdit3;
    }

    public Boolean getProfileEdit4() {
        return profileEdit4;
    }

    public void setProfileEdit4(Boolean profileEdit4) {
        this.profileEdit4 = profileEdit4;
    }

    public Boolean getCurriculumEdit1() {
        return curriculumEdit1;
    }

    public void setCurriculumEdit1(Boolean curriculumEdit1) {
        this.curriculumEdit1 = curriculumEdit1;
    }

    public Boolean getCurriculumEdit2() {
        return curriculumEdit2;
    }

    public void setCurriculumEdit2(Boolean curriculumEdit2) {
        this.curriculumEdit2 = curriculumEdit2;
    }

    public Boolean getCurriculumEdit3() {
        return curriculumEdit3;
    }

    public void setCurriculumEdit3(Boolean curriculumEdit3) {
        this.curriculumEdit3 = curriculumEdit3;
    }

    public Boolean getCurriculumEdit4() {
        return curriculumEdit4;
    }

    public void setCurriculumEdit4(Boolean curriculumEdit4) {
        this.curriculumEdit4 = curriculumEdit4;
    }

    public WizardProfileBean getWizardProfileBean() {
        return wizardProfileBean;
    }

    public void setWizardProfileBean(WizardProfileBean wizardProfileBean) {
        this.wizardProfileBean = wizardProfileBean;
    }
}
