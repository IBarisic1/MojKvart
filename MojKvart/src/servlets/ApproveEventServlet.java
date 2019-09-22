package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Event;
import beans.User;
import dao.EventDAO;
import dao.UserDAO;
import utils.RedirectUtils;
import utils.StorageUtils;

@WebServlet("/approveevent")
public class ApproveEventServlet extends HttpServlet {
	private static final long serialVersionUID = 425445512810365017L;

	public ApproveEventServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idString = request.getParameter("id");
		int id = Integer.parseInt(idString);
		
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		String userNeighborhood = loggedInUser.getNeighborhood();
		
		Connection conn = StorageUtils.getStoredConnection(request);
		boolean accepted = true;
		try {
			Event event = EventDAO.findEvent(conn, id);
			String authorName = event.getUsername();
			User author = UserDAO.findUser(conn, authorName);
			String eventNeighborhood = author.getNeighborhood();
			
			if (!userNeighborhood.equals(eventNeighborhood)) {
				request.setAttribute("loggedInUser", loggedInUser);
				
				RequestDispatcher dispatcher = request.getServletContext()
						.getRequestDispatcher("/WEB-INF/views/accessDeniedView.jsp");
				dispatcher.forward(request, response);
				return;
			}
			
			EventDAO.updateEventAccepted(conn, id, accepted);
		} catch (SQLException e) {
			e.printStackTrace();
			
			RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
			return;
		}
		
		response.sendRedirect(request.getContextPath() + "/pendingeventslist");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
