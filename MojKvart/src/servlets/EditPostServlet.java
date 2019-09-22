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

import beans.Post;
import beans.User;
import dao.PostDAO;
import utils.RedirectUtils;
import utils.StorageUtils;

@WebServlet("/editpost")
public class EditPostServlet extends HttpServlet {
	private static final long serialVersionUID = 615076196695996685L;

	public EditPostServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String thread = request.getParameter("thread");
		String idString = request.getParameter("id");
		int id = Integer.parseInt(idString);

		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		String neighborhood = loggedInUser.getNeighborhood();

		Connection conn = StorageUtils.getStoredConnection(request);

		Post post = null;
		try {
			post = PostDAO.findPost(conn, id, thread, neighborhood);

			String author = post.getUsername();
			String username = loggedInUser.getUsername();

			if (!username.equals(author)) {
				RedirectUtils.accessDeniedRedirect(loggedInUser, request, response);
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();

			RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
			return;
		}

		request.setAttribute("loggedInUser", loggedInUser);
		request.setAttribute("post", post);

		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/editPostView.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String thread = request.getParameter("thread");
		String idString = request.getParameter("id");
		int id = Integer.parseInt(idString);
		String text = request.getParameter("text");

		String errorMessage = null;
		if (text == null || text.isEmpty()) {
			errorMessage = "The post is empty!";
		}

		if (errorMessage == null) {
			User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
			String username = loggedInUser.getUsername();
			String neighborhood = loggedInUser.getNeighborhood();

			Post newPost = new Post(id, thread, neighborhood, username, text);

			Connection conn = StorageUtils.getStoredConnection(request);

			try {
				Post oldPost = PostDAO.findPost(conn, id, thread, neighborhood);

				String author = oldPost.getUsername();

				if (!username.equals(author)) {
					RedirectUtils.accessDeniedRedirect(loggedInUser, request, response);
					return;
				}
				
				PostDAO.updatePost(conn, newPost);
			} catch (SQLException e) {
				e.printStackTrace();

				RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
				return;
			}
		}

		if (errorMessage != null) {
			response.sendRedirect(request.getContextPath() + "/editpost?thread=" + thread + "&id=" + id);
		} else {
			response.sendRedirect(request.getContextPath() + "/thread?thread=" + thread);
		}
	}
}
