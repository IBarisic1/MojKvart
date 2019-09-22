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

import beans.CouncilTopicAnnouncement;
import beans.User;
import dao.CouncilTopicAnnouncementDAO;
import utils.RedirectUtils;
import utils.StorageUtils;

@WebServlet("/counciltopicannouncementslist")
public class CouncilTopicAnnouncementsListServlet extends HttpServlet {
	private static final long serialVersionUID = 8625301376488594730L;

	public CouncilTopicAnnouncementsListServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		String neighborhood = loggedInUser.getNeighborhood();

		Connection conn = StorageUtils.getStoredConnection(request);
		List<CouncilTopicAnnouncement> announcements = null;
		try {
			announcements = CouncilTopicAnnouncementDAO.queryNonExpiredCouncilTopicAnnouncements(conn, neighborhood);
		} catch (SQLException e) {
			e.printStackTrace();
			
			RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
			return;
		}

		request.setAttribute("loggedInUser", loggedInUser);
		request.setAttribute("announcements", announcements);

		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/announcementsView.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
