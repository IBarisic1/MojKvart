package beans;

import java.sql.Timestamp;

public class ForumThread {
	private String name;
	private String neighborhood;
	private int nextPostId;
	private String username;
	private Timestamp lastPostTime;
	
	public ForumThread(String name, String neighborhood, int nextPostId, String username, Timestamp lastPostTime) {
		this.name = name;
		this.neighborhood = neighborhood;
		this.nextPostId = nextPostId;
		this.username = username;
		this.setLastPostTime(lastPostTime);
	}
	
	public ForumThread(String name, String neighborhood, String username) {
		this(name, neighborhood, 0, username, null);
	}

	public ForumThread() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public int getNextPostId() {
		return nextPostId;
	}

	public void setNext(int nextPostId) {
		this.nextPostId = nextPostId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Timestamp getLastPostTime() {
		return lastPostTime;
	}

	public void setLastPostTime(Timestamp lastPostTime) {
		this.lastPostTime = lastPostTime;
	}
}
