package filters;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import beans.User;
import dao.UserDAO;
import utils.StorageUtils;
import utils.UrlPatternUtils;

@WebFilter(filterName = "cookieFilter", urlPatterns = { "/*" })
public class CookieFilter implements Filter {
	private static final String COOKIE_CHECKED_ATRIBUTE = "COOKIE_CHECKED_ATTRIBUTE";
	private static final String COOKIE_CHECKED = "COOKIE_CHECKED";
	
	public CookieFilter() {
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
		HttpSession session = req.getSession();
		
		if(!UrlPatternUtils.isTargetServlet(req)) {
			chain.doFilter(request, response);
			return;
		}
		
		User userInSession = StorageUtils.getLoggedInUser(session);
		if (userInSession != null) {
			session.setAttribute(COOKIE_CHECKED_ATRIBUTE, COOKIE_CHECKED);
			chain.doFilter(request, response);
			return;
		}
		
		Connection conn = StorageUtils.getStoredConnection(request);
		String cookieChecked = (String) session.getAttribute(COOKIE_CHECKED_ATRIBUTE);
		
		if (cookieChecked == null && conn != null) {
			String userInCookie = StorageUtils.getUserCookie(req);
			try {
				User user = UserDAO.findUser(conn, userInCookie);
				StorageUtils.storeLoggedInUser(session, user);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			session.setAttribute(COOKIE_CHECKED_ATRIBUTE, COOKIE_CHECKED);
		}
		
		chain.doFilter(request, response);
	}
}
