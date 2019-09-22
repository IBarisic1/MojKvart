package utils;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import beans.User;

public class RedirectUtils {
	public static void errorRedirect(User loggedInUser, String errorMessage, ServletRequest request, ServletResponse response) throws ServletException, IOException {
		request.setAttribute("loggedInUser", loggedInUser);
		request.setAttribute("errorMessage", errorMessage);

		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/errorView.jsp");
		dispatcher.forward(request, response);
	}
	
	public static void accessDeniedRedirect(User loggedInUser, ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("loggedInUser", loggedInUser);
		
		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/accessDeniedView.jsp");
		dispatcher.forward(request, response);
	}
}
