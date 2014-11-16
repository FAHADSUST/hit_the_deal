/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bari.login;

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
import org.json.simple.JSONObject;

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
        
        JSONObject json = new JSONObject();
                  
        response.setContentType("text/html");  
        PrintWriter out = response.getWriter();  
        String signUpkey[] = {"user_type_id", "user_name", "address", "email", "phn_no", "date_of_creation", "latitude", "longitude", "image_url", "password", "creator_type_id"};
        int length = signUpkey.length;
        String params[] = new String[length]; 
        for(int i=0;i<length;i++){
            params[i]=request.getParameter(signUpkey[i]);
        }   
                
        String sql = "INSERT INTO `hit_the_deal`.`user` (`user_id`, `user_type_id`, `user_name`, `address`, `email`, `phn_no`, `date_of_creation`, `latitude`, `longitude`, `image_url`, `password`, `creator_type_id`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = DBConnectionHandler.getConnection();
         
        try {
            System.out.println("dssdsfdf");
            PreparedStatement ps = con.prepareStatement(sql);
            for(int i=0;i<length;i++){
                ps.setString(i+1, params[i]);
            }
           System.out.println("343434dssdsfdf");
            int rsInt = ps.executeUpdate();
            if (rsInt !=0) {
                json.put("success", "1");
                //json.put("user_type_id",params[0]);
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

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
