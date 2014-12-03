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
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author Fahad_G4
 */
@WebServlet(name = "GetSelectedEventDetail", urlPatterns = {"/GetSelectedEventDetail"})
public class GetSelectedEventDetail extends HttpServlet {

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
            out.println("<title>Servlet GetSelectedEventDetail</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetSelectedEventDetail at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    String params[] = new String[2]; 
    Connection con;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();
                  
        response.setContentType("text/html");  
        PrintWriter out = response.getWriter();  
        String eventkey[] = {"event_id", "creator_id", "event_name","event_description", "start_date", "end_date", "longitude", "latitude","event_img","event_url"};
        String creatorNeededKey[]={"user_name","image_url","creator_type_id"};
        
        int length = eventkey.length;
        int creatorLength=creatorNeededKey.length;
        
        params[0]=request.getParameter("event_id");
        params[1]=request.getParameter("viewer_id");
                
        String selectedEventItem="`event_id`, `creator_id`, event_name,`event_description`, `start_date`, `end_date`, events.longitude, events.latitude, event_img, event_url";
        String selectedCreatorItem=", `user_name`, `image_url`, `creator_type_id`";
        String sql = "SELECT "+selectedEventItem+" "+selectedCreatorItem+" FROM `events`,`user` WHERE creator_id=user_id and event_id=?";
        con = DBConnectionHandler.getConnection();
         
        try {
            PreparedStatement ps = con.prepareStatement(sql);            
            ps.setString(1, params[0]);
                       
            ResultSet rs = ps.executeQuery();
            
            JSONObject jsonInner = new JSONObject();
            boolean checkNull = true;
            if(rs.next()) {
                checkNull = false;                
                for(int i=0;i<length;i++){
                    
                    jsonInner.put(eventkey[i], rs.getString(eventkey[i]));
                } 
                for(int i=0;i<creatorLength;i++){
                    jsonInner.put(creatorNeededKey[i], rs.getString(creatorNeededKey[i]));
                }                                                            
                //json.put("info", "success");
            }
            JSONObject jsonObjectFeedItems = getAllFeedForThisEvent();  
            JSONObject jsonObjectRatingDetail = getEventRatingDetail();  
            
                //json.put("info", "success");
            
            
            if(!checkNull){
                json.put("success", "1");
                json.put("myViwerRating",myViwerRating());
                json.put("eventDetail", jsonInner);
                json.put("feedDetail",jsonObjectFeedItems);
                json.put("ratingDetail",jsonObjectRatingDetail);
            }
            else json.put("success", "0");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private JSONObject getAllFeedForThisEvent() {
        String feedBackkey[] = {"feedback_id","event_id","viewer_id","feedback","date"};
        int length = feedBackkey.length;
        String feedBackUserkey[] = {"user_name","image_url"};
        int userLength=feedBackUserkey.length;
        
        
        String feedBack = "`feedback_id`, `event_id`, `viewer_id`, `feedback`, `date`";
        String userNedd = ", user_name, image_url";

        String sql = "SELECT "+feedBack+" "+userNedd+" FROM `feedback`,user  WHERE viewer_id=user_id and event_id=?";

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

                    jsonInner.put(feedBackkey[i], rs.getString(feedBackkey[i]));
                }
                for (int i = 0; i < userLength; i++) {

                    jsonInner.put(feedBackUserkey[i], rs.getString(feedBackUserkey[i]));
                }
                
                jsonArray.put(jsonInner);

            }
            if (!checkNull) {              
                json.put("allFeedBack", jsonArray);
                json.put("feedBackSuccess", "1");
            } else {
                json.put("feedBackSuccess", "0");
            }
            return json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    private JSONObject getEventRatingDetail() {
        String ratingkey[] = {"rating_id","event_id","viewer_id","rating"};
        int length = ratingkey.length;              
        String sql = "SELECT rating FROM `rating` WHERE event_id=?";
        Connection con = DBConnectionHandler.getConnection();
        JSONObject json = new JSONObject();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, params[0]);
            ResultSet rs = ps.executeQuery();

            
            boolean checkNull = true;
            int count=0;
            double reting=0;
            while (rs.next()) {
                checkNull = false;               
                reting += rs.getInt("rating");
                count++;               
            }
            if(count!=0)
                reting=reting/count;             
            if (!checkNull) {
                json.put("rating", reting);
                json.put("countNumber", count);
            } else {
                json.put("rating", "0");
                json.put("countNumber", 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
    
    private int myViwerRating() {
        String sql = "SELECT rating FROM `rating` WHERE event_id=? and  viewer_id=?";
        con = DBConnectionHandler.getConnection();
         
        try {           
            PreparedStatement ps = con.prepareStatement(sql);
           
            ps.setString(1, params[0]);
            ps.setString(2, params[1]);
                      
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                
                return rs.getInt("rating");
                
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
}