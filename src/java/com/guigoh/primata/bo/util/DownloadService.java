/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guigoh.primata.bo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ipti004
 */
public class DownloadService {
    
    public static void downloadFile(String filePath, String fileType) {
        File file = new File(filePath);
        downloadFile(file, fileType, FacesContext.getCurrentInstance());
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
}
