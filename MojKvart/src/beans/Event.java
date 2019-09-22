package beans;

import java.sql.Timestamp;

public class Event {
	private int id;
	private String name;
	private String place;
	private Timestamp start;
	private String duration;
	private String description;
	private String username;
	private boolean accepted;
	private boolean expired;

	public Event(int id, String name, String place, Timestamp start, String duration, String description,
			String username, boolean accepted, boolean expired) {
		this.id = id;
		this.name = name;
		this.place = place;
		this.start = start;
		this.duration = duration;
		this.description = description;
		this.username = username;
		this.accepted = accepted;
		this.expired = expired;
	}

	public Event(String name, String place, Timestamp start, String duration, String description, String username) {
		this(0, name, place, start, duration, description, username, false, false);
	}
	
	public Event(int id, String name, String place, Timestamp start, String duration, String description, String username) {
		this(id, name, place, start, duration, description, username, false, false);
	}

	public Event() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Timestamp getStart() {
		return start;
	}

	public void setStart(Timestamp start) {
		this.start = start;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}
}
