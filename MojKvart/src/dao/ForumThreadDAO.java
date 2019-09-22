package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import beans.ForumThread;
import conn.ConnectionUtils;

public class ForumThreadDAO {
	public static List<ForumThread> queryThread(Connection conn, String neighborhood) throws SQLException {
		String getThreadsQuery =
				"SELECT name, nextPostId, username, lastPostTime"
				+ " FROM thread"
				+ " WHERE neighborhood=?"
				+ " ORDER BY lastPostTime DESC;";
		
		PreparedStatement pstm = conn.prepareStatement(getThreadsQuery);
		pstm.setString(1, neighborhood);
		ResultSet rs = pstm.executeQuery();
		List<ForumThread> threads = new LinkedList<>();
		
		while (rs.next()) {
			String name = rs.getString("name");
			int nextPostId = rs.getInt("nextPostId");
			String username = rs.getString("username");
			Timestamp lastPostTime = rs.getTimestamp("lastPostTime");
			
			ForumThread thread = new ForumThread(name, neighborhood, nextPostId, username, lastPostTime);
			threads.add(thread);
		}
		
		return threads;
	}
	
	public static ForumThread findThread(Connection conn, String thread, String neighborhood) throws SQLException {
		String getThreadsQuery =
				"SELECT nextPostId, username, lastPostTime"
				+ " FROM thread"
				+ " WHERE neighborhood=? AND name=?";
		
		PreparedStatement pstm = conn.prepareStatement(getThreadsQuery);
		pstm.setString(1, neighborhood);
		pstm.setString(2, thread);
		ResultSet rs = pstm.executeQuery();
		
		if (rs.next()) {
			int nextPostId = rs.getInt("nextPostId");
			String username = rs.getString("username");
			Timestamp lastPostTime = rs.getTimestamp("lastPostTime");
			
			ForumThread th = new ForumThread(thread, neighborhood, nextPostId, username, lastPostTime);
			return th;
		}
		
		return null;
	}

	public static void insertThread(Connection conn, ForumThread thread) throws SQLException {
		String insertThreadSQL =
				"INSERT INTO thread(name, neighborhood, nextPostId, username)"
				+ " VALUES (?,?,?,?);";
		
		PreparedStatement pstm = conn.prepareStatement(insertThreadSQL);
		pstm.setString(1, thread.getName());
		pstm.setString(2, thread.getNeighborhood());
		pstm.setInt(3, thread.getNextPostId());
		pstm.setString(4, thread.getUsername());
		pstm.executeUpdate();
	}
	
	public static void updateLastPostTime(Connection conn, 
			String thread, String neighborhood, Timestamp lastPostTime) 
					throws SQLException {
		String updateThreadSQL =
				"UPDATE thread"
				+ " SET lastposttime=?"
				+ " WHERE name=? AND neighborhood=?;";
		
		PreparedStatement pstm = conn.prepareStatement(updateThreadSQL);
		pstm.setTimestamp(1, lastPostTime);
		pstm.setString(2, thread);
		pstm.setString(3, neighborhood);
		pstm.executeUpdate();
	}
	
	//used for testing, should delete after
		public static void main(String[] args) throws SQLException {
			Connection conn = null;
			
			//ForumThread thread = new ForumThread("yolo", "kvart", 0, "mod1kvart");
			try {
				conn = ConnectionUtils.getConnection();
				conn.setAutoCommit(false);
				//insertThread(conn, thread);
				String hood = "kvart";
				updateLastPostTime(conn, "wat", hood, Timestamp.valueOf(LocalDateTime.now()));
				List<ForumThread> list = queryThread(conn, hood);
				//ForumThread n = findThread(conn, "wat", "kvart");
				conn.commit();
				//System.out.println(n.getName() + " " + n.getNeighborhood() + " " + n.getNextPostId() + " " + n.getUsername() + " " + n.getLastPostTime());
				for (ForumThread n : list) {
					System.out.println(n.getName() + " " + n.getNeighborhood() + " " + n.getNextPostId() + " " + n.getUsername() + " " + n.getLastPostTime());
				}
			} catch(Exception e) {
				ConnectionUtils.rollbackQuietly(conn);
				e.printStackTrace();
			} finally {
				ConnectionUtils.closeQuietly(conn);
			}
		}
}
