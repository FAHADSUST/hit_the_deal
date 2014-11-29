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
@WebServlet(name = "IGDFavourite", urlPatterns = {"/IGDFavourite"})
public class IGDFavourite extends HttpServlet {

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
            out.println("<title>Servlet IGDFavourite</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet IGDFavourite at " + request.getContextPath() + "</h1>");
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
        
        
        int viewer_id = Integer.parseInt(request.getParameter("viewer_id"));
		int creator_id = Integer.parseInt(request.getParameter("creator_id"));
		int option = Integer.parseInt(request.getParameter("option"));

		JSONArray queryJson = new JSONArray();
		Connection connection = DatabaseConnector.connectToCrmDB();

		if (connection != null) {
			try {
				if (option == 1) {
					PreparedStatement preparedStatement = connection
							.prepareStatement(Queries.INSERT_FAVOURITE);
					preparedStatement.setInt(1, viewer_id);
					preparedStatement.setInt(2, creator_id);

					preparedStatement.executeUpdate();
					queryJson.put(new JSONObject().put("success", true));
					response.getWriter().println(queryJson.toString());
				} else if (option == 2) {
					PreparedStatement preparedStatement = connection
							.prepareStatement(Queries.GET_FAVOURITE);
					preparedStatement.setInt(1, viewer_id);
					preparedStatement.setInt(2, creator_id);

					ResultSet resultSet = preparedStatement.executeQuery();
					if (resultSet.isBeforeFirst()) {
						queryJson.put(new JSONObject().put("success", true));
						response.getWriter().println(queryJson.toString());
					} else {
						queryJson.put(new JSONObject().put("success", false));
						response.getWriter().println(queryJson.toString());
					}
				} else if (option == 3) {
					PreparedStatement preparedStatement = connection
							.prepareStatement(Queries.DELETE_FAVOURITE);
					preparedStatement.setInt(1, viewer_id);
					preparedStatement.setInt(2, creator_id);

					preparedStatement.executeUpdate();
					queryJson.put(new JSONObject().put("success", true));
					response.getWriter().println(queryJson.toString());
				}

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
        int viewer_id = Integer.parseInt(request.getParameter("viewer_id"));
		int creator_id = Integer.parseInt(request.getParameter("creator_id"));
		int option = Integer.parseInt(request.getParameter("option"));

		JSONArray queryJson = new JSONArray();
		Connection connection = DatabaseConnector.connectToCrmDB();

		if (connection != null) {
			try {
				if (option == 1) {
					PreparedStatement preparedStatement = connection
							.prepareStatement(Queries.INSERT_FAVOURITE);
					preparedStatement.setInt(1, viewer_id);
					preparedStatement.setInt(2, creator_id);

					preparedStatement.executeUpdate();
					queryJson.put(new JSONObject().put("success", true));
					response.getWriter().println(queryJson.toString());
				} else if (option == 2) {
					PreparedStatement preparedStatement = connection
							.prepareStatement(Queries.GET_FAVOURITE);
					preparedStatement.setInt(1, viewer_id);
					preparedStatement.setInt(2, creator_id);

					ResultSet resultSet = preparedStatement.executeQuery();
					if (resultSet.isBeforeFirst()) {
						queryJson.put(new JSONObject().put("success", true));
						response.getWriter().println(queryJson.toString());
					} else {
						queryJson.put(new JSONObject().put("success", false));
						response.getWriter().println(queryJson.toString());
					}
				} else if (option == 3) {
					PreparedStatement preparedStatement = connection
							.prepareStatement(Queries.DELETE_FAVOURITE);
					preparedStatement.setInt(1, viewer_id);
					preparedStatement.setInt(2, creator_id);

					preparedStatement.executeUpdate();
					queryJson.put(new JSONObject().put("success", true));
					response.getWriter().println(queryJson.toString());
				}

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
