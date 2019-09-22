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

import beans.Neighborhood;
import beans.User;
import dao.NeighborhoodDAO;
import dao.UserDAO;
import utils.RedirectUtils;
import utils.StorageUtils;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1699175971221123698L;

	public RegistrationServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		if (loggedInUser != null) {
			String errorMessage = "You should log out first before trying to register!";
			RedirectUtils.errorRedirect(loggedInUser, errorMessage, request, response);
			return;
		}
		
		Connection conn = StorageUtils.getStoredConnection(request);
		List<Neighborhood> neighborhoods = null;

		try {
			neighborhoods = NeighborhoodDAO.queryNeighborhood(conn);
		} catch (SQLException e) {
			e.printStackTrace();

			RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
			return;
		}

		request.setAttribute("neighborhoods", neighborhoods);

		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/registerView.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String neighborhood = request.getParameter("neighborhood");

		String errorMessage = null;

		if (username == null || password == null || neighborhood == null || username.length() == 0
				|| password.length() == 0 || neighborhood.length() == 0) {
			errorMessage = "Username, password and neighborhood are required.";
		}

		Connection conn = StorageUtils.getStoredConnection(request);
		
		if (errorMessage == null) {
			User testUser = null;
			try {
				testUser = UserDAO.findUser(conn, username);
			} catch (SQLException e) {
				e.printStackTrace();

				RedirectUtils.errorRedirect(null, e.getMessage(), request, response);
				return;
			}
			
			if (testUser != null) {
				errorMessage = "Username '" + username + "' already exists!";
			}
		}
		
		String role = "resident";
		User user = new User(username, password, neighborhood, role);
		
		if (errorMessage == null) {
			try {
				UserDAO.insertUser(conn, user);
			} catch (SQLException e) {
				e.printStackTrace();
				errorMessage = e.getMessage();
			}
		}

		if (errorMessage != null) {
			List<Neighborhood> neighborhoods = null;
			try {
				neighborhoods = NeighborhoodDAO.queryNeighborhood(conn);
			} catch (SQLException e) {
				e.printStackTrace();

				RedirectUtils.errorRedirect(null, e.getMessage(), request, response);
				return;
			}
			
			request.setAttribute("errorMessage", errorMessage);
			request.setAttribute("user", user);
			request.setAttribute("neighborhoods", neighborhoods);

			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/registerView.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/login");
		}
	}
}
