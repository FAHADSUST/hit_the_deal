/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bari.login;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Fahad_G4
 */
public class ImageUpload {

    public static void imageUploadToServer(ServletContext servletContext, String imageName, String imageDataString) {
        try {
            byte[] imageByteArray = decodeImage(imageDataString);
            File imageDirectory = new File(servletContext.getRealPath("")
                    + File.separator + "profile_images");


            if (!imageDirectory.exists()) {
                imageDirectory.mkdir();
            }

            FileOutputStream fOut = new FileOutputStream(imageDirectory
                    + File.separator + imageName);
            fOut.write(imageByteArray);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public static byte[] decodeImage(String imageDataString) {
		return Base64.decodeBase64(imageDataString);
    }
}
