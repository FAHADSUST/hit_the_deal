package ornob.servlet;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;

import com.mysql.jdbc.Connection;

public class DatabaseConnector {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/hit_the_deal";

	static final String USER = "root";
	static final String PASS = "";

	public static Connection connectToCrmDB() {

		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = (Connection) DriverManager.getConnection(DB_URL, USER,
					PASS);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return connection;
	}

	public static JSONArray makeQuery(PreparedStatement preparedStatement)
			throws Exception {
		ResultSet resultSet = preparedStatement.executeQuery();
		JSONArray resultJson = ResultSetToJSON.convertToJSON(resultSet);
		return resultJson;
	}
}
