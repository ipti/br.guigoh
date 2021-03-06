/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.Author;
import br.org.ipti.guigoh.model.entity.AuthorRole;
import br.org.ipti.guigoh.model.entity.EducationalObject;
import br.org.ipti.guigoh.model.entity.EducationalObjectMedia;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Tags;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.AuthorJpaController;
import br.org.ipti.guigoh.model.jpa.controller.AuthorRoleJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectJpaController;
import br.org.ipti.guigoh.model.jpa.controller.EducationalObjectMediaJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.TagsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UsersJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UtilJpaController;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.MailService;
import br.org.ipti.guigoh.util.UploadService;
import br.org.ipti.guigoh.util.translator.Translator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author ipti004
 */
@ViewScoped
@Named
public class EducationalObjectPublishBean implements Serializable {

    private EducationalObject educationalObject;

    private List<Interests> interestList;
    private List<AuthorRole> authorRoleList;
    private List<Tags> tagList;
    private List<Author> authorList;

    private Author author;
    private Translator trans;

    private String tag;
    private String imageSrc;
    private Integer[] cropCoordinates;

    private transient File imageSampleFile;
    private transient Part imageFile;
    private transient Part mediaFile1;
    private transient Part mediaFile2;
    private transient Part mediaFile3;

    private InterestsJpaController interestsJpaController;
    private AuthorRoleJpaController authorRoleJpaController;

