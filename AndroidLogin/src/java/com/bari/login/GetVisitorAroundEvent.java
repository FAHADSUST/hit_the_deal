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
@WebServlet(name = "GetVisitorAroundEvent", urlPatterns = {"/GetVisitorAroundEvent"})
public class GetVisitorAroundEvent extends HttpServlet {

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
            out.println("<title>Servlet GetAroundEvent</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetAroundEvent at " + request.getContextPath() + "</h1>");
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
        String eventkey[] = {"event_id", "creator_id", "event_description", "start_date", "end_date", "longitude", "latitude","event_img", "event_url"};
        String creatorNeededKey[]={"user_name","image_url","creator_type_id"};
        int length = eventkey.length;
        int creatorLength=creatorNeededKey.length;
        
        String params[] = new String[3];
        //for(int i=0;i<length;i++){
        params[0] = request.getParameter(eventkey[5]);
        params[1] = request.getParameter(eventkey[6]);
        params[2] = request.getParameter("distance");
        //}   
        double lon1 = Double.parseDouble(params[0]);
        double lat1 = Double.parseDouble(params[1]);
        double dist = Double.parseDouble(params[2]);
        //`event_id`, `creator_id`, `event_description`, `start_date`, `end_date`, `events.longitude`, `events.latitude`, `user_name`, `image_url`
        String selectedEventItem="`event_id`, `creator_id`, `event_description`, `start_date`, `end_date`, events.longitude, events.latitude, event_img, event_url";
        String selectedCreatorItem=", `user_name`, `image_url`, `creator_type_id`";
        String sql = "SELECT "+selectedEventItem+" "+selectedCreatorItem+" FROM `events`,`user` WHERE creator_id=user_id";
        Connection con = DBConnectionHandler.getConnection();

        JSONObject json = new JSONObject();
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
//            for (int i = 0; i < 2; i++) {
//                ps.setString(i + 1, params[i]);
//            }
            ResultSet rs = ps.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            boolean checkNull = true;
            while (rs.next()) {              
                JSONObject jsonInner = new JSONObject();
                double lon2 = Double.parseDouble(rs.getString(eventkey[5]));
                double lat2 = Double.parseDouble(rs.getString(eventkey[6]));
                if(distance(lat1,  lon1,  lat2,  lon2)<=dist){
                    checkNull = false;
                    for (int i = 0; i < length; i++) {

                        jsonInner.put(eventkey[i], rs.getString(eventkey[i]));
                    }
                    for(int i=0;i<creatorLength;i++){
                        jsonInner.put(creatorNeededKey[i], rs.getString(creatorNeededKey[i]));
                    }
                    jsonArray.add(jsonInner);
                }
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
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
