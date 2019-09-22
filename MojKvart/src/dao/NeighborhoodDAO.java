package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import beans.Neighborhood;
import conn.ConnectionUtils;

public class NeighborhoodDAO {
	public static List<Neighborhood> queryNeighborhood(Connection conn) throws SQLException {	
		String getNeighborhoodsQuery =
				"SELECT name"
				+ " FROM neighborhood"
				+ " ORDER BY name;";
		
		Statement statement = conn.createStatement();	
		ResultSet rs = statement.executeQuery(getNeighborhoodsQuery);
		List<Neighborhood> neighborhoods = new LinkedList<>();
		
		while (rs.next()) {
			String name = rs.getString("name");
			Neighborhood neighborhood = new Neighborhood(name);
			neighborhoods.add(neighborhood);
		}
		
		return neighborhoods;
	}
	
	public static Neighborhood findNeighborhood(Connection conn, String name) throws SQLException {
		String findNeighborhoodQuery =
				"SELECT name"
				+ " FROM neighborhood"
				+ " WHERE name=?";
		
		PreparedStatement pstm = conn.prepareStatement(findNeighborhoodQuery);
		pstm.setString(1, name);
		ResultSet rs = pstm.executeQuery();
		
		if (rs.next()) {
			Neighborhood neighborhood = new Neighborhood(name);
			return neighborhood;
		}
		
		return null;
	}
	
	public static void insertNeighborhood(Connection conn, Neighborhood neighborhood) throws SQLException {
		String insertNeighborhoodSQL = 
				"INSERT INTO neighborhood(name)"
				+ " VALUES (?);";
		
		PreparedStatement pstm = conn.prepareStatement(insertNeighborhoodSQL);
		pstm.setString(1, neighborhood.getName());
		pstm.executeUpdate();
	}
	
	//used for testing, should delete after
	public static void main(String[] args) throws SQLException {
		Connection conn = null;
		
		//Neighborhood hood = new Neighborhood("Dugave Republika");
		try {
			conn = ConnectionUtils.getConnection();
			conn.setAutoCommit(false);
			//insertNeighborhood(conn, hood);
			List<Neighborhood> list = queryNeighborhood(conn);
			conn.commit();
			for (Neighborhood n : list) {
				System.out.println(n.getName());
			}
		} catch(Exception e) {
			ConnectionUtils.rollbackQuietly(conn);
			e.printStackTrace();
		} finally {
			ConnectionUtils.closeQuietly(conn);
		}
	}
}
