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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author Fahad_G4
 */
@WebServlet(name = "InsertEventFeedBack", urlPatterns = {"/InsertEventFeedBack"})
public class InsertEventFeedBack extends HttpServlet {

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
            out.println("<title>Servlet InsertEventFeedBack</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InsertEventFeedBack at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }
    String params[];
    Connection con;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JSONObject json = new JSONObject();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String typeFlag = "setFlag";
        String typeGetOrSet = request.getParameter(typeFlag);

        String feedBackkey[] = {"event_id", "viewer_id", "feedback", "date"};
        int length = feedBackkey.length;
        params = new String[length];
        for (int i = 0; i < length; i++) {
            params[i] = request.getParameter(feedBackkey[i]);
        }
        con = DBConnectionHandler.getConnection();
        
        if (typeGetOrSet.equals("1")) {
            
            String sql = "INSERT INTO `hit_the_deal`.`feedback` (`feedback_id`, `event_id`, `viewer_id`, `feedback`, `date`) VALUES (NULL, ?, ?, ?, ?)";
            

            try {
                System.out.println("dssdsfdf");
                PreparedStatement ps = con.prepareStatement(sql);
                for (int i = 0; i < length; i++) {
                    ps.setString(i + 1, params[i]);
                }
                int rsInt = ps.executeUpdate();
                if (rsInt != 0) {
                    JSONObject jsonObj = getAllFeedForThisEvent();

                    json.put("success", "1");
                    json.put("feedDetail", jsonObj);
                } else {
                    json.put("success", "0");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (typeGetOrSet.equals("2")) {
            try {
                JSONObject jsonObj = getAllFeedForThisEvent();
                json.put("success", "1");
                json.put("feedDetail", jsonObj);
            } catch (JSONException ex) {
                Logger.getLogger(InsertEventFeedBack.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //System.out.println(json);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String typeFlag = "setFlag";
        String typeGetOrSet = request.getParameter(typeFlag);

        String feedBackkey[] = {"event_id", "viewer_id", "feedback", "date"};
        int length = feedBackkey.length;
        params = new String[length];
        for (int i = 0; i < length; i++) {
            params[i] = request.getParameter(feedBackkey[i]);
        }
        con = DBConnectionHandler.getConnection();
        
        if (typeGetOrSet.equals("1")) {
            
            String sql = "INSERT INTO `hit_the_deal`.`feedback` (`feedback_id`, `event_id`, `viewer_id`, `feedback`, `date`) VALUES (NULL, ?, ?, ?, ?)";
            

            try {
                System.out.println("dssdsfdf");
                PreparedStatement ps = con.prepareStatement(sql);
                for (int i = 0; i < length; i++) {
                    ps.setString(i + 1, params[i]);
                }
                int rsInt = ps.executeUpdate();
                if (rsInt != 0) {
                    JSONObject jsonObj = getAllFeedForThisEvent();

                    json.put("success", "1");
                    json.put("feedDetail", jsonObj);
                } else {
                    json.put("success", "0");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (typeGetOrSet.equals("2")) {
            try {
                JSONObject jsonObj = getAllFeedForThisEvent();
                json.put("success", "1");
                json.put("feedDetail", jsonObj);
           } catch (Exception e) {
                e.printStackTrace();
            }
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

    private JSONObject getAllFeedForThisEvent() {
        String feedBackkey[] = {"feedback_id", "event_id", "viewer_id", "feedback", "date"};
        int length = feedBackkey.length;
        String feedBackUserkey[] = {"user_name", "image_url"};
        int userLength = feedBackUserkey.length;


        String feedBack = "`feedback_id`, `event_id`, `viewer_id`, `feedback`, `date`";
        String userNedd = ", user_name, image_url";

        String sql = "SELECT " + feedBack + " " + userNedd + " FROM `feedback`,user  WHERE viewer_id=user_id and event_id=?";

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
            } else {
                json.put("allFeedBack", "0");
            }
            return json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
