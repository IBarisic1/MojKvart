package utils;

import java.sql.Connection;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;

public class StorageUtils {
	private static final String REQUEST_CONNECTION = "REQUEST_CONNECTION";
	private static final String SESSION_USER = "SESSION_USER";
	private static final String COOKIE_USER = "COOKIE_USER";
	private static final String SESSION_REDIRECT_URI = "SESSION_REDIRECT_URI";

	private static final int SECONDS_IN_ONE_DAY = 24 * 60 * 60;

	public static void storeConnection(ServletRequest request, Connection conn) {
		request.setAttribute(REQUEST_CONNECTION, conn);
	}

	public static Connection getStoredConnection(ServletRequest request) {
		Connection conn = (Connection) request.getAttribute(REQUEST_CONNECTION);
		return conn;
	}

	public static void storeLoggedInUser(HttpSession session, User user) {
		session.setAttribute(SESSION_USER, user);
	}

	public static User getLoggedInUser(HttpSession session) {
		User user = (User) session.getAttribute(SESSION_USER);
		return user;
	}

	public static void storeUserCookie(HttpServletResponse response, User user) {
		Cookie cookie = new Cookie(COOKIE_USER, user.getUsername());
		cookie.setMaxAge(SECONDS_IN_ONE_DAY);
		response.addCookie(cookie);
	}

	public static String getUserCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (COOKIE_USER.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public static void deleteUserCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie(COOKIE_USER, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	public static void storeRedirectAfterLoginUri(HttpSession session, String uri) {
		session.setAttribute(SESSION_REDIRECT_URI, uri);
	}
	
	public static String getRedirectAfterLoginUri(HttpSession session) {
		String uri = (String) session.getAttribute(SESSION_REDIRECT_URI);
		return uri;
	}
}
