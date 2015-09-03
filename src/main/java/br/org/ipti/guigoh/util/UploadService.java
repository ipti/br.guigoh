/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.servlet.http.Part;

/**
 *
 * @author ipti004
 */
public class UploadService {

    public static boolean uploadFile(Part part, String basePath, Integer[] cropImage) throws IOException {

        boolean success;
        // Extract file name from content-disposition header of file part
        String fileName = getFileName(part);
        File directory = new File(basePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File outputFilePath = new File(basePath + fileName);
        // Copy uploaded file to destination path
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = part.getInputStream();
            outputStream = new FileOutputStream(outputFilePath);
            int read = 0;
            final byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            if (cropImage != null) {
                String[] fileSplit = part.getSubmittedFileName().split("\\.");
                String fileType = fileSplit[fileSplit.length - 1];
                BufferedImage image = ImageIO.read(outputFilePath);
                Integer x = cropImage[0];
                Integer y = cropImage[1];
                Integer w = cropImage[2];
                Integer h = cropImage[3];
                BufferedImage out = image.getSubimage(x, y, w, h);
                ImageIO.write(out, fileType, new File(basePath + fileName));
            }
            success = true;
        } catch (IOException e) {
            success = false;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return success;
    }

    public static String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim()
                        .replace("\"", "");
            }
        }
        return null;
    }
}
