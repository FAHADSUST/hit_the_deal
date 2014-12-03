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
import java.util.Enumeration;
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
@WebServlet(name = "LoginHitTheDeal", urlPatterns = {"/LoginHitTheDeal"})
public class LoginHitTheDeal extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        JSONObject json = new JSONObject();
                  
        response.setContentType("text/html");  
        PrintWriter out = response.getWriter();  
        String params[] = new String[2];  
        
        params[0]=request.getParameter("email");  
        params[1]=request.getParameter("password"); 
        
        String userToken[] = {"user_id", "user_type_id", "user_name", "address", "email", "phn_no", "date_of_creation", "latitude", "longitude", "image_url", "password", "creator_type_id"};
        int length = userToken.length;
 
        String sql = "SELECT * FROM `user` WHERE email=? and password=?";
        Connection con = DBConnectionHandler.getConnection();
         
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, params[0]);
            ps.setString(2, params[1]);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                
                JSONObject jsonObj = new JSONObject();
                for(int i=0;i<length;i++){
                    jsonObj.put(userToken[i], rs.getString(userToken[i]));
                }                               
                json.put("success", "1");
                json.put("userData",jsonObj);
            } else {
                json.put("success", "0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(json);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
    
    
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
            out.println("<title>Servlet LoginHitTheDeal</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginHitTheDeal at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

        
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
