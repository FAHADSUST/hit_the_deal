package ornob.servlet;

public class Queries {
	public static final String UPDATE_CREATOR_QUERY = "UPDATE user SET user_name = ?, address = ?, email = ?, phn_no = ?, latitude = ?, longitude = ?, creator_type_id = ? WHERE user_id = ?";
	public static final String CREATE_EVENT_QUERY = "INSERT INTO events (creator_id, event_name, event_description, start_date, end_date, latitude, longitude, event_img, event_url) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String GET_EVENT_BY__USER = "SELECT * FROM events WHERE creator_id = ? ORDER BY start_date DESC, end_date ASC";
	public static final String GET_RATING = "SELECT event_id , count(rating) as rating_count, avg(rating) as rating FROM rating WHERE event_id=? group by event_id";
	public static final String GET_FEEDBACKS = "SELECT * FROM feedback WHERE event_id=? ORDER BY date ASC";
	public static final String GET_FEEDBACK_USER = "SELECT user_id, user_name, image_url From user WHERE user_id=?";
	public static final String INSERT_FEEDBACK = "INSERT INTO feedback(event_id, viewer_id, feedback, date) VALUES(?, ?, ?, ?);";
	public static final String UPDATE_EVENT_QUERY = "UPDATE events SET creator_id = ?, event_name = ?, event_description = ?, start_date = ?, end_date = ?, latitude = ?, longitude = ?, event_img=?, event_url=? WHERE event_id = ?";
	public static final String GET_FAVOURITE = "SELECT viewer_id, creator_id FROM favourite WHERE viewer_id = ? AND creator_id = ?";
	public static final String INSERT_FAVOURITE = "INSERT INTO favourite(viewer_id, creator_id) VALUES(?, ?)";
	public static final String DELETE_FAVOURITE = "DELETE FROM favourite WHERE viewer_id = ? AND creator_id = ?";
	public static final String GET_CREATOR_DETAILS = "SELECT * FROM user WHERE user_id = ?";
}
