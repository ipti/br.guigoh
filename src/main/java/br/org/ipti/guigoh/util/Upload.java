/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.util;

import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
@WebServlet(name = "Upload", urlPatterns = {"/profile/Upload"})
public class Upload extends HttpServlet {

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
        for (Iterator<FileItem> it = items.iterator(); it.hasNext();) {
            FileItem item = it.next();
            if (item.isFormField()) {
            } else {
                //byte[] arquivo = item.get();
                if (((item.getContentType().equals("image/jpeg")) || (item.getContentType().equals("image/gif"))
                        || (item.getContentType().equals("image/png"))) && item.getSize() < 1000000) {
                    //String caminho = getServletContext().getRealPath();
                    try {
                        user.setToken(CookieService.getCookieByRequest("token", request));
                        user.setUsername(CookieService.getCookieByRequest("user", request));
                        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
                        SocialProfile socialProfile = socialProfileJpaController.findSocialProfile(user.getToken());

                        //File f = new File("C:\\Users\\Paulo\\Documents\\guigohdata\\socialProfile\\photo\\" + socialProfile.getSocialProfileId() + "." + item.getName());
                        String type = item.getContentType().split("/")[1];
                        File f = new File("/home/www/com.guigoh.cdn/guigoh/users/" + socialProfile.getSocialProfileId() + "." + type);
                        item.write(f);

                        socialProfile.setPhoto("http://cdn.guigoh.com/guigoh/users/" + socialProfile.getSocialProfileId() + "." + type);
                        socialProfileJpaController.edit(socialProfile);
                        //request.setAttribute("content", item.getContentType());
                        //request.setAttribute("size", item.getSize());
                        //request.getRequestDispatcher("/profile/exibearquivo.jsp").forward(request, response);
                    } catch (Exception e) {
                    }
                }
            }
        }
        request.getRequestDispatcher("/profile/view.xhtml").forward(request, response);
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
