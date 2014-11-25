package databaseManipulators;

import java.sql.DriverManager;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import distanceMeasurement.Coordinate;
import distanceMeasurement.DegreeCoordinate;
import distanceMeasurement.EarthCalc;
import distanceMeasurement.Point;

public class GetEventsWithInRadius {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/hit_the_deal";

	static final String USER = "root";
	static final String PASS = "";

	Connection connection = null;
	Statement statement = null;
	Double latitude, longitude;
	JSONArray filteredEvents;
	int count;
	int x[] = new int[3];

	public GetEventsWithInRadius() {
		filteredEvents = new JSONArray();
		this.latitude = latitude;
		this.longitude = longitude;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = (Connection) DriverManager.getConnection(DB_URL, USER,
					PASS);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public JSONArray getData(double latitude, double longitude)
			throws Exception {
		this.latitude = latitude;
		this.longitude = longitude;
		count = 0;
		if (connection != null) {
			statement = (Statement) connection.createStatement();
			String sql = "SELECT * FROM events";
			ResultSet resultSet = statement.executeQuery(sql);
			JSONArray jsonArray = ResultSetToJSON.convertToJSON(resultSet);
			filterEvents(jsonArray);
			statement.close();
			connection.close();
			filteredEvents.put(new JSONObject().put("success", true));
			return filteredEvents;
		}

		else {
			filteredEvents.put(new JSONObject().put("success", false));
			return filteredEvents;
		}
	}

	void filterEvents(JSONArray array) throws JSONException {
		for (int i = 0; i < array.length(); i++) {
			JSONObject eventJson = (JSONObject) array.get(i);
			Double toLatitude = eventJson.getDouble("latitude");
			Double toLongitude = eventJson.getDouble("longitude");
			if ((int) getDistance(latitude, longitude, toLatitude, toLongitude) <= 2000) {

				count++;
				filteredEvents.put(eventJson);
			}
		}
	}

	private double getDistance(double fromLatitude, double fromLongitude,
			double toLatitude, double toLongitude) {

		Coordinate lat = new DegreeCoordinate(fromLatitude);
		Coordinate lng = new DegreeCoordinate(fromLongitude);
		Point kew = new Point(lat, lng);

		// Richmond, London
		lat = new DegreeCoordinate(toLatitude);
		lng = new DegreeCoordinate(toLongitude);
		Point richmond = new Point(lat, lng);

		double distance = EarthCalc.getDistance(richmond, kew); // in meters

		return distance;
	}

	private double deg2rad(double deg) {

		return (deg * Math.PI / 180.0);

	}

	private double rad2deg(double rad) {

		return (rad * 180 / Math.PI);

	}
}
