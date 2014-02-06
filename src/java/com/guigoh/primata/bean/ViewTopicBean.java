/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.DiscussionTopicBO;
import com.guigoh.primata.bo.DiscussionTopicFilesBO;
import com.guigoh.primata.bo.DiscussionTopicMsgBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.DiscussionTopic;
import com.guigoh.primata.entity.DiscussionTopicFiles;
import com.guigoh.primata.entity.DiscussionTopicMsg;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Users;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    private List<DiscussionTopicFiles> newFilesList;
    private int files;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            DiscussionTopicBO dtBO = new DiscussionTopicBO();
            DiscussionTopicMsgBO dtmBO = new DiscussionTopicMsgBO();
            DiscussionTopicFilesBO dtfBO = new DiscussionTopicFilesBO();
            if (discussionTopic == null) {
                discussionTopic = dtBO.findDiscussionTopicByID(discussionTopicID);
                discussionTopic.setDiscussionTopicFilesList(dtfBO.getDiscussionTopicFilesByFK(discussionTopic.getId(), TOPIC));

                discussionTopicMsgList = dtmBO.findDiscussionTopicMsgsByTopic(discussionTopic.getId());
                for (DiscussionTopicMsg dtm : discussionTopicMsgList) {
                    dtm.setDiscussionTopicFilesList(dtfBO.getDiscussionTopicFilesByFK(dtm.getId(), MESSAGE));
                }
                newFilesList = new ArrayList<DiscussionTopicFiles>();
                newReply = "";
                user = new Users();
                loadUserCookie();
                SocialProfileBO spBO = new SocialProfileBO();
                socialProfile = spBO.findSocialProfile(user.getToken());
            } else if (discussionTopic.getId() != discussionTopicID){
                discussionTopic = dtBO.findDiscussionTopicByID(discussionTopicID);
                discussionTopic.setDiscussionTopicFilesList(dtfBO.getDiscussionTopicFilesByFK(discussionTopic.getId(), TOPIC));

                discussionTopicMsgList = dtmBO.findDiscussionTopicMsgsByTopic(discussionTopic.getId());
                for (DiscussionTopicMsg dtm : discussionTopicMsgList) {
                    dtm.setDiscussionTopicFilesList(dtfBO.getDiscussionTopicFilesByFK(dtm.getId(), MESSAGE));
                }
                newFilesList = new ArrayList<DiscussionTopicFiles>();
                newReply = "";
                user = new Users();
                loadUserCookie();
                SocialProfileBO spBO = new SocialProfileBO();
                socialProfile = spBO.findSocialProfile(user.getToken());
            }
            loadSessionFiles();
        }
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

    private void loadSessionFiles() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession session = request.getSession();
        if (files == 1) {
            newFilesList = (List<DiscussionTopicFiles>) session.getAttribute("listDiscussionTopicFiles");
        } else {
            session.removeAttribute("listDiscussionTopicFiles");
            newFilesList = new ArrayList<DiscussionTopicFiles>();
        }
    }

    public void redirect(String filePath) {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(filePath.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(String filePath, String fileType) {
        File file = new File(filePath);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        downloadFile(file, fileType, facesContext);
    }

    public void holdViewTopic() {
    }

    public static synchronized void downloadFile(File file, String mimeType, FacesContext facesContext) {

        ExternalContext context = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        response.setHeader("Content-Disposition", "attachment;filename=\"" + file.getName() + "\"");
        response.setContentLength((int) file.length());
        response.setContentType(mimeType);
        try {
            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();
            byte[] buf = new byte[(int) file.length()];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            in.close();
            out.flush();
            out.close();
            facesContext.responseComplete();
        } catch (IOException ex) {
            System.out.println("Error in downloadFile: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void replyTopic() throws RollbackFailureException, Exception {
        try {
            if (!newReply.equals("")) {
                DiscussionTopicMsgBO dtmBO = new DiscussionTopicMsgBO();
                DiscussionTopicFilesBO dtfBO = new DiscussionTopicFilesBO();
                DiscussionTopicMsg discussionTopicMsg = new DiscussionTopicMsg();
                discussionTopicMsg.setDiscussionTopicId(discussionTopic);
                discussionTopicMsg.setReply(newReply);
                discussionTopicMsg.setStatus('A');
                discussionTopicMsg.setSocialProfileId(socialProfile);
                discussionTopicMsg.setData(dtmBO.getServerTime());
                dtmBO.replyTopic(discussionTopicMsg);
                if (newFilesList != null && !newFilesList.isEmpty()) {
                    for (DiscussionTopicFiles dtf : newFilesList) {
                        dtf.setFkId(discussionTopicMsg.getId());
                        dtf.setFkType(MESSAGE);
                        dtfBO.create(dtf);
                        discussionTopicMsg.getDiscussionTopicFilesList().add(dtf);
                    }
                }
                FacesContext facesContext = FacesContext.getCurrentInstance();
                HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
                HttpSession session = request.getSession();
                session.removeAttribute("listDiscussionTopicFiles");
                newFilesList = new ArrayList<DiscussionTopicFiles>();
                discussionTopicMsgList.add(discussionTopicMsg);
                newReply = "";
            }
            newReply = "";
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

    public List<DiscussionTopicFiles> getNewFilesList() {
        return newFilesList;
    }

    public void setNewFilesList(List<DiscussionTopicFiles> newFilesList) {
        this.newFilesList = newFilesList;
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

    public int getFiles() {
        return files;
    }

    public void setFiles(int files) {
        this.files = files;
    }
}
