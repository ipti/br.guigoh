/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UsersJpaController;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.UploadService;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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

    private SocialProfileJpaController socialProfileJpaController;

    private Part uploadedPhoto;
    private Integer[] cropCoordinates;

    public void init() {
        initGlobalVariables();
    }

    public void uploadPhoto() throws IOException {
        String basePath = System.getProperty("user.home") + File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn" + File.separator + "guigoh" + File.separator + "users" + File.separator + socialProfile.getSocialProfileId() + File.separator + "photo" + File.separator;
        UploadService.uploadFile(uploadedPhoto, basePath, cropCoordinates);
        //http://cdn.guigoh.com/guigoh/users/" + socialProfile.getSocialProfileId() + "." + type
    }

    private void initGlobalVariables() {
        socialProfileJpaController = new SocialProfileJpaController();

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
            socialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
        } else {
            socialProfile = socialProfileJpaController.findSocialProfileBySocialProfileId(Integer.valueOf(request.getParameter("id")));
        }
        
        cropCoordinates = new Integer[6];
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

}
//    public void downloadPDF() throws FileNotFoundException, DocumentException, IOException {
//        Document doc = null;
//        OutputStream os = null;
//        File file = new File("curriculum.pdf");
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        try {
//            //cria o documento tamanho A4, margens de 2,54cm
//            doc = new Document(PageSize.A4, 72, 72, 72, 72);
//            //cria a stream de saída
//            os = new FileOutputStream(file);
//            //associa a stream de saída
//            PdfWriter.getInstance(doc, os);
//            //abre o documento
//            doc.open();
//            //corpo do pdf
//            Font f1 = new Font(Font.FontFamily.TIMES_ROMAN, 28, Font.BOLD);
//            Font f2 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
//            Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 14);
//            Font f4 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
//            Font f5 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
//
//            Paragraph p1 = new Paragraph("Curriculum Vitae", f1);
//            Paragraph p2 = new Paragraph(socialProfile.getName(), f2);
//            Paragraph p3 = new Paragraph(socialProfile.getOccupationsId().getName(), f3);
//            Paragraph p4 = new Paragraph(socialProfile.getUsers().getUsername(), f3);
//            Paragraph p5 = new Paragraph("Endereço", f4);
//            Paragraph p6 = new Paragraph("Educação", f4);
//            Paragraph p7 = new Paragraph("Experiências", f4);
//
//            Paragraph pEnd1 = new Paragraph(socialProfile.getAddress() + ", Nº " + socialProfile.getNumber() + " - " + "Bairro " + socialProfile.getNeighborhood(), f5);
//            Paragraph pEnd2 = new Paragraph(socialProfile.getCityId().getName() + ", " + socialProfile.getStateId().getName() + ", " + socialProfile.getCountryId().getName(), f5);
//            Paragraph pEnd3 = new Paragraph("CEP: " + socialProfile.getZipcode(), f5);
//
//            p1.setAlignment(Element.ALIGN_CENTER);
//            p1.setSpacingAfter(20);
//            p2.setAlignment(Element.ALIGN_CENTER);
//            p3.setAlignment(Element.ALIGN_CENTER);
//            p4.setAlignment(Element.ALIGN_CENTER);
//            p4.setSpacingAfter(30);
//            p5.setSpacingAfter(20);
//            p6.setSpacingBefore(10);
//            p6.setSpacingAfter(20);
//            p7.setSpacingBefore(10);
//            p7.setSpacingAfter(20);
//
//            doc.add(p1);
//            doc.add(p2);
//            doc.add(p3);
//            doc.add(p4);
//            doc.add(p5);
//
//            doc.add(pEnd1);
//            doc.add(pEnd2);
//            doc.add(pEnd3);
//
//            doc.add(p6);
//
//            for (Educations education : educationsList) {
//                DateFormat data = new SimpleDateFormat("dd/MM/yyyy");
//                String dataBegin = data.format(education.getDataBegin().getTime());
//                String dataEnd = data.format(education.getDataEnd().getTime());
//                Paragraph pEdu1 = new Paragraph(education.getNameId().getName() + "     " + dataBegin + " - " + dataEnd, f5);
//                Paragraph pEdu2 = new Paragraph(education.getLocationId().getName() + " - " + education.getStateId().getName() + ", " + education.getCountryId().getName(), f5);
//                pEdu2.setSpacingAfter(10);
//                doc.add(pEdu1);
//                doc.add(pEdu2);
//            }
//
//            doc.add(p7);
//
//            for (Experiences experience : experiencesList) {
//                DateFormat data = new SimpleDateFormat("dd/MM/yyyy");
//                String dataBegin = data.format(experience.getDataBegin().getTime());
//                String dataEnd = data.format(experience.getDataEnd().getTime());
//                Paragraph pExp1 = new Paragraph(experience.getNameId().getName() + "     " + dataBegin + " - " + dataEnd, f5);
//                Paragraph pExp2 = new Paragraph(experience.getLocationId().getName() + " - " + experience.getStateId().getName() + ", " + experience.getCountryId().getName(), f5);
//                pExp2.setSpacingAfter(10);
//                doc.add(pExp1);
//                doc.add(pExp2);
//            }
//
//        } finally {
//            if (doc != null) {
//                //fechamento do documento
//                doc.close();
//            }
//            if (os != null) {
//                //fechamento da stream de saída
//                os.close();
//            }
//        }
//        DownloadService.downloadFile(file, "pdf");
//    }
