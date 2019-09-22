package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import beans.Event;
import conn.ConnectionUtils;

public class EventDAO {
	private static List<Event> queryEvent(Connection conn, boolean accepted, boolean expired, String neighborhood) 
			throws SQLException {
		String getEventsQuery =
				"SELECT expired, id, name, place, start, duration, description, event.username"
				+ " FROM event"
				+ " JOIN livesIn"
				+ " ON event.username=livesIn.username"
				+ " WHERE accepted=? AND neighborhood=?"
				+ " ORDER BY start;";
		
		PreparedStatement pstm = conn.prepareStatement(getEventsQuery,
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		pstm.setBoolean(1, accepted);
		pstm.setString(2, neighborhood);
		ResultSet rs = pstm.executeQuery();
		List<Event> events = new LinkedList<>();
		
		while(rs.next()) {
			boolean exp = rs.getBoolean("expired");
			
			// if we're searching for non-expired events and the current event has already expired
			if (exp == true && expired == false) {
				continue;
			}
			
			Timestamp start = rs.getTimestamp("start");
			
			//check if the event has expired since the last query
			if (!exp) {
				exp = isExpired(start);
				if (exp) {
					rs.updateBoolean("expired", true);
					rs.updateRow();
				}
			}
			
			if (exp != expired) {
				continue;
			}
			
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String place = rs.getString("place");
			String duration = rs.getString("duration");
			String description = rs.getString("description");
			String username = rs.getString("username");
			
			Event event = new Event(id, name, place, start, duration, description, username, accepted, expired);
			events.add(event);
		}
		
		return events;
	}
	
	private static boolean isExpired(Timestamp time) {
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		return time.before(currentTime);
	}
	
	public static List<Event> queryAcceptedNonExpiredEvents(Connection conn, String neighborhood) throws SQLException {
		boolean accepted = true;
		boolean expired = false;
		List<Event> acceptedEvents = queryEvent(conn, accepted, expired, neighborhood);
		return acceptedEvents;
	}
	
	public static List<Event> queryPendingNonExpiredEvents(Connection conn, String neighborhood) throws SQLException {
		boolean accepted = false;
		boolean expired = false;
		List<Event> pendingEvents = queryEvent(conn, accepted, expired, neighborhood);
		return pendingEvents;
	}
	
	public static List<Event> queryAcceptedExpiredEvents(Connection conn, String neighborhood) throws SQLException {
		boolean accepted = true;
		boolean expired = true;
		List<Event> expiredEvents = queryEvent(conn, accepted, expired, neighborhood);
		return expiredEvents;
	}
	
	public static Event findEvent(Connection conn, int id) throws SQLException {
		String getEventQuery =
				"SELECT name, place, start, duration, description, username"
				+ " FROM event"
				+ " WHERE id=?;";
		
		PreparedStatement pstm = conn.prepareStatement(getEventQuery);
		pstm.setInt(1, id);
		ResultSet rs = pstm.executeQuery();
		
		if (rs.next()) {
			String name = rs.getString("name");
			String place = rs.getString("place");
			Timestamp start = rs.getTimestamp("start");
			String duration = rs.getString("duration");
			String description = rs.getString("description");
			String username = rs.getString("username");
			
			Event event = new Event(id, name, place, start, duration, description, username);
			return event;
		}
		
		return null;
	}
	
	public static void insertEvent(Connection conn, String name, String place, 
			Timestamp start, String duration, String description, String username, 
			boolean accepted) throws SQLException {
		String insertEventSQL =
				"INSERT INTO event(name, place, start, duration, description, username, accepted, expired)"
				+ " VALUES (?,?,?,?,?,?,?,?);";
		
		boolean expired = isExpired(start);
		
		PreparedStatement pstm = conn.prepareStatement(insertEventSQL);
		pstm.setString(1, name);
		pstm.setString(2, place);
		pstm.setTimestamp(3, start);
		pstm.setString(4, duration);
		pstm.setString(5, description);
		pstm.setString(6, username);
		pstm.setBoolean(7, accepted);
		pstm.setBoolean(8, expired);
		
		pstm.executeUpdate();
	}
	
	public static void updateEvent(Connection conn, int id, String name, 
			String place,Timestamp start, String duration, 
			String description, boolean accepted) throws SQLException {
		String updateEventSQL =
				"UPDATE event"
				+ " SET name=?, place=?, start=?, duration=?, description=?, accepted=?"
				+ " WHERE id=?;";
		
		PreparedStatement pstm = conn.prepareStatement(updateEventSQL);
		pstm.setString(1, name);
		pstm.setString(2, place);
		pstm.setTimestamp(3, start);
		pstm.setString(4, duration);
		pstm.setString(5, description);
		pstm.setBoolean(6, accepted);
		pstm.setInt(7, id);
		
		pstm.executeUpdate();
	}
	
	public static void updateEventAccepted(Connection conn, int id, boolean accepted) throws SQLException {
		String updateEventSQL =
				"UPDATE event"
				+ " SET accepted=?"
				+ " WHERE id=?;";
		
		PreparedStatement pstm = conn.prepareStatement(updateEventSQL);
		pstm.setBoolean(1, accepted);
		pstm.setInt(2, id);
		
		pstm.executeUpdate();
	}
	
	public static void deleteEvent(Connection conn, int id) throws SQLException {
		String deleteEventSQL =
				"DELETE FROM event"
				+ " WHERE id=?;";
		
		PreparedStatement pstm = conn.prepareStatement(deleteEventSQL);
		pstm.setInt(1, id);
		
		pstm.executeUpdate();
	}
	
	//used for testing, should delete after
	public static void main(String[] args) {
		Connection conn = null;
		
		try {
			conn = ConnectionUtils.getConnection();
			conn.setAutoCommit(false);
			
			LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
			insertEvent(conn, "parti", "spilja", Timestamp.valueOf(tomorrow), "5 hours", "its gonna be lit", "yovi", true);
			//updateEvent(conn, 21, true);
			
			List<Event> acceptedNonExp = queryAcceptedNonExpiredEvents(conn, "kvart");
			List<Event> pendingNonExp = queryPendingNonExpiredEvents(conn, "kvart");
			List<Event> acceptedExp = queryAcceptedExpiredEvents(conn, "kvart");
			
			System.out.println("Accepted non expired:");
			for (Event event : acceptedNonExp) {
				System.out.println(event.getId() + " " + event.getName() + " " + event.getPlace() + " " + event.getStart() + " " + event.getDuration() + " " + event.getDescription() + " " + event.getUsername() + " " + event.isAccepted() + " " + event.isExpired());
			}
			
			System.out.println();
			System.out.println("Pending non expired:");
			for (Event event : pendingNonExp) {
				System.out.println(event.getId() + " " + event.getName() + " " + event.getPlace() + " " + event.getStart() + " " + event.getDuration() + " " + event.getDescription() + " " + event.getUsername() + " " + event.isAccepted() + " " + event.isExpired());
			}
			
			System.out.println();
			System.out.println("Accepted expired:");
			for (Event event : acceptedExp) {
				System.out.println(event.getId() + " " + event.getName() + " " + event.getPlace() + " " + event.getStart() + " " + event.getDuration() + " " + event.getDescription() + " " + event.getUsername() + " " + event.isAccepted() + " " + event.isExpired());
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
