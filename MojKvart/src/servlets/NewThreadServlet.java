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

import beans.ForumThread;
import beans.User;
import dao.ForumThreadDAO;
import utils.RedirectUtils;
import utils.StorageUtils;

@WebServlet("/newthread")
public class NewThreadServlet extends HttpServlet {
	private static final long serialVersionUID = 3564130662465290926L;

	public NewThreadServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		request.setAttribute("loggedInUser", loggedInUser);
		
		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/newThreadView.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");

		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		String neighborhood = loggedInUser.getNeighborhood();
		String username = loggedInUser.getUsername();

		Connection conn = StorageUtils.getStoredConnection(request);

		String errorMessage = null;

		if (name == null || name.isEmpty()) {
			errorMessage = "Name is required.";
		}

		if (errorMessage == null) {
			ForumThread thread = null;
			try {
				thread = ForumThreadDAO.findThread(conn, name, neighborhood);
			} catch (SQLException e) {
				e.printStackTrace();

				RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
				return;
			}

			if (thread != null) {
				errorMessage = "Thread named '" + name + "' already exists!";
			}
		}

		if (errorMessage == null) {
			ForumThread newThread = new ForumThread(name, neighborhood, username);

			try {
				ForumThreadDAO.insertThread(conn, newThread);
			} catch (SQLException e) {
				e.printStackTrace();
				errorMessage = e.getMessage();
			}
		}

		if (errorMessage != null) {
			request.setAttribute("loggedInUser", loggedInUser);
			request.setAttribute("errorMessage", errorMessage);
			request.setAttribute("thread", name);

			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/newThreadView.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/thread?thread=" + name);
		}
	}

}
