package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import dao.UserDAO;
import utils.RedirectUtils;
import utils.StorageUtils;

@WebServlet("/editevent")
public class EditEventServlet extends HttpServlet {
	private static final long serialVersionUID = -9173231147967288061L;

	public EditEventServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idString = request.getParameter("id");
		int id = Integer.parseInt(idString);
		
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());

		Connection conn = StorageUtils.getStoredConnection(request);
		Event event = null;
		try {
			event = EventDAO.findEvent(conn, id);
			
			if (event == null) {
				String errorMessage = "Selected event doesn't exist!";
				RedirectUtils.errorRedirect(loggedInUser, errorMessage, request, response);
				return;
			}
			
			String authorName = event.getUsername();			
			User author = UserDAO.findUser(conn, authorName);
			String eventNeighborhood = author.getNeighborhood();
			String userNeighborhood = loggedInUser.getNeighborhood();
			
			if (!userNeighborhood.equals(eventNeighborhood)) {
				RedirectUtils.accessDeniedRedirect(loggedInUser, request, response);
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();

			RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
			return;
		}

		String startString = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(event.getStart().toLocalDateTime());
		request.setAttribute("loggedInUser", loggedInUser);
		request.setAttribute("event", event);
		request.setAttribute("start", startString);

		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/editEventView.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idString = request.getParameter("id");
		int id = Integer.parseInt(idString);
		String name = request.getParameter("name");
		String place = request.getParameter("place");
		String startString = request.getParameter("start");
		String duration = request.getParameter("duration");
		String description = request.getParameter("description");
		String username = request.getParameter("username");

		String errorMessage = null;

		if (name == null || place == null || startString == null || duration == null || description == null
				|| name.isEmpty() || place.isEmpty() || startString.isEmpty() || duration.isEmpty()
				|| description.isEmpty()) {
			errorMessage = "Enter all information.";
		}

		Timestamp start = null;
		if (errorMessage == null) {
			try {
				start = Timestamp.valueOf(LocalDateTime.parse(startString));
			} catch (DateTimeParseException e) {
				e.printStackTrace();
				errorMessage = e.getMessage();
			}
		}

		Connection conn = StorageUtils.getStoredConnection(request);
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());

		if (errorMessage == null) {
			try {
				Event event = EventDAO.findEvent(conn, id);
				
				if (event == null) {
					errorMessage = "Selected event doesn't exist!";
					RedirectUtils.errorRedirect(loggedInUser, errorMessage, request, response);
					return;
				}
				
				String authorName = event.getUsername();			
				User author = UserDAO.findUser(conn, authorName);
				String eventNeighborhood = author.getNeighborhood();
				String userNeighborhood = loggedInUser.getNeighborhood();
				
				if (!userNeighborhood.equals(eventNeighborhood)) {
					RedirectUtils.accessDeniedRedirect(loggedInUser, request, response);
					return;
				}
				
				boolean accepted = true;
				EventDAO.updateEvent(conn, id, name, place, start, duration, description, accepted);
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
					.getRequestDispatcher("/WEB-INF/views/editEventView.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/pendingeventslist");
		}
	}
}
