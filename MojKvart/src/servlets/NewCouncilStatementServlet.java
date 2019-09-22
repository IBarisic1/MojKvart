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
import dao.CouncilStatementDAO;
import utils.StorageUtils;

@WebServlet("/newcouncilstatement")
public class NewCouncilStatementServlet extends HttpServlet {
	private static final long serialVersionUID = 3143048829655365143L;

	public NewCouncilStatementServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		request.setAttribute("loggedInUser", loggedInUser);

		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/newStatementView.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String statement = request.getParameter("statement");

		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		String councillor = loggedInUser.getUsername();

		String errorMessage = null;

		if (statement == null || statement.isEmpty()) {
			errorMessage = "The statement is empty!";
		}

		if (errorMessage == null) {
			Connection conn = StorageUtils.getStoredConnection(request);
			try {
				CouncilStatementDAO.insertStatement(conn, councillor, statement);
			} catch (SQLException e) {
				e.printStackTrace();
				errorMessage = e.getMessage();
			}
		}

		if (errorMessage != null) {
			request.setAttribute("loggedInUser", loggedInUser);
			request.setAttribute("statement", statement);
			request.setAttribute("errorMessage", errorMessage);

			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/newStatementView.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/councilstatementslist");
		}
	}

}
