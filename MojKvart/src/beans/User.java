package beans;

import java.util.LinkedList;
import java.util.List;

public class User {
	private String username;
	private String password;
	private String neighborhood;

	private List<String> roles;

	public User(String username, String password, String neighborhood, String... roles) {
		this.username = username;
		this.password = password;
		this.neighborhood = neighborhood;

		this.roles = new LinkedList<>();
		if (roles != null) {
			for (String r : roles) {
				this.roles.add(r);
			}
		}
	}

	public User() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
