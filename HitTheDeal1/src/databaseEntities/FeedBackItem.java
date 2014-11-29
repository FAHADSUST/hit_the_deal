package databaseEntities;

public class FeedBackItem {

	int feedback_id, event_id, viewer_id;
	String feedback;
	long date;

	public FeedBackItem(int feedback_id, int event_id, int viewer_id,
			String feedback, long date) {
		super();
		this.feedback_id = feedback_id;
		this.event_id = event_id;
		this.viewer_id = viewer_id;
		this.feedback = feedback;
		this.date = date;
	}

	public int getFeedback_id() {
		return feedback_id;
	}

	public void setFeedback_id(int feedback_id) {
		this.feedback_id = feedback_id;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public int getViewer_id() {
		return viewer_id;
	}

	public void setViewer_id(int viewer_id) {
		this.viewer_id = viewer_id;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

}
