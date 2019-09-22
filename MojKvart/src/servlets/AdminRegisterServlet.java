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

import beans.User;
import dao.UserDAO;
import utils.StorageUtils;

@WebServlet("/adminregister")
public class AdminRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1759231738210243827L;

	public AdminRegisterServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		request.setAttribute("loggedInUser", loggedInUser);
		
		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/adminRegisterView.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		String errorMessage = null;

		if (username == null || password == null || username.length() == 0
				|| password.length() == 0) {
			errorMessage = "Username and password are required.";
		}

		String role = "admin";
		String neighborhood = "null";
		User user = new User(username, password, neighborhood, role);
		
		if (errorMessage == null) {
			Connection conn = StorageUtils.getStoredConnection(request);
			try {
				UserDAO.insertUser(conn, user);
			} catch (SQLException e) {
				e.printStackTrace();
				errorMessage = e.getMessage();
			}
		}

		if (errorMessage != null) {
			User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
			request.setAttribute("loggedInUser", loggedInUser);
			request.setAttribute("errorMessage", errorMessage);
			request.setAttribute("user", user);

			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/adminRegisterView.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/home");
		}
	}
}
