/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ornob.servlet;

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
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
@WebServlet(name = "CreateEvent", urlPatterns = {"/CreateEvent"})
public class CreateEvent extends HttpServlet {

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
            out.println("<title>Servlet CreateEvent</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateEvent at " + request.getContextPath() + "</h1>");
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
        //processRequest(request, response);
       
		int creator_id = Integer.parseInt(request.getParameter("creator_id"));
		String event_name = request.getParameter("event_name");
		String event_description = request.getParameter("event_description");
		long start_date = Long.parseLong(request.getParameter("start_date"));
		long end_date = Long.parseLong(request.getParameter("end_date"));
		double latitude = Double.parseDouble(request.getParameter("latitude"));
		double longitude = Double
				.parseDouble(request.getParameter("longitude"));
		String event_img = request.getParameter("event_img");
		String event_url = request.getParameter("event_url");

		JSONArray queryJson = new JSONArray();
		Connection connection = DatabaseConnector.connectToCrmDB();

		if (connection != null) {
			try {
				PreparedStatement preparedStatement = connection
						.prepareStatement(Queries.CREATE_EVENT_QUERY);
				preparedStatement.setInt(1, creator_id);
				preparedStatement.setString(2, event_name);
				preparedStatement.setString(3, event_description);
				preparedStatement.setLong(4, start_date);
				preparedStatement.setLong(5, end_date);
				preparedStatement.setDouble(6, latitude);
				preparedStatement.setDouble(7, longitude);
				preparedStatement.setString(8, event_img);
				preparedStatement.setString(9, event_url);

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
