package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import conn.ConnectionUtils;

public class PermissionDAO {
	public static List<String> queryPermissionsForRole(Connection conn, String role) throws SQLException {
		String getPermissionsQuery =
				"SELECT url"
				+ " FROM hasPermission"
				+ " WHERE rolename=?;";
		
		PreparedStatement pstm = conn.prepareStatement(getPermissionsQuery);
		pstm.setString(1, role);
		ResultSet rs = pstm.executeQuery();
		List<String> urls = new LinkedList<>();
		
		while (rs.next()) {
			String url = rs.getString("url");
			urls.add(url);
		}
		
		return urls;
	}
	
	//used for testing, should delete after
	public static void main(String[] args) {
		Connection conn = null;
		
		try {
			conn = ConnectionUtils.getConnection();
			conn.setAutoCommit(false);
			List<String> permissions = queryPermissionsForRole(conn, "admin");
					
			for (String permission : permissions) {
				System.out.println(permission);
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
