package utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import dao.PermissionDAO;
import dao.RoleDAO;

public class SecurityUtils {
	public static boolean isSecuredPage(HttpServletRequest request) throws SQLException {
		Connection conn = StorageUtils.getStoredConnection(request);	
		String urlPattern = UrlPatternUtils.getUrlPattern(request);
		
		List<String> roles = RoleDAO.queryRole(conn);
		
		for (String role : roles) {
			List<String> urlPatterns = PermissionDAO.queryPermissionsForRole(conn, role);
			if (urlPatterns.contains(urlPattern)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean hasPermission(HttpServletRequest request) throws SQLException {
		String urlPattern = UrlPatternUtils.getUrlPattern(request);
		Connection conn = StorageUtils.getStoredConnection(request);
		
		List<String> roles = RoleDAO.queryRole(conn);
		
		for (String role : roles) {
			if (!request.isUserInRole(role)) {
				continue;
			}
			
			List<String> urlPatterns = PermissionDAO.queryPermissionsForRole(conn, role);
			if (urlPatterns.contains(urlPattern)) {
				return true;
			}
		}
		
		return false;
	}
}
