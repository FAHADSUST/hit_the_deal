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
@WebServlet(name = "GetCreatorEvents", urlPatterns = {"/GetCreatorEvents"})
public class GetCreatorEvents extends HttpServlet {

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
            out.println("<title>Servlet GetCreatorEvents</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetCreatorEvents at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String eventkey[] = {"event_id", "creator_id", "event_description", "start_date", "end_date", "longitude", "latitude"};
        int length = eventkey.length;


        String params[] = new String[1];
        //for(int i=0;i<length;i++){
        params[0] = request.getParameter(eventkey[1]);

        

        String sql = "SELECT * FROM `events` WHERE creator_id=?";
        Connection con = DBConnectionHandler.getConnection();

        JSONObject json = new JSONObject();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
//            for (int i = 0; i < 2; i++) {
            ps.setString(1, params[0]);
//            }
            ResultSet rs = ps.executeQuery();

            JSONArray jsonArray = new JSONArray();
            boolean checkNull = true;
            while (rs.next()) {
                JSONObject jsonInner = new JSONObject();


                checkNull = false;
                for (int i = 0; i < length; i++) {

                    jsonInner.put(eventkey[i], rs.getString(eventkey[i]));
                }
                jsonArray.add(jsonInner);

            }
            if (!checkNull) {
                json.put("success", "1");
                json.put("all", jsonArray);
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
