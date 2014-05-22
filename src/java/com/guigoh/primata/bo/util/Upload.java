/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo.util;

import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Users;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Joe
 */
@WebServlet(name = "Upload", urlPatterns = {"/primata/profile/Upload"})
public class Upload extends HttpServlet implements Serializable{

    Users user = new Users();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileUploadException, Exception {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> items = null;
        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException ex) {
            Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
        }
        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            if (item.isFormField()) {
            } else {
                //byte[] arquivo = item.get();
                if ( ( (item.getContentType().equals("image/jpeg")) || (item.getContentType().equals("image/gif"))
                        || (item.getContentType().equals("image/png")) ) && item.getSize() < 1000000 ) {
                    //String caminho = getServletContext().getRealPath();
                    try {
                        loadUserCookie(request);
                        SocialProfileBO socialProfileBO = new SocialProfileBO();
                        SocialProfile socialProfile = socialProfileBO.findSocialProfile(user.getToken());

                        //File f = new File("C:\\Users\\Joe\\Documents\\Guigoh\\web\\resources\\images\\"+ item.getName());
                        String place = this.getServletContext().getRealPath("/");
                        String type = item.getContentType().split("/")[1];
                        File f = new File("/home/JBoss/arteciencia/primatadata/users/" + socialProfile.getSocialProfileId() + "." + type);
                        item.write(f);

                        socialProfile.setPhoto("http://cdn.guigoh.com/primatadata/users/" + socialProfile.getSocialProfileId() + "." + type);
                        socialProfileBO.edit(socialProfile);


                        //request.setAttribute("content", item.getContentType());
                        //request.setAttribute("size", item.getSize());
                        //request.getRequestDispatcher("/primata/profile/exibearquivo.jsp").forward(request, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        request.getRequestDispatcher("/primata/profile/viewProfile.xhtml").forward(request, response);
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
