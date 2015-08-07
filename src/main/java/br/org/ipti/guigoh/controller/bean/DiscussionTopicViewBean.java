/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.DiscussionTopic;
import br.org.ipti.guigoh.model.entity.DiscussionTopicFiles;
import br.org.ipti.guigoh.model.entity.DiscussionTopicMsg;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicFilesJpaController;
import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicJpaController;
import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicMsgJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UtilJpaController;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import br.org.ipti.guigoh.util.CookieService;
import br.org.ipti.guigoh.util.UploadService;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

/**
 *
 * @author IPTI
 */
@ViewScoped
@ManagedBean(name = "discussionTopicViewBean")
public class DiscussionTopicViewBean implements Serializable {

    private static final char TOPIC = 'T', MESSAGE = 'M';

    private int discussionTopicID;
    private String newReply;

    private DiscussionTopic discussionTopic;
    private Users user;
    private SocialProfile socialProfile;

    private List<DiscussionTopicMsg> discussionTopicMsgList;

    private transient Part fileMedia;
    private transient List<Part> fileList;

    private DiscussionTopicJpaController discussionTopicJpaController;
    private DiscussionTopicFilesJpaController discussionTopicFilesJpaController;
    private DiscussionTopicMsgJpaController discussionTopicMsgJpaController;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }

    public void addMedia() throws IOException {
        if (fileList.size() < 3) {
            if (!fileMedia.getSubmittedFileName().equals("") && fileMedia.getSubmittedFileName().contains(".")) {
                fileList.add(fileMedia);
            }
        }
    }

    public void removeMedia(Part media) {
        fileList.remove(media);
    }

    public void replyTopic() throws RollbackFailureException, Exception {
        try {
            UtilJpaController utilJpaController = new UtilJpaController();
            newReply = new String(newReply.getBytes("ISO-8859-1"), "UTF-8");
            if (!newReply.equals("")) {
                DiscussionTopicMsg discussionTopicMsg = new DiscussionTopicMsg();
                discussionTopicMsg.setDiscussionTopicId(discussionTopic);
                discussionTopicMsg.setReply(newReply);
                discussionTopicMsg.setStatus('A');
                discussionTopicMsg.setSocialProfileId(socialProfile);
                discussionTopicMsg.setData(utilJpaController.getTimestampServerTime());
                discussionTopicMsgJpaController.create(discussionTopicMsg);
                List<DiscussionTopicFiles> dtfList = new ArrayList<>();
                if (!fileList.isEmpty()) {
                    for (Part part : fileList) {
                        String filePath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn" + File.separator + "guigoh" + File.separator + "discussionFiles" + File.separator + "message" + File.separator + discussionTopicMsg.getId() + File.separator;
                        UploadService.uploadFile(part, filePath);
                        DiscussionTopicFiles discussionTopicFiles = new DiscussionTopicFiles();
                        String[] fileSplit = part.getSubmittedFileName().split("\\.");
                        discussionTopicFiles.setFileName(part.getSubmittedFileName().replace("." + fileSplit[fileSplit.length - 1], ""));
                        discussionTopicFiles.setFileType(fileSplit[fileSplit.length - 1]);
                        discussionTopicFiles.setFilepath("http://cdn.guigoh.com/guigoh/discussionFiles/message/" + discussionTopicMsg.getId() + "/" + part.getSubmittedFileName());
                        discussionTopicFiles.setFkType(MESSAGE);
                        discussionTopicFiles.setFkId(discussionTopicMsg.getId());
                        dtfList.add(discussionTopicFiles);
                        discussionTopicFilesJpaController.create(discussionTopicFiles);
                    }
                }
                discussionTopicMsg.setDiscussionTopicFilesList(dtfList);
                discussionTopicMsgList.add(discussionTopicMsg);
            }
            newReply = "";
            fileList = new ArrayList<>();
        } catch (IOException e) {
        }
    }
    
    private void initGlobalVariables() {
        user = new Users();
        
        user.setUsername(CookieService.getCookie("user"));
        user.setToken(CookieService.getCookie("token"));
        
        fileList = new ArrayList<>();
        
        discussionTopicJpaController = new DiscussionTopicJpaController();
        discussionTopicFilesJpaController = new DiscussionTopicFilesJpaController();
        discussionTopicMsgJpaController = new DiscussionTopicMsgJpaController();
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        
        newReply = "";
        
        if (discussionTopic == null) {
            discussionTopic = discussionTopicJpaController.findDiscussionTopic(discussionTopicID);
            discussionTopic.setDiscussionTopicFilesList(discussionTopicFilesJpaController.getDiscussionTopicFilesByFK(discussionTopic.getId(), TOPIC));

            discussionTopicMsgList = discussionTopicMsgJpaController.findDiscussionTopicMsgsByTopic(discussionTopic.getId());
            discussionTopicMsgList.stream().forEach((dtm) -> {
                dtm.setDiscussionTopicFilesList(discussionTopicFilesJpaController.getDiscussionTopicFilesByFK(dtm.getId(), MESSAGE));
            });
            socialProfile = socialProfileJpaController.findSocialProfile(user.getToken());
        } else if (discussionTopic.getId() != discussionTopicID) {
            discussionTopic = discussionTopicJpaController.findDiscussionTopic(discussionTopicID);
            discussionTopic.setDiscussionTopicFilesList(discussionTopicFilesJpaController.getDiscussionTopicFilesByFK(discussionTopic.getId(), TOPIC));
            discussionTopicMsgList = discussionTopicMsgJpaController.findDiscussionTopicMsgsByTopic(discussionTopic.getId());
            discussionTopicMsgList.stream().forEach((dtm) -> {
                dtm.setDiscussionTopicFilesList(discussionTopicFilesJpaController.getDiscussionTopicFilesByFK(dtm.getId(), MESSAGE));
            });
            socialProfile = socialProfileJpaController.findSocialProfile(user.getToken());
        }
    }

    public int getDiscussionTopicID() {
        return discussionTopicID;
    }

    public void setDiscussionTopicID(int discussionTopicID) {
        this.discussionTopicID = discussionTopicID;
    }

    public DiscussionTopic getDiscussionTopic() {
        return discussionTopic;
    }

    public void setDiscussionTopic(DiscussionTopic discussionTopic) {
        this.discussionTopic = discussionTopic;
    }

    public List<DiscussionTopicMsg> getDiscussionTopicMsgList() {
        return discussionTopicMsgList;
    }

    public void setDiscussionTopicMsgList(List<DiscussionTopicMsg> discussionTopicMsgList) {
        this.discussionTopicMsgList = discussionTopicMsgList;
    }

    public String getNewReply() {
        return newReply;
    }

    public void setNewReply(String newReply) {
        this.newReply = newReply;
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

    public Part getFileMedia() {
        return fileMedia;
    }

    public void setFileMedia(Part fileMedia) {
        this.fileMedia = fileMedia;
    }

    public List<Part> getFileList() {
        return fileList;
    }

    public void setFileList(List<Part> fileList) {
        this.fileList = fileList;
    }
}
