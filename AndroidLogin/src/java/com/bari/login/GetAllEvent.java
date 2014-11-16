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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Fahad_G4
 */
@WebServlet(name = "GetAllEvent", urlPatterns = {"/GetAllEvent"})
public class GetAllEvent extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
            out.println("<title>Servlet GetAllEvent</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetAllEvent at " + request.getContextPath() + "</h1>");
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
        String eventkey[] = {"event_id","creator_id","event_description","start_date","end_date","longitude","latitude"};
        int length = eventkey.length;
//        String params[] = new String[length]; 
//        for(int i=0;i<length;i++){
//            params[i]=request.getParameter(signUpkey[i]);
//        }   
                
        String sql = "SELECT * FROM `events` WHERE 1";
        Connection con = DBConnectionHandler.getConnection();
         
        try {
            PreparedStatement ps = con.prepareStatement(sql);
//            for(int i=0;i<length;i++){
//                ps.setString(i+1, params[i]);
//            }           
            ResultSet rs = ps.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            boolean checkNull = true;
            while(rs.next()) {
                checkNull = false;
                JSONObject jsonInner = new JSONObject();
                for(int i=0;i<length;i++){
                    
                    jsonInner.put(eventkey[i], rs.getString(eventkey[i]));
                }                
                jsonArray.add(jsonInner);
                //json.put("info", "success");
            }
            if(!checkNull){
                json.put("success", "1");
                json.put("all", jsonArray);
            }
            else json.put("success", "0");
            
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

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