    public void init() throws IOException {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
            SocialProfile mySocialProfile = socialProfileJpaController.findSocialProfile(CookieService.getCookie("token"));
            if (mySocialProfile.getRoleId() != null && mySocialProfile.getRoleId().getName().equals("Visitante")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../home.xhtml");
            }
            initGlobalVariables();
        }
    }

    public SocialProfile getSocialProfileByEmail(String email) {
        UsersJpaController usersJpaController = new UsersJpaController();
        Users user = usersJpaController.findUsers(email);
        if (user != null) {
            return user.getSocialProfile();
        } else {
            return null;
        }
    }

    public void addTag() {
        if (tagList.size() < 7) {
            boolean exists = false;
            for (Tags t : tagList) {
                if (t.getName().equals(tag)) {
                    exists = true;
                }
            }
            if (!exists) {
                Tags tag = new Tags();
                tag.setName(this.tag);
                tagList.add(tag);
                this.tag = "";
            }
        }
    }

    public void removeTag(int index) {
        tagList.remove(index);
    }

    public void addAuthor() throws UnsupportedEncodingException {
        if (authorList.size() < 10) {
            boolean exists = false;
            for (Author a : authorList) {
                if (a.getName().equals(this.author.getName())
                        && a.getEmail().equals(this.author.getEmail())) {
                    exists = true;
                }
            }
            if (!exists) {
                authorList.add(author);
                author = new Author();
            }
        }
    }

    public void removeAuthor(Author author) {
        authorList.remove(author);
    }

    public void cropImage() throws IOException {
        if (imageFile.getSize() != 0) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            imageSampleFile = new File("imageSampleFile");
            inputStream = imageFile.getInputStream();
            outputStream = new FileOutputStream(imageSampleFile);
            try {
                inputStream = imageFile.getInputStream();
                outputStream = new FileOutputStream(imageSampleFile);
                int read = 0;
                final byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                String[] fileSplit = imageFile.getSubmittedFileName().split("\\.");
                String fileType = fileSplit[fileSplit.length - 1];
                BufferedImage image = ImageIO.read(imageSampleFile);
                Integer x = cropCoordinates[0];
                Integer y = cropCoordinates[1];
                Integer w = cropCoordinates[2];
                Integer h = cropCoordinates[3];
                BufferedImage out = image.getSubimage(x, y, w, h);
                ImageIO.write(out, fileType, imageSampleFile);

                StringBuilder sb = new StringBuilder();
                sb.append("data:image/*;base64,".concat(StringUtils.newStringUtf8(Base64.encodeBase64(FileUtils.readFileToByteArray(imageSampleFile), false))));
                imageSrc = sb.toString();
            } catch (IOException e) {
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
    }

    public void submit() throws IOException, Exception {
        imageSampleFile.delete();
        EducationalObjectJpaController educationalObjectJpaController = new EducationalObjectJpaController();
        UtilJpaController utilJpaController = new UtilJpaController();
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        educationalObject.setSocialProfileId(socialProfileJpaController.findSocialProfile(CookieService.getCookie("token")));
        educationalObject.setStatus("PE");
        educationalObject.setDate(utilJpaController.getTimestampServerTime());
        educationalObject.setViews(BigInteger.ZERO);
        educationalObjectJpaController.create(educationalObject);
        String imagePath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn"
                + File.separator + "guigoh" + File.separator + "educationalobjects" + File.separator + educationalObject.getId()
                + File.separator + "image" + File.separator;
        UploadService.uploadFile(imageFile, imagePath, cropCoordinates);
        educationalObject.setImage("http://cdn.guigoh.com/guigoh/educationalobjects/" + educationalObject.getId() + "/image/" + imageFile.getSubmittedFileName());
        TagsJpaController tagsJpaController = new TagsJpaController();
        for (Tags tag : tagList) {
            Tags tagDB = tagsJpaController.findTagByName(tag.getName());
            if (tagDB == null) {
                tagsJpaController.create(tag);
                educationalObject.getTagsCollection().add(tag);
            } else {
                educationalObject.getTagsCollection().add(tagDB);
            }
        }
        AuthorJpaController authorJpaController = new AuthorJpaController();
        for (Author authorOE : authorList) {
            authorJpaController.create(authorOE);
            educationalObject.getAuthorCollection().add(authorOE);
        }
        educationalObjectJpaController.edit(educationalObject);
        if (mediaFile1 != null) {
            submitFile(mediaFile1);
        }
        if (mediaFile2 != null) {
            submitFile(mediaFile2);
        }
        if (mediaFile3 != null) {
            submitFile(mediaFile3);
        }
        if (!educationalObject.getThemeId().getUsersCollection().isEmpty()) {
            String[] emails = new String[educationalObject.getThemeId().getUsersCollection().size()];
            int i = 0;
            for (Users user : educationalObject.getThemeId().getUsersCollection()) {
                emails[i] = user.getUsername();
                i++;
            }
            String mailSubject = "Novo Objeto Educacional";
            String mailText = "Um novo objeto educacional foi publicado no Arte com Ciência.";
            trans.setLocale(educationalObject.getSocialProfileId().getLanguageId().getAcronym());
            mailSubject = trans.getWord(mailSubject);
            mailText = trans.getWord(mailText);
            mailText += "\n\n" + educationalObject.getName() + "\n" + educationalObject.getSocialProfileId().getName();
            mailText += (educationalObject.getSocialProfileId().getSubnetworkId() != null ? "\n" + educationalObject.getSocialProfileId().getSubnetworkId().getDescription() : "");
            mailText += "\n" + educationalObject.getThemeId().getName();
            MailService.sendMail(mailText, mailSubject, emails);
            trans.setLocale(CookieService.getCookie("locale"));
        }
    }

    private void submitFile(Part media) throws IOException, Exception {
        String mediaPath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn"
                + File.separator + "guigoh" + File.separator + "educationalobjects" + File.separator + educationalObject.getId()
                + File.separator + "media" + File.separator;
        boolean success = UploadService.uploadFile(media, mediaPath, null);
        if (success) {
            EducationalObjectMedia educationalObjectMedia = new EducationalObjectMedia();
            educationalObjectMedia.setEducationalObjectId(educationalObject);
            educationalObjectMedia.setSize(media.getSize());
            String[] fileSplit = media.getSubmittedFileName().split("\\.");
            educationalObjectMedia.setName(media.getSubmittedFileName().replace("." + fileSplit[fileSplit.length - 1], ""));
            educationalObjectMedia.setType(fileSplit[fileSplit.length - 1]);
            educationalObjectMedia.setMedia("http://cdn.guigoh.com/guigoh/educationalobjects/" + educationalObject.getId() + "/media/" + media.getSubmittedFileName());
            EducationalObjectMediaJpaController educationalObjectMediaJpaController = new EducationalObjectMediaJpaController();
            educationalObjectMediaJpaController.create(educationalObjectMedia);
        }
    }

    private void initGlobalVariables() {
        interestsJpaController = new InterestsJpaController();
        authorRoleJpaController = new AuthorRoleJpaController();

        interestList = interestsJpaController.findInterestsEntities();
        authorRoleList = authorRoleJpaController.findAuthorRoleEntities();
        tagList = new ArrayList<>();
        authorList = new ArrayList<>();

        educationalObject = new EducationalObject();
        author = new Author();
        trans = new Translator();

        trans.setLocale(CookieService.getCookie("locale"));

        cropCoordinates = new Integer[6];
    }

    public EducationalObject getEducationalObject() {
        return educationalObject;
    }

    public void setEducationalObject(EducationalObject educationalObject) {
        this.educationalObject = educationalObject;
    }

    public List<Interests> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interests> interestList) {
        this.interestList = interestList;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Tags> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tags> tagList) {
        this.tagList = tagList;
    }

    public List<AuthorRole> getAuthorRoleList() {
        return authorRoleList;
    }

    public void setAuthorRoleList(List<AuthorRole> authorRoleList) {
        this.authorRoleList = authorRoleList;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public Part getImageFile() {
        return imageFile;
    }

    public void setImageFile(Part imageFile) {
        this.imageFile = imageFile;
    }

    public Integer[] getCropCoordinates() {
        return cropCoordinates;
    }

    public void setCropCoordinates(Integer[] cropCoordinates) {
        this.cropCoordinates = cropCoordinates;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public Part getMediaFile1() {
        return mediaFile1;
    }

    public void setMediaFile1(Part mediaFile1) {
        this.mediaFile1 = mediaFile1;
    }

    public Part getMediaFile2() {
        return mediaFile2;
    }

    public void setMediaFile2(Part mediaFile2) {
        this.mediaFile2 = mediaFile2;
    }

    public Part getMediaFile3() {
        return mediaFile3;
    }

    public void setMediaFile3(Part mediaFile3) {
        this.mediaFile3 = mediaFile3;
    }

}
