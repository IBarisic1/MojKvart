package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import conn.ConnectionUtils;

public class RoleDAO {
	public static List<String> queryRole(Connection conn) throws SQLException {		
		String getRolesQuery =
				"SELECT rolename"
				+ " FROM role;";
		
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(getRolesQuery);
		List<String> roles = new LinkedList<>();
		
		while (rs.next()) {
			String role = rs.getString("rolename");
			roles.add(role);
		}
		
		return roles;
	}
	
	//used for testing, should delete after
		public static void main(String[] args) {
			Connection conn = null;
			
			try {
				conn = ConnectionUtils.getConnection();
				conn.setAutoCommit(false);
				List<String> roles = queryRole(conn);
				
				for (String role : roles) {
					System.out.println(role);
				}
				conn.commit();
			} catch (Exception e) {
				ConnectionUtils.rollbackQuietly(conn);
				e.printStackTrace();
			} finally {
				ConnectionUtils.closeQuietly(conn);
			}
		}
}
