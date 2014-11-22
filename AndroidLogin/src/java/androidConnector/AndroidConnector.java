/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package androidConnector;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import databaseManipulators.GetEventsWithInRadius;

/**
 * Servlet implementation class AndroidConnector
 */
@WebServlet("/AndroidConnector")
public class AndroidConnector extends HttpServlet {
	private static final long serialVersionUID = 1L;

	JSONArray dataArray;
	int count;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AndroidConnector() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().println("I am Ornob.");
                
                dataArray = new JSONArray();
		try {
			dataArray = new GetEventsWithInRadius().getData(
					Double.parseDouble((request.getParameter("latitude")
							.toString())), Double.parseDouble((request
							.getParameter("longitude").toString())));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().println(dataArray.toString());
		// response.getWriter().println(request.getParameter("name"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
	}
}
