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
@WebServlet(name = "GetFeedbacks", urlPatterns = {"/GetFeedbacks"})
public class GetFeedbacks extends HttpServlet {

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
            out.println("<title>Servlet GetFeedbacks</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetFeedbacks at " + request.getContextPath() + "</h1>");
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

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int event_id = Integer.parseInt(request.getParameter("event_id"));

        JSONArray queryJson = new JSONArray();
        Connection connection = DatabaseConnector.connectToCrmDB();

        if (connection != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_FEEDBACKS);
                preparedStatement.setInt(1, event_id);
                ResultSet resultSet = preparedStatement.executeQuery();
                queryJson = convertToJSON(resultSet, connection, response);
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

    JSONArray convertToJSON(ResultSet resultSet, Connection connection,
            HttpServletResponse response) throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);

            int event_id = obj.getInt("viewer_id");
            // response.getWriter().println(event_id);
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_FEEDBACK_USER);
            preparedStatement.setInt(1, event_id);
            ResultSet resultSet2 = preparedStatement.executeQuery();
            JSONObject ratingObject = new JSONObject();

            resultSet2.next();
            int total_row = resultSet2.getMetaData().getColumnCount();
            for (int i = 0; i < total_row; i++) {
                ratingObject.put(resultSet2.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet2.getObject(i + 1));

            }
            // response.getWriter().println(ratingObject.toString());
            jsonArray.put(ratingObject);
        }
        return jsonArray;
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
