package beans;

import java.sql.Timestamp;

public class CouncilTopicAnnouncement {
	private int id;
	private String name;
	private String councillor;
	private Timestamp start;
	private boolean expired;

	public CouncilTopicAnnouncement(int id, String name, String councillor, Timestamp start, boolean expired) {
		this.id = id;
		this.name = name;
		this.councillor = councillor;
		this.start = start;
		this.expired = expired;
	}
	
	public CouncilTopicAnnouncement(String name, Timestamp start) {
		this(0, name, null, start, false);
	}

	public CouncilTopicAnnouncement() {
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

	public String getCouncillor() {
		return councillor;
	}

	public void setCouncillor(String councillor) {
		this.councillor = councillor;
	}

	public Timestamp getStart() {
		return start;
	}

	public void setStart(Timestamp start) {
		this.start = start;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}
}
