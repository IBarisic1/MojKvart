package beans;

import java.sql.Timestamp;

public class Post {
	private int id;
	private String thread;
	private String neighborhood;
	private String username;
	private String text;
	private Timestamp creationTime;

	public Post(int id, String thread, String neighborhood, String username, String text, Timestamp creationTime) {
		this.id = id;
		this.thread = thread;
		this.neighborhood = neighborhood;
		this.username = username;
		this.text = text;
		this.creationTime = creationTime;
	}

	public Post(int id, String thread, String neighborhood, String username, String text) {
		this(id, thread, neighborhood, username, text, null);
	}

	public Post() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getThread() {
		return thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}
}
