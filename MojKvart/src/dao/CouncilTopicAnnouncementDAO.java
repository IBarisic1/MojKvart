package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import beans.CouncilTopicAnnouncement;
import conn.ConnectionUtils;

public class CouncilTopicAnnouncementDAO {
	public static List<CouncilTopicAnnouncement> queryNonExpiredCouncilTopicAnnouncements(Connection conn, String neighborhood) 
			throws SQLException {
		String getAnnouncementsQuery =
				"SELECT cta.id, cta.name, cta.councillor, cta.start, cta.expired"
				+ " FROM councilTopicAnnouncement cta"
				+ " JOIN livesIn"
				+ " ON cta.councillor=livesIn.username"
				+ " WHERE livesIn.neighborhood=? AND cta.expired=false"
				+ " ORDER BY cta.start;";
		
		PreparedStatement pstm = conn.prepareStatement(getAnnouncementsQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		pstm.setString(1, neighborhood);
		ResultSet rs = pstm.executeQuery();
		List<CouncilTopicAnnouncement> announcements = new LinkedList<>();
		
		while(rs.next()) {
			Timestamp start = rs.getTimestamp("start");
			if (isExpired(start)) {
				rs.updateBoolean("expired", true);
				rs.updateRow();
				
				continue;
			}
			
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String councillor = rs.getString("councillor");
			boolean expired = false;
			
			CouncilTopicAnnouncement announcement = new CouncilTopicAnnouncement(id, name, councillor, start, expired);
			announcements.add(announcement);
		}
		
		return announcements;
	}
	
	private static boolean isExpired(Timestamp time) {
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		return time.before(currentTime);
	}
	
	public static void insertCouncilTopicAnnouncement(Connection conn, String name, String councillor, Timestamp start) throws SQLException {
		String insertAnnouncementSQL =
				"INSERT INTO councilTopicAnnouncement(name, councillor, start, expired)"
				+ " VALUES(?,?,?,?);";
		
		boolean expired = isExpired(start);
		
		PreparedStatement pstm = conn.prepareStatement(insertAnnouncementSQL);
		pstm.setString(1, name);
		pstm.setString(2, councillor);
		pstm.setTimestamp(3, start);
		pstm.setBoolean(4, expired);
		
		pstm.executeUpdate();
	}
	
	//used for testing, should delete after
		public static void main(String[] args) {
			Connection conn = null;
			
			try {
				conn = ConnectionUtils.getConnection();
				conn.setAutoCommit(false);
				
				LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
				insertCouncilTopicAnnouncement(conn, "rasprava", "yovi", Timestamp.valueOf(tomorrow));
				
				List<CouncilTopicAnnouncement> nonExp = queryNonExpiredCouncilTopicAnnouncements(conn, "kvart");
				
				System.out.println("Non expired announcements:");
				for (CouncilTopicAnnouncement ann : nonExp) {
					System.out.println(ann.getId() + " " + ann.getName() + " " + ann.getCouncillor() + " " + ann.getStart());
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
