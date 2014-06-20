/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.DiscussionTopicBO;
import com.guigoh.primata.bo.DiscussionTopicFilesBO;
import com.guigoh.primata.bo.DiscussionTopicMsgBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.util.CookieService;
import com.guigoh.primata.bo.util.UploadService;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.DiscussionTopic;
import com.guigoh.primata.entity.DiscussionTopicFiles;
import com.guigoh.primata.entity.DiscussionTopicMsg;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Users;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    private Part fileMedia;
    private List<Part> fileList;
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
            if (!fileMedia.getSubmittedFileName().equals("")) {
                fileList.add(fileMedia);
            }
        }
    }

    public void replyTopic() throws RollbackFailureException, Exception {
        try {
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
                        String filePath = System.getProperty("user.home") + File.separator + "guigoh" + File.separator + "discussionFiles" + File.separator;
                        UploadService.uploadFile(part, filePath);
                        DiscussionTopicFiles discussionTopicFiles = new DiscussionTopicFiles();
                        discussionTopicFiles.setFileName(part.getSubmittedFileName());
                        discussionTopicFiles.setFileType(part.getContentType().split("/")[1]);
                        discussionTopicFiles.setFilepath("http://cdn.guigoh.com/discussionFiles/" + part.getSubmittedFileName());
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
