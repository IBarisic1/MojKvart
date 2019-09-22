package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import beans.User;
import conn.ConnectionUtils;

public class UserDAO {
	private static User findUserHelper(Connection conn, String username, String password) throws SQLException {
		String getNeighborhoodQuery = "SELECT neighborhood" + " FROM livesIn" + " WHERE username=?";
		PreparedStatement getNeighborhoodPSTM = conn.prepareStatement(getNeighborhoodQuery);
		getNeighborhoodPSTM.setString(1, username);
		String neighborhood = null;
		ResultSet getNeighborhoodRS = getNeighborhoodPSTM.executeQuery();
		if (getNeighborhoodRS.next()) {
			neighborhood = getNeighborhoodRS.getString("neighborhood");
		}

		String getRolesQuery = "SELECT rolename" + " FROM hasRole" + " WHERE username=?";
		PreparedStatement getRolesPSTM = conn.prepareStatement(getRolesQuery);
		getRolesPSTM.setString(1, username);
		ResultSet getRolesRS = getRolesPSTM.executeQuery();
		List<String> roles = new LinkedList<>();
		while (getRolesRS.next()) {
			String role = getRolesRS.getString("rolename");
			roles.add(role);
		}

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setNeighborhood(neighborhood);
		user.setRoles(roles);
		return user;
	}

	public static User findUser(Connection conn, String username, String password) throws SQLException {
		String getUserQuery = "SELECT username, password" + " FROM forumUser" + " WHERE username=? AND password=?;";
		PreparedStatement pstm = conn.prepareStatement(getUserQuery);
		pstm.setString(1, username);
		pstm.setString(2, password);
		ResultSet rs = pstm.executeQuery();
		if (!rs.next()) {
			return null;
		}

		User user = findUserHelper(conn, username, password);
		return user;
	}

	public static User findUser(Connection conn, String username) throws SQLException {
		String getUserQuery = "SELECT password" + " FROM forumUser" + " WHERE username=?;";
		PreparedStatement pstm = conn.prepareStatement(getUserQuery);
		pstm.setString(1, username);
		ResultSet rs = pstm.executeQuery();
		if (!rs.next()) {
			return null;
		}

		String password = rs.getString("password");

		User user = findUserHelper(conn, username, password);
		return user;
	}

	public static void insertUser(Connection conn, User user) throws SQLException {
		String insertUserSQL = "INSERT INTO forumUser(username, password)" + " VALUES (?,?);";
		PreparedStatement insertUserPSTM = conn.prepareStatement(insertUserSQL);
		insertUserPSTM.setString(1, user.getUsername());
		insertUserPSTM.setString(2, user.getPassword());
		insertUserPSTM.executeUpdate();

		boolean isAdmin = user.getRoles().contains("admin");
		if (!isAdmin) {
			String insertNeighborhoodSQL = "INSERT INTO livesIn(username, neighborhood)" + " VALUES (?,?);";
			PreparedStatement insertNeighborhoodPSTM = conn.prepareStatement(insertNeighborhoodSQL);
			insertNeighborhoodPSTM.setString(1, user.getUsername());
			insertNeighborhoodPSTM.setString(2, user.getNeighborhood());
			insertNeighborhoodPSTM.executeUpdate();
		}

		String insertRoleSQL = "INSERT INTO hasRole(username, rolename)" + " VALUES (?,?);";
		PreparedStatement insertRolePSTM = conn.prepareStatement(insertRoleSQL);
		List<String> roles = user.getRoles();
		String username = user.getUsername();
		for (String role : roles) {
			insertRolePSTM.setString(1, username);
			insertRolePSTM.setString(2, role);

			insertRolePSTM.executeUpdate();
		}
	}

	// used for testing, should delete after
	public static void main(String[] args) throws SQLException {
		Connection conn = null;

		List<String> roles = new ArrayList<>();
		roles.add("terminator");
		roles.add("boss");

		User user = new User();
		user.setUsername("Yovi");
		user.setPassword("ivan");
		user.setNeighborhood("kvart");
		user.setRoles(roles);
		try {
			conn = ConnectionUtils.getConnection();
			conn.setAutoCommit(false);
			// insertUser(conn, user);
			User us = findUser(conn, "yovi", "ivan");
			conn.commit();
			System.out.println(us.getUsername() + " " + us.getPassword() + " " + us.getNeighborhood());
			for (String role : us.getRoles()) {
				System.out.println(role);
			}
		} catch (Exception e) {
			ConnectionUtils.rollbackQuietly(conn);
			e.printStackTrace();
		} finally {
			ConnectionUtils.closeQuietly(conn);
		}
	}
}
