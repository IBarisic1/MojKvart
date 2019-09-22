package filters;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;
import request.RequestWrapper;
import utils.SecurityUtils;
import utils.StorageUtils;
import utils.UrlPatternUtils;

@WebFilter(filterName = "securityFilter", urlPatterns = { "/*" })
public class SecurityFilter implements Filter {

	public SecurityFilter() {
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		if (!UrlPatternUtils.isTargetServlet(req)) {
			chain.doFilter(request, response);
			return;
		}

		String servletPath = req.getServletPath();
		if ("/login".equals(servletPath)) {
			chain.doFilter(request, response);
			return;
		}

		HttpSession session = req.getSession();
		
		StorageUtils.storeRedirectAfterLoginUri(session, null);
		
		User loggedInUser = StorageUtils.getLoggedInUser(session);
		HttpServletRequest wrappedRequest = req;

		if (loggedInUser != null) {
			List<String> roles = loggedInUser.getRoles();
			wrappedRequest = new RequestWrapper(roles, req);
		}

		try {
			if (SecurityUtils.isSecuredPage(req)) {
				if (loggedInUser == null) {
					String uri = req.getRequestURI();
					StorageUtils.storeRedirectAfterLoginUri(session, uri);
					resp.sendRedirect(req.getContextPath() + "/login");
					return;
				}

				boolean hasPermission = SecurityUtils.hasPermission(wrappedRequest);
				if (!hasPermission) {
					request.setAttribute("loggedInUser", loggedInUser);
					RequestDispatcher dispatcher = request.getServletContext()
							.getRequestDispatcher("/WEB-INF/views/accessDeniedView.jsp");

					dispatcher.forward(request, response);
					return;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();

			request.setAttribute("errorMessage", e.getMessage());

			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/errorView.jsp");
			dispatcher.forward(request, response);
			return;
		}

		chain.doFilter(wrappedRequest, response);
	}
}
