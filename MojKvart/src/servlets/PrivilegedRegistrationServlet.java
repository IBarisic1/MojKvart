package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
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

@WebServlet("/privilegedregister")
public class PrivilegedRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = -125030315997071443L;

	public PrivilegedRegistrationServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = StorageUtils.getStoredConnection(request);
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		List<Neighborhood> neighborhoods = null;

		try {
			neighborhoods = NeighborhoodDAO.queryNeighborhood(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			
			RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
			return;
		}

		List<String> privilegedRoles = new LinkedList<>();
		privilegedRoles.add("moderator");
		privilegedRoles.add("councillor");

		request.setAttribute("loggedInUser", loggedInUser);
		request.setAttribute("neighborhoods", neighborhoods);
		request.setAttribute("privilegedRoles", privilegedRoles);

		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/privilegedRegisterView.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String neighborhood = request.getParameter("neighborhood");
		String role1 = request.getParameter("role");

		String errorMessage = null;

		if (username == null || password == null || neighborhood == null || role1 == null || username.length() == 0
				|| password.length() == 0 || neighborhood.length() == 0 || role1.length() == 0) {
			errorMessage = "Username, password and neighborhood are required.";
		}

		Connection conn = StorageUtils.getStoredConnection(request);
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		
		if (errorMessage == null) {
			User testUser = null;
			try {
				testUser = UserDAO.findUser(conn, username);
			} catch (SQLException e) {
				e.printStackTrace();

				RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
				return;
			}
			
			if (testUser != null) {
				errorMessage = "Username '" + username + "' already exists!";
			}
		}
		
		String role2 = "resident";
		User user = new User(username, password, neighborhood, role1, role2);

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

				RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
				return;
			}
			
			List<String> privilegedRoles = new LinkedList<>();
			privilegedRoles.add("moderator");
			privilegedRoles.add("councillor");

			request.setAttribute("loggedInUser", loggedInUser);
			request.setAttribute("errorMessage", errorMessage);
			request.setAttribute("user", user);
			request.setAttribute("neighborhoods", neighborhoods);
			request.setAttribute("privilegedRoles", privilegedRoles);

			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/privilegedRegisterView.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/home");
		}
	}
}
