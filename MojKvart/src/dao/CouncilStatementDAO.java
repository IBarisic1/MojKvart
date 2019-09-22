package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import beans.CouncilStatement;
import conn.ConnectionUtils;

public class CouncilStatementDAO {
	public static List<CouncilStatement> queryCouncilStatement(Connection conn, String neighborhood) 
			throws SQLException {
		String getStatementsQuery =
				"SELECT cs.id, cs.councillor, cs.statement"
				+ " FROM councilStatement cs"
				+ " JOIN livesIn"
				+ " ON cs.councillor=livesIn.username"
				+ " WHERE livesIn.neighborhood=?"
				+ " ORDER BY cs.id;";
		
		PreparedStatement pstm = conn.prepareStatement(getStatementsQuery);
		pstm.setString(1, neighborhood);
		ResultSet rs = pstm.executeQuery();
		List<CouncilStatement> statements = new LinkedList<>();
		
		while (rs.next()) {
			int id = rs.getInt("id");
			String councillor = rs.getString("councillor");
			String s = rs.getString("statement");
			
			CouncilStatement councilStatement = new CouncilStatement(id, councillor, s);
			statements.add(councilStatement);
		}
		
		return statements;
	}
	
	public static void insertStatement(Connection conn, String councillor, String statement) 
			throws SQLException {
		String insertStatementSQL =
				"INSERT INTO councilStatement(councillor, statement)"
				+ " VALUES (?,?);";
		
		PreparedStatement pstm = conn.prepareStatement(insertStatementSQL);
		pstm.setString(1, councillor);
		pstm.setString(2, statement);
		
		pstm.executeUpdate();
	}
	
	//used for testing, should delete after
	public static void main(String[] args) {
		Connection conn = null;
		
		try {
			conn = ConnectionUtils.getConnection();
			conn.setAutoCommit(false);
			//insertStatement(conn, "yovi", "nekaj smo pricali ovo ono vamo tamo ne");
			List<CouncilStatement> statements = queryCouncilStatement(conn, "kvart");
			
			for (CouncilStatement st : statements) {
				System.out.println(st.getId() + " " + st.getCouncillor() + " " + st.getStatement());
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
