/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo.util;

import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.entity.DiscussionTopicFiles;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Users;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.guigoh.primata.entity.DiscussionTopicFiles;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Joe
 */
@WebServlet(name = "UploadFile", urlPatterns = {"/primata/discussion/UploadFile"})
public class UploadFile extends HttpServlet implements Serializable{

    Users user = new Users();
    Boolean topic = false;

    public static Object getManagedBean(String beanName) {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.getApplication().getELResolver().getValue(fc.getELContext(), null, beanName);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileUploadException, Exception {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        //String querys = request.getQueryString();
        //String fileType = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fileType");

        HttpSession session = request.getSession();
        List<DiscussionTopicFiles> listDiscussionTopicFiles = new ArrayList<DiscussionTopicFiles>();
        //session.setAttribute("nomeObjeto", d); 

        List<FileItem> items = null;
        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException ex) {
            Logger.getLogger(UploadFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            if (item.isFormField()) {
                if (item.getFieldName().equals("topic")) {
                    topic = true;
                }
                if (item.getFieldName().equals("message")) {
                    topic = false;
                }

            } else {
                //byte[] arquivo = item.get();
                if (item.getSize() < 1000000 && item.getSize() > 0) {
                    //String caminho = getServletContext().getRealPath();
                    try {
                        DiscussionTopicFiles discussionTopicFiles = new DiscussionTopicFiles();
                        loadUserCookie(request);
                        //SocialProfileBO socialProfileBO = new SocialProfileBO();
                        //SocialProfile socialProfile = socialProfileBO.findSocialProfile(user.getToken());

                        //File f = new File("C:\\Users\\Joe\\Documents\\Guigoh\\web\\resources\\images\\"+ item.getName());
                        String place = this.getServletContext().getRealPath("/");
                        String type = item.getContentType().split("/")[1];
                        File f = new File("/home/JBoss/arteciencia/primatadata/discussionFiles/" + item.getName());
                        item.write(f);

                        //socialProfile.setPhoto("/resources/images/" + socialProfile.getSocialProfileId() + "." + type);
                        //socialProfileBO.edit(socialProfile);

                        discussionTopicFiles.setFileName(item.getName());
                        discussionTopicFiles.setFileType(type);
                        discussionTopicFiles.setFilepath("cdn.guigoh.com/primatadata/discussionFiles/" + item.getName());

                        listDiscussionTopicFiles.add(discussionTopicFiles);
                        //request.setAttribute("content", item.getContentType());
                        //request.setAttribute("size", item.getSize());
                        //request.getRequestDispatcher("/primata/profile/exibearquivo.jsp").forward(request, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        session.setAttribute("listDiscussionTopicFiles", listDiscussionTopicFiles);
        if (topic) {
            request.getRequestDispatcher("/primata/discussion/createTopic.xhtml?files=1").forward(request, response);
        } else {
            request.getRequestDispatcher("/primata/discussion/viewTopic.xhtml?files=1").forward(request, response);
        }


        //request.getRequestDispatcher("/primata/discussion/viewTopic.xhtml?id=3").forward(request, response);

    }

    private void loadUserCookie(HttpServletRequest request) {

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
