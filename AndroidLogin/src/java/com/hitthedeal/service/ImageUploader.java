package com.hitthedeal.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

/**
 * Servlet implementation class ImageUploader
 */
@WebServlet("/ImageUploader")
public class ImageUploader extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	public ImageUploader() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		File imageDirectory = new File(getServletContext().getRealPath("")
				+ File.separator + "images");
		if (!imageDirectory.exists()) {
			imageDirectory.mkdir();
			System.out.println(imageDirectory.getPath());
		}
		System.out.println(imageDirectory.getPath());
	}

	
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String imageDataString = request.getParameter("image");
                String imageName = request.getParameter("image_name");
		byte[] imageByteArray = decodeImage(imageDataString);
                File imageDirectory = new File(getServletContext().getRealPath("")
				+ File.separator + "images");
                
                
		if (!imageDirectory.exists()) {
			imageDirectory.mkdir();
		}

		FileOutputStream fOut = new FileOutputStream(imageDirectory
				+ File.separator + imageName);
		fOut.write(imageByteArray);
		System.out.println(getServletContext());
		response.getWriter().println("Done");
	}

	public static byte[] decodeImage(String imageDataString) {
		return Base64.decodeBase64(imageDataString);
	}
}
