package servlets;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;
import conn.ConnectionUtils;
import dao.UserDAO;
import utils.RedirectUtils;
import utils.StorageUtils;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 4286940923785367241L;

	public LoginServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		if (loggedInUser != null) {
			String errorMessage = "You should log out first before trying to log in!";
			RedirectUtils.errorRedirect(loggedInUser, errorMessage, request, response);
			return;
		}

		RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String rememberMe = request.getParameter("rememberMe");
		boolean remember = "Y".equals(rememberMe);

		Connection conn = null;
		User user = null;
		String errorMessage = null;

		if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
			errorMessage = "Username and password are required.";
		}

		if (errorMessage == null) {
			try {
				conn = ConnectionUtils.getConnection();
				user = UserDAO.findUser(conn, username, password);
			} catch (Exception e) {
				e.printStackTrace();
				errorMessage = e.getMessage();
			}

			if (user == null) {
				errorMessage = "Invalid username or password.";
			}
		}

		if (errorMessage != null) {
			user = new User(username, password, null);

			request.setAttribute("user", user);
			request.setAttribute("errorMessage", errorMessage);

			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/loginView.jsp");
			dispatcher.forward(request, response);
		} else {
			HttpSession session = request.getSession();
			StorageUtils.storeLoggedInUser(session, user);

			if (remember) {
				StorageUtils.storeUserCookie(response, user);
			} else {
				StorageUtils.deleteUserCookie(response);
			}

			String redirectUri = StorageUtils.getRedirectAfterLoginUri(session);
			if (redirectUri != null) {
				response.sendRedirect(redirectUri);
				return;
			}

			response.sendRedirect(request.getContextPath() + "/userinfo");
		}
	}
}
