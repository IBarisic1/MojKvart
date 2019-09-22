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

import beans.ForumThread;
import beans.Post;
import beans.User;
import dao.ForumThreadDAO;
import dao.PostDAO;
import utils.RedirectUtils;
import utils.StorageUtils;

@WebServlet("/thread")
public class ThreadServlet extends HttpServlet {
	private static final long serialVersionUID = -4908859129172928530L;

	public ThreadServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String thread = request.getParameter("thread");

		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		String neighborhood = loggedInUser.getNeighborhood();

		String errorMessage = null;

		Connection conn = StorageUtils.getStoredConnection(request);

		ForumThread th = null;
		try {
			th = ForumThreadDAO.findThread(conn, thread, neighborhood);
		} catch (SQLException e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}

		if (errorMessage == null && th == null) {
			errorMessage = "Requested topic doesn't exist.";
		}

		List<Post> posts = null;
		if (errorMessage == null) {
			try {
				posts = PostDAO.queryPost(conn, thread, neighborhood);
			} catch (SQLException e) {
				e.printStackTrace();
				errorMessage = e.getMessage();
			}
		}

		if (errorMessage != null) {
			RedirectUtils.errorRedirect(loggedInUser, errorMessage, request, response);
		} else {
			request.setAttribute("loggedInUser", loggedInUser);
			request.setAttribute("thread", thread);
			request.setAttribute("author", th.getUsername());
			request.setAttribute("posts", posts);

			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/threadView.jsp");
			dispatcher.forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
