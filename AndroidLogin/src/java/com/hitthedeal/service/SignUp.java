/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hitthedeal.service;

import dbConnection.DBConnectionHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author Fahad_G4
 */
@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SignUp</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SignUp at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
    }

    String params[];
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();                 
        response.setContentType("text/html");  
        PrintWriter out = response.getWriter(); 
        
        String imageDataString = request.getParameter("image");
        String imageName = request.getParameter("image_name");
        
        String signUpkey[] = {"user_type_id", "user_name", "address", "email", "phn_no", "date_of_creation", "latitude", "longitude", "image_url", "password", "creator_type_id"};
        int length = signUpkey.length;
        params = new String[length]; 
        for(int i=0;i<length;i++){
            params[i]=request.getParameter(signUpkey[i]);
        }   
        
        
        String checkingDql = "SELECT * FROM `user` WHERE email=?";
        Connection chekingCon = DBConnectionHandler.getConnection();
        try {
            PreparedStatement ps1 = chekingCon.prepareStatement(checkingDql);          
            ps1.setString(1, params[3]);           
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                json.put("success", "0");
                
            } else {
                if(!imageDataString.equals(""))
                    ImageUpload.imageUploadToServer(getServletContext(), imageName, imageDataString);
                
                String sql = "INSERT INTO `hit_the_deal`.`user` (`user_id`, `user_type_id`, `user_name`, `address`, `email`, `phn_no`, `date_of_creation`, `latitude`, `longitude`, `image_url`, `password`, `creator_type_id`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                Connection con = DBConnectionHandler.getConnection();

                try {
                    PreparedStatement ps = con.prepareStatement(sql);
                    for(int i=0;i<length;i++){
                        ps.setString(i+1, params[i]);
                    }
                System.out.println("343434dssdsfdf");
                    int rsInt = ps.executeUpdate();
                    if (rsInt !=0) {
                        json.put("success", "1");
                        json.put("user_id", getUserId());
                        //json.put("user_type_id",params[0]);
                    } else {
                        json.put("success", "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        //System.out.println(json);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
        
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    public String getUserId(){
        String sql = "SELECT * FROM `user` WHERE email=?";
        Connection con = DBConnectionHandler.getConnection();
         
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, params[3]);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                
                return rs.getString("user_id");
            } else {
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-1";
    }
}
