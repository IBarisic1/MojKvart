package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.CouncilTopicAnnouncement;
import beans.User;
import dao.CouncilTopicAnnouncementDAO;
import utils.StorageUtils;

@WebServlet("/newcounciltopicannouncement")
public class NewCouncilTopicAnnouncementServlet extends HttpServlet {
	private static final long serialVersionUID = 7340873525139109616L;

	public NewCouncilTopicAnnouncementServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		request.setAttribute("loggedInUser", loggedInUser);
		
		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/newAnnouncementView.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String announcement = request.getParameter("name");
		String startString = request.getParameter("start");

		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		String username = loggedInUser.getUsername();

		String errorMessage = null;

		if (announcement == null || startString == null || announcement.isEmpty() || startString.isEmpty()) {
			errorMessage = "Enter all information.";
		}

		Timestamp start = null;
		if (startString != null && !startString.isEmpty()) {
			try {
				start = Timestamp.valueOf(LocalDateTime.parse(startString));
			} catch (DateTimeParseException e) {
				e.printStackTrace();
				errorMessage = e.getMessage();
			}
		}

		if (errorMessage == null) {
			Connection conn = StorageUtils.getStoredConnection(request);

			try {
				CouncilTopicAnnouncementDAO.insertCouncilTopicAnnouncement(conn, announcement, username, start);
			} catch (SQLException e) {
				e.printStackTrace();
				errorMessage = e.getMessage();
			}
		}

		if (errorMessage != null) {
			CouncilTopicAnnouncement ann = new CouncilTopicAnnouncement(announcement, start);
			request.setAttribute("loggedInUser", loggedInUser);
			request.setAttribute("announcement", ann);
			request.setAttribute("errorMessage", errorMessage);

			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/newAnnouncementView.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/counciltopicannouncementslist");
		}
	}
}
