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
import beans.Post;
import conn.ConnectionUtils;

public class PostDAO {
	public static List<Post> queryPost(Connection conn, String thread, String neighborhood)
			throws SQLException {
		String getPostsQuery =
				"SELECT id, username, text, creationTime"
				+ " FROM post"
				+ " WHERE neighborhood=? AND thread=?"
				+ " ORDER BY id;";
		
		PreparedStatement pstm = conn.prepareStatement(getPostsQuery);
		pstm.setString(1, neighborhood);
		pstm.setString(2, thread);
		ResultSet rs = pstm.executeQuery();
		List<Post> posts = new LinkedList<>();
		
		while (rs.next()) {
			int id = rs.getInt("id");
			String username = rs.getString("username");
			String text = rs.getString("text");
			Timestamp creationTime = rs.getTimestamp("creationTime");
			
			Post post = new Post(id, thread, neighborhood, username, text, creationTime);
			posts.add(post);
		}
		
		return posts;
	}

	public static Post findPost(Connection conn, int id, String thread, String neighborhood) 
			throws SQLException {
		String getPostsQuery =
				"SELECT username, text, creationTime"
				+ " FROM post"
				+ " WHERE id=? AND neighborhood=? AND thread=?;";
		
		PreparedStatement pstm = conn.prepareStatement(getPostsQuery);
		pstm.setInt(1, id);
		pstm.setString(2, neighborhood);
		pstm.setString(3, thread);
		ResultSet rs = pstm.executeQuery();
		
		if (rs.next()) {
			String username = rs.getString("username");
			String text = rs.getString("text");
			Timestamp creationTime = rs.getTimestamp("creationTime");
			Post post = new Post(id, thread, neighborhood, username, text, creationTime);
			return post;
		}
		
		return null;
	}
	
	public static void insertPost(Connection conn, String thread, String neighborhood, String username, String text) 
			throws SQLException {
		String insertPostSQL =
				"INSERT INTO post(id, thread, neighborhood, username, text)"
				+ " VALUES (?,?,?,?,?);";
		
		ForumThread th = ForumThreadDAO.findThread(conn, thread, neighborhood);
		int id = th.getNextPostId();
		
		PreparedStatement insertPostPSTM = conn.prepareStatement(insertPostSQL);
		insertPostPSTM.setInt(1, id);
		insertPostPSTM.setString(2, thread);
		insertPostPSTM.setString(3, neighborhood);
		insertPostPSTM.setString(4, username);
		insertPostPSTM.setString(5, text);
		
		insertPostPSTM.executeUpdate();
		
		String updateThreadSQL =
				"UPDATE thread"
				+ " SET nextPostId=?, lastPostTime=?"
				+ " WHERE name=? AND neighborhood=?;";
		
		PreparedStatement updateThreadPSTM = conn.prepareStatement(updateThreadSQL);
		updateThreadPSTM.setInt(1, th.getNextPostId() + 1);
		Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
		updateThreadPSTM.setTimestamp(2, currentTime);
		updateThreadPSTM.setString(3, thread);
		updateThreadPSTM.setString(4, neighborhood);
		
		updateThreadPSTM.executeUpdate();
	}
	
	public static void updatePost(Connection conn, Post post) throws SQLException {
		String updatePostSQL =
				"UPDATE post"
				+ " SET text=?"
				+ " WHERE id=? AND neighborhood=? AND thread=?;";
		
		PreparedStatement pstm = conn.prepareStatement(updatePostSQL);
		pstm.setString(1, post.getText());
		pstm.setInt(2, post.getId());
		pstm.setString(3, post.getNeighborhood());
		pstm.setString(4, post.getThread());
		
		pstm.executeUpdate();
	}
	
	public static void deletePost(Connection conn, int id, String neighborhood, String thread) 
			throws SQLException {
		String deletePostSQL =
				"DELETE FROM post"
				+ " WHERE id=? AND neighborhood=? AND thread=?;";
		
		PreparedStatement deletePostPSTM = conn.prepareStatement(deletePostSQL);
		deletePostPSTM.setInt(1, id);
		deletePostPSTM.setString(2, neighborhood);
		deletePostPSTM.setString(3, thread);
		
		deletePostPSTM.executeUpdate();
	}
	
	//used for testing, should delete after
	public static void main(String[] args) throws SQLException {
		Connection conn = null;

		//ForumThread thread = new ForumThread("wat", "kvart", 0, "yovi");
		try {
			conn = ConnectionUtils.getConnection();
			conn.setAutoCommit(false);
			//insertPost(conn, "o zivote", "kvart", "yovi", "jos jedna hehe", Timestamp.valueOf(LocalDateTime.now()));
			//Post post = new Post(5, "o zivote", "kvart", "yovi", "a kaj sad");
			//updatePost(conn, post);
			//deletePost(conn, 4, "kvart", "o zivote");
			//List<Post> list = queryPost(conn, "o zivote", "kvart");
			//Post n = findPost(conn, 6, "o zivote", "kvart");
			conn.commit();
			//for (Post n : list) {
			//	System.out.println(n.getId() + " " + n.getNeighborhood() + " " + n.getThread() + " " + n.getUsername() + " Text: " + n.getText() + " Creation time: " + n.getCreationTime());
			//}
			//System.out.println(n.getId() + " " + n.getNeighborhood() + " " + n.getThread() + " " + n.getUsername() + " Text: " + n.getText() + " Creation time: " + n.getCreationTime());
		} catch(Exception e) {
			ConnectionUtils.rollbackQuietly(conn);
			e.printStackTrace();
		} finally {
			ConnectionUtils.closeQuietly(conn);
		}
	}
}
