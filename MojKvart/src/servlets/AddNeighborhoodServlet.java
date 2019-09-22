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
import utils.RedirectUtils;
import utils.StorageUtils;

@WebServlet("/addneighborhood")
public class AddNeighborhoodServlet extends HttpServlet {
	private static final long serialVersionUID = -2405850843806132548L;

	public AddNeighborhoodServlet() {
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

		request.setAttribute("loggedInUser", loggedInUser);
		request.setAttribute("neighborhoods", neighborhoods);

		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/addNeighborhoodView.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("neighborhood");

		String errorMessage = null;

		Connection conn = StorageUtils.getStoredConnection(request);
		User loggedInUser = StorageUtils.getLoggedInUser(request.getSession());
		Neighborhood neighborhood = null;
		try {
			neighborhood = NeighborhoodDAO.findNeighborhood(conn, name);
		} catch (SQLException e) {
			e.printStackTrace();

			RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
			return;
		}

		if (neighborhood != null) {
			errorMessage = "Neighborhood '" + name + "' already exists!";
		}

		if (errorMessage == null) {
			Neighborhood newNeighborhood = new Neighborhood(name);
			try {
				NeighborhoodDAO.insertNeighborhood(conn, newNeighborhood);
			} catch (SQLException e) {
				e.printStackTrace();

				RedirectUtils.errorRedirect(loggedInUser, e.getMessage(), request, response);
				return;
			}
		}

		if (errorMessage != null) {
			request.setAttribute("loggedInUser", loggedInUser);
			request.setAttribute("errorMessage", errorMessage);
			request.setAttribute("neighborhood", neighborhood);

			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/addNeighborhoodView.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/addneighborhood");
		}
	}
}
