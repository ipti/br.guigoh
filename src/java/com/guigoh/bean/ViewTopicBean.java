/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bean;

import com.guigoh.bo.DiscussionTopicBO;
import com.guigoh.bo.DiscussionTopicFilesBO;
import com.guigoh.bo.DiscussionTopicMsgBO;
import com.guigoh.bo.SocialProfileBO;
import com.guigoh.bo.util.CookieService;
import com.guigoh.bo.util.UploadService;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.DiscussionTopic;
import com.guigoh.entity.DiscussionTopicFiles;
import com.guigoh.entity.DiscussionTopicMsg;
import com.guigoh.entity.SocialProfile;
import com.guigoh.entity.Users;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

/**
 *
 * @author IPTI
 */
@SessionScoped
@ManagedBean(name = "viewTopicBean")
public class ViewTopicBean implements Serializable {

    private static final char TOPIC = 'T';
    private static final char MESSAGE = 'M';
    private int discussionTopicID;
    private DiscussionTopic discussionTopic;
    private List<DiscussionTopicMsg> discussionTopicMsgList;
    private String newReply;
    private Users user;
    private SocialProfile socialProfile;
    private transient Part fileMedia;
    private transient List<Part> fileList;
    private DiscussionTopicMsgBO dtmBO;
    private DiscussionTopicBO dtBO;
    private DiscussionTopicFilesBO dtfBO;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            dtBO = new DiscussionTopicBO();
            dtmBO = new DiscussionTopicMsgBO();
            dtfBO = new DiscussionTopicFilesBO();
            user = new Users();
            user.setUsername(CookieService.getCookie("user"));
            user.setToken(CookieService.getCookie("token"));
            fileList = new ArrayList<>();
            newReply = "";
            if (discussionTopic == null) {
                discussionTopic = dtBO.findDiscussionTopicByID(discussionTopicID);
                discussionTopic.setDiscussionTopicFilesList(dtfBO.getDiscussionTopicFilesByFK(discussionTopic.getId(), TOPIC));

                discussionTopicMsgList = dtmBO.findDiscussionTopicMsgsByTopic(discussionTopic.getId());
                for (DiscussionTopicMsg dtm : discussionTopicMsgList) {
                    dtm.setDiscussionTopicFilesList(dtfBO.getDiscussionTopicFilesByFK(dtm.getId(), MESSAGE));
                }
                SocialProfileBO spBO = new SocialProfileBO();
                socialProfile = spBO.findSocialProfile(user.getToken());
            } else if (discussionTopic.getId() != discussionTopicID) {
                discussionTopic = dtBO.findDiscussionTopicByID(discussionTopicID);
                discussionTopic.setDiscussionTopicFilesList(dtfBO.getDiscussionTopicFilesByFK(discussionTopic.getId(), TOPIC));
                discussionTopicMsgList = dtmBO.findDiscussionTopicMsgsByTopic(discussionTopic.getId());
                for (DiscussionTopicMsg dtm : discussionTopicMsgList) {
                    dtm.setDiscussionTopicFilesList(dtfBO.getDiscussionTopicFilesByFK(dtm.getId(), MESSAGE));
                }
                SocialProfileBO spBO = new SocialProfileBO();
                socialProfile = spBO.findSocialProfile(user.getToken());
            }
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
            newReply = new String(newReply.getBytes("ISO-8859-1"), "UTF-8");
            if (!newReply.equals("")) {
                DiscussionTopicMsg discussionTopicMsg = new DiscussionTopicMsg();
                discussionTopicMsg.setDiscussionTopicId(discussionTopic);
                discussionTopicMsg.setReply(newReply);
                discussionTopicMsg.setStatus('A');
                discussionTopicMsg.setSocialProfileId(socialProfile);
                discussionTopicMsg.setData(dtmBO.getServerTime());
                dtmBO.replyTopic(discussionTopicMsg);
                List<DiscussionTopicFiles> dtfList = new ArrayList<>();
                if (!fileList.isEmpty()) {
                    for (Part part : fileList) {
                        String filePath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn" + File.separator + "guigoh" + File.separator + "discussionFiles" + File.separator + "message" + File.separator + discussionTopicMsg.getId() + File.separator;
                        UploadService.uploadFile(part, filePath);
                        DiscussionTopicFiles discussionTopicFiles = new DiscussionTopicFiles();
                        String[] fileSplit = part.getSubmittedFileName().split("\\.");
                        discussionTopicFiles.setFileName(part.getSubmittedFileName().replace("."+fileSplit[fileSplit.length - 1], ""));
                        discussionTopicFiles.setFileType(fileSplit[fileSplit.length - 1]);
                        discussionTopicFiles.setFilepath("http://cdn.guigoh.com/guigoh/discussionFiles/message/" + discussionTopicMsg.getId() + "/" + part.getSubmittedFileName());
                        discussionTopicFiles.setFkType(MESSAGE);
                        discussionTopicFiles.setFkId(discussionTopicMsg.getId());
                        dtfList.add(discussionTopicFiles);
                        dtfBO.create(discussionTopicFiles);
                    }
                }
                discussionTopicMsg.setDiscussionTopicFilesList(dtfList);
                discussionTopicMsgList.add(discussionTopicMsg);
            }
            newReply = "";
            fileList = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
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
