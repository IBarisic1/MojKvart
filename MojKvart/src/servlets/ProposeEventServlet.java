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

import beans.Event;
import beans.User;
import dao.EventDAO;
import utils.StorageUtils;

@WebServlet("/proposeevent")
public class ProposeEventServlet extends HttpServlet {
	private static final long serialVersionUID = -291627366441826133L;

	public ProposeEventServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		request.setAttribute("loggedInUser", loggedInUser);
		
		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/proposeEventView.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String place = request.getParameter("place");
		String startString = request.getParameter("start");
		String duration = request.getParameter("duration");
		String description = request.getParameter("description");

		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		String username = loggedInUser.getUsername();

		String errorMessage = null;

		if (name == null || place == null || startString == null || duration == null || description == null
				|| name.isEmpty() || place.isEmpty() || startString.isEmpty() || duration.isEmpty()
				|| description.isEmpty()) {
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

		Connection conn = StorageUtils.getStoredConnection(request);

		if (errorMessage == null) {
			boolean isModerator = loggedInUser.getRoles().contains("moderator");
			boolean accepted = isModerator;
			try {
				EventDAO.insertEvent(conn, name, place, start, duration, description, username, accepted);
			} catch (SQLException e) {
				e.printStackTrace();
				errorMessage = e.getMessage();
			}
		}

		if (errorMessage != null) {
			Event event = new Event(name, place, start, duration, description, username);

			request.setAttribute("loggedInUser", loggedInUser);
			request.setAttribute("errorMessage", errorMessage);
			request.setAttribute("event", event);

			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/proposeEventView.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/eventslist");
		}
	}
}
