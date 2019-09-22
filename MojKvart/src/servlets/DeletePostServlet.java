package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Post;
import beans.User;
import dao.PostDAO;
import dao.UserDAO;
import utils.RedirectUtils;
import utils.StorageUtils;

@WebServlet("/deletepost")
public class DeletePostServlet extends HttpServlet {
	private static final long serialVersionUID = 1845510409577387852L;

	public DeletePostServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String thread = request.getParameter("thread");
		String idString = request.getParameter("id");
		int id = Integer.parseInt(idString);

		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		String userNeighborhood = loggedInUser.getNeighborhood();

		Connection conn = StorageUtils.getStoredConnection(request);
		Post post = null;
		try {
			post = PostDAO.findPost(conn, id, thread, userNeighborhood);
			
			if (post == null) {
				String errorMessage = "Selected post doesn't exist!";
				RedirectUtils.errorRedirect(loggedInUser, errorMessage, request, response);
				return;
			}

			boolean isModerator = loggedInUser.getRoles().contains("moderator");
			String authorName = post.getUsername();
			User author = UserDAO.findUser(conn, authorName);
			String authorNeighborhood = author.getNeighborhood();
			boolean isSameNeighborhood = authorNeighborhood.equals(userNeighborhood);
			boolean isNeighborhoodModerator = isModerator && isSameNeighborhood;

			String loggedInUsername = loggedInUser.getUsername();

			if (!authorName.equals(loggedInUsername) && !isNeighborhoodModerator) {
				RedirectUtils.accessDeniedRedirect(loggedInUser, request, response);
				return;
			}
			
			PostDAO.deletePost(conn, id, userNeighborhood, thread);
		} catch (SQLException e) {
			e.printStackTrace();
			
			RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
			return;
		}

		response.sendRedirect(request.getContextPath() + "/thread?thread=" + thread);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
