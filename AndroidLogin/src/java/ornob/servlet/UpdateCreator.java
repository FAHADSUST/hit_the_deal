/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ornob.servlet;

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
@WebServlet(name = "UpdateCreator", urlPatterns = {"/UpdateCreator"})
public class UpdateCreator extends HttpServlet {

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
            out.println("<title>Servlet UpdateCreator</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateCreator at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int user_id = Integer.parseInt(request.getParameter("user_id"));
        String user_name = request.getParameter("user_name");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String phn_no = request.getParameter("phn_no");
        double latitude = Double.parseDouble(request.getParameter("latitude"));
        double longitude = Double.parseDouble(request.getParameter("longitude"));
        int creator_type_id = Integer.parseInt(request.getParameter("creator_type_id"));

        JSONArray queryJson = new JSONArray();
        Connection connection = DatabaseConnector.connectToCrmDB();

        if (connection != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(Queries.UPDATE_CREATOR_QUERY);
                preparedStatement.setString(1, user_name);
                preparedStatement.setString(2, address);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, phn_no);
                preparedStatement.setDouble(5, latitude);
                preparedStatement.setDouble(6, longitude);
                preparedStatement.setInt(7, creator_type_id);
                preparedStatement.setInt(8, user_id);

                preparedStatement.executeUpdate();

                queryJson.put(new JSONObject().put("success", true));
                response.getWriter().println(queryJson.toString());
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                queryJson.put(new JSONObject().put("success", "denied"));
                response.getWriter().println(queryJson.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
