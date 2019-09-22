package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.User;
import dao.PostDAO;
import utils.RedirectUtils;
import utils.StorageUtils;

@WebServlet("/newpost")
public class NewPostServlet extends HttpServlet {
	private static final long serialVersionUID = -1901645049078949584L;

	public NewPostServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String thread = request.getParameter("thread");
		String post = request.getParameter("post");

		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		String username = loggedInUser.getUsername();
		String neighborhood = loggedInUser.getNeighborhood();

		Connection conn = StorageUtils.getStoredConnection(request);

		String errorMessage = null;

		if (post == null || post.isEmpty()) {
			errorMessage = "The post is empty.";
		}

		if (errorMessage == null) {
			try {
				PostDAO.insertPost(conn, thread, neighborhood, username, post);
			} catch (SQLException e) {
				e.printStackTrace();

				RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
				return;
			}
		}
		if (errorMessage != null) {
			RedirectUtils.errorRedirect(loggedInUser, errorMessage, request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/thread?thread=" + thread);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
