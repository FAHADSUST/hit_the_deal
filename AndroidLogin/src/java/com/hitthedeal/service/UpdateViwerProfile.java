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
@WebServlet(name = "UpdateViwerProfile", urlPatterns = {"/UpdateViwerProfile"})
public class UpdateViwerProfile extends HttpServlet {

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
            out.println("<title>Servlet UpdateViwerProfile</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateViwerProfile at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONObject json = new JSONObject();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

//        String imageDataString = request.getParameter("image");
//        String imageName = request.getParameter("image_name");

        String signUpkey[] = {"user_id", "user_name", "address", "phn_no"};//"phn_no" ,
        int length = signUpkey.length;
        params = new String[length];
        for (int i = 0; i < length; i++) {
            params[i] = request.getParameter(signUpkey[i]);
        }




        String sql = "UPDATE `user` SET  user_name=?,address=?,phn_no=? WHERE user_id=?";//,phn_no=?
        Connection con = DBConnectionHandler.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, params[1]);
            ps.setString(2, params[2]);
            ps.setString(3, params[3]);
            ps.setString(4, params[0]);
            

            int rsInt = ps.executeUpdate();
            if (rsInt != 0) {
                json.put("success", "1");

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
    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    String[] params;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONObject json = new JSONObject();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

//        String imageDataString = request.getParameter("image");
//        String imageName = request.getParameter("image_name");

        String signUpkey[] = {"user_id", "user_name", "address", "phn_no"};//"phn_no" ,
        int length = signUpkey.length;
        params = new String[length];
        for (int i = 0; i < length; i++) {
            params[i] = request.getParameter(signUpkey[i]);
        }




        String sql = "UPDATE `user` SET  user_name=?,address=?,phn_no=? WHERE user_id=?";//,phn_no=?
        Connection con = DBConnectionHandler.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, params[1]);
            ps.setString(2, params[2]);
            ps.setString(3, params[3]);
            ps.setString(4, params[0]);
           

            int rsInt = ps.executeUpdate();
            if (rsInt != 0) {
                json.put("success", "1");

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
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
