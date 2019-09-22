package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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

@WebServlet("/deleteevent")
public class DeleteEventServlet extends HttpServlet {
	private static final long serialVersionUID = -8932197317474681491L;

	public DeleteEventServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idString = request.getParameter("id");
		int id = Integer.parseInt(idString);

		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		String userNeighborhood = loggedInUser.getNeighborhood();

		try {
			Connection conn = StorageUtils.getStoredConnection(request);
			
			Event event = EventDAO.findEvent(conn, id);
			String authorName = event.getUsername();
			User author = UserDAO.findUser(conn, authorName);
			String eventNeighborhood = author.getNeighborhood();
			
			if (!userNeighborhood.equals(eventNeighborhood)) {
				RedirectUtils.accessDeniedRedirect(loggedInUser, request, response);
				return;
			}
			
			EventDAO.deleteEvent(conn, id);
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
