/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.bean;

import br.org.ipti.guigoh.model.entity.DiscussionTopic;
import br.org.ipti.guigoh.model.entity.DiscussionTopicFiles;
import br.org.ipti.guigoh.model.entity.DiscussionTopicMsg;
import br.org.ipti.guigoh.model.entity.Interests;
import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicFilesJpaController;
import br.org.ipti.guigoh.model.jpa.controller.DiscussionTopicJpaController;
import br.org.ipti.guigoh.model.jpa.controller.InterestsJpaController;
import br.org.ipti.guigoh.util.DownloadService;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Part;

/**
 *
 * @author iptipc008
 */
@ViewScoped
@Named
public class StudyGroupViewBean implements Serializable {
    
    private Integer discussionTopicId;

    private DiscussionTopic discussionTopic;

    private List<Interests> interestList;

    private DiscussionTopicJpaController discussionTopicJpaController;
    private DiscussionTopicFilesJpaController discussionTopicFilesJpaController;
    private InterestsJpaController interestsJpaController;

    public void init() throws IOException {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initGlobalVariables();
        }
    }

//    public void addMedia() throws IOException {
//        if (fileList.size() < 3) {
//            if (!fileMedia.getSubmittedFileName().equals("") && fileMedia.getSubmittedFileName().contains(".")) {
//                fileList.add(fileMedia);
//            }
//        }
//    }
//
//    public void removeMedia(Part media) {
//        fileList.remove(media);
//    }
//
//    public void replyTopic() throws RollbackFailureException, Exception {
//        try {
//            UtilJpaController utilJpaController = new UtilJpaController();
//            newReply = new String(newReply.getBytes("ISO-8859-1"), "UTF-8");
//            if (!newReply.equals("")) {
//                DiscussionTopicMsg discussionTopicMsg = new DiscussionTopicMsg();
//                discussionTopicMsg.setDiscussionTopicId(discussionTopic);
//                discussionTopicMsg.setReply(newReply);
//                discussionTopicMsg.setStatus('A');
//                discussionTopicMsg.setSocialProfileId(socialProfile);
//                discussionTopicMsg.setData(utilJpaController.getTimestampServerTime());
//                discussionTopicMsgJpaController.create(discussionTopicMsg);
//                List<DiscussionTopicFiles> dtfList = new ArrayList<>();
//                if (!fileList.isEmpty()) {
//                    for (Part part : fileList) {
//                        String filePath = File.separator + "home" + File.separator + "www" + File.separator + "com.guigoh.cdn" + File.separator + "guigoh" + File.separator + "discussionFiles" + File.separator + "message" + File.separator + discussionTopicMsg.getId() + File.separator;
//                        UploadService.uploadFile(part, filePath, null);
//                        DiscussionTopicFiles discussionTopicFiles = new DiscussionTopicFiles();
//                        String[] fileSplit = part.getSubmittedFileName().split("\\.");
//                        discussionTopicFiles.setFileName(part.getSubmittedFileName().replace("." + fileSplit[fileSplit.length - 1], ""));
//                        discussionTopicFiles.setFileType(fileSplit[fileSplit.length - 1]);
//                        discussionTopicFiles.setFilepath("http://cdn.guigoh.com/guigoh/discussionFiles/message/" + discussionTopicMsg.getId() + "/" + part.getSubmittedFileName());
//                        discussionTopicFiles.setFkType(MESSAGE);
//                        discussionTopicFiles.setFkId(discussionTopicMsg.getId());
//                        dtfList.add(discussionTopicFiles);
//                        discussionTopicFilesJpaController.create(discussionTopicFiles);
//                    }
//                }
//                discussionTopicMsg.setDiscussionTopicFilesList(dtfList);
//                discussionTopicMsgList.add(discussionTopicMsg);
//            }
//            newReply = "";
//            fileList = new ArrayList<>();
//        } catch (IOException e) {
//        }
//    }
    
    public void downloadFile(String filePath, String fileType) throws IOException {
        DownloadService.downloadFileFromURL("http://" + filePath, fileType);
    }
    
    private void initGlobalVariables() throws IOException {
        discussionTopicJpaController = new DiscussionTopicJpaController();
        interestsJpaController = new InterestsJpaController();
        discussionTopicFilesJpaController = new DiscussionTopicFilesJpaController();
        
        if (discussionTopicId == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
        } else {
            discussionTopic = discussionTopicJpaController.findDiscussionTopic(discussionTopicId);
            discussionTopic.setDiscussionTopicFilesList(discussionTopicFilesJpaController.getDiscussionTopicFilesByFK(discussionTopic.getId(), 'T'));
            for (DiscussionTopicMsg discussionTopicMsg : discussionTopic.getDiscussionTopicMsgCollection()){
                discussionTopicMsg.setDiscussionTopicFilesList(discussionTopicFilesJpaController.getDiscussionTopicFilesByFK(discussionTopicMsg.getId(), 'M'));
            }
            interestList = interestsJpaController.findInterestsEntities();
        }
    }

    public Integer getDiscussionTopicId() {
        return discussionTopicId;
    }

    public void setDiscussionTopicId(Integer discussionTopicId) {
        this.discussionTopicId = discussionTopicId;
    }

    public DiscussionTopic getDiscussionTopic() {
        return discussionTopic;
    }

    public void setDiscussionTopic(DiscussionTopic discussionTopic) {
        this.discussionTopic = discussionTopic;
    }

    public List<Interests> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interests> interestList) {
        this.interestList = interestList;
    }
}
