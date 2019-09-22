package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Event;
import beans.User;
import dao.EventDAO;
import utils.RedirectUtils;
import utils.StorageUtils;

@WebServlet("/eventslist")
public class EventsListServlet extends HttpServlet {
	private static final long serialVersionUID = 2867397905952995211L;

	public EventsListServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		String neighborhood = loggedInUser.getNeighborhood();
		
		Connection conn = StorageUtils.getStoredConnection(request);
		List<Event> events = null;
		try {
			events = EventDAO.queryAcceptedNonExpiredEvents(conn, neighborhood);
		} catch (SQLException e) {
			e.printStackTrace();

			RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
			return;
		}

		request.setAttribute("loggedInUser", loggedInUser);
		request.setAttribute("events", events);

		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/eventsListView.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
