/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.Educations;
import br.org.ipti.guigoh.model.entity.Experiences;
import br.org.ipti.guigoh.model.entity.Friends;
import br.org.ipti.guigoh.model.entity.Occupations;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.FriendsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.OccupationsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
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
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 *
 * @author IPTI
 */
@ViewScoped
@Named
public class ProfileViewBean implements Serializable {

    private SocialProfile socialProfile;

    private List<EducationalObject> educationalObjectList;
    private List<String> editFieldList;

    private SocialProfileJpaController socialProfileJpaController;
    private FriendsJpaController friendsJpaController;
    private EducationalObjectJpaController educationalObjectJpaController;
    private OccupationsJpaController occupationsJpaController;

    private Part uploadedPhoto;

    private Integer[] cropCoordinates;

    private Boolean himself;
    private String editableFieldValue;

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
        }
        editFieldList.add(field);
    }

    public void editField(String field) throws Exception {
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
                socialProfileJpaController.edit(socialProfile);
                break;
        }
        editableFieldValue = "";
        editFieldList.remove(field);
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

            Paragraph pEnd1 = new Paragraph(addressParagraph1, f5);
            Paragraph pEnd2 = new Paragraph(socialProfile.getCityId().getName() + ", " + socialProfile.getStateId().getName() + ", " + socialProfile.getCountryId().getName(), f5);
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
                String dataEnd = data.format(education.getDataEnd().getTime());
                Paragraph pEdu1 = new Paragraph(education.getNameId().getName() + "     " + dataBegin + " - " + dataEnd, f5);
                Paragraph pEdu2 = new Paragraph(education.getLocationId().getName(), f5);
                pEdu2.setSpacingAfter(10);
                doc.add(pEdu1);
                doc.add(pEdu2);
            }

            if (!socialProfile.getExperiencesCollection().isEmpty()) {
                doc.add(p7);
            }

            for (Experiences experience : socialProfile.getExperiencesCollection()) {
                DateFormat data = new SimpleDateFormat("dd/MM/yyyy");
                String dataBegin = data.format(experience.getDataBegin().getTime());
                String dataEnd = data.format(experience.getDataEnd().getTime());
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

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
            socialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
        } else {
            socialProfile = socialProfileJpaController.findSocialProfileBySocialProfileId(Integer.valueOf(request.getParameter("id")));
        }
        educationalObjectList = educationalObjectJpaController.findEducationalObjectsBySocialProfileId(socialProfile.getSocialProfileId());
        editFieldList = new ArrayList<>();

        cropCoordinates = new Integer[6];

        himself = socialProfile.getTokenId().equals(CookieService.getCookie("token"));
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

    public List<EducationalObject> getEducationalObjectList() {
        return educationalObjectList;
    }

    public void setEducationalObjectList(List<EducationalObject> educationalObjectList) {
        this.educationalObjectList = educationalObjectList;
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

}
