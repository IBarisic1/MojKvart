package utils;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;

public class UrlPatternUtils {
	private static boolean isUrlPatternRegistered(Collection<? extends ServletRegistration> servletRegistrations, String urlPattern) {		
		for (ServletRegistration sr : servletRegistrations) {
			Collection<String> mappings = sr.getMappings();
			if (mappings.contains(urlPattern)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static String getUrlPattern(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		String pathInfo = request.getPathInfo();
		
		String urlPattern = null;
		
		// urlPattern: "/servletPath/*"
		if (pathInfo != null) {
			urlPattern = servletPath + pathInfo;
			return urlPattern;
		}

		ServletContext context = request.getServletContext();
		Collection<? extends ServletRegistration> servletRegistrations =
				context.getServletRegistrations().values();
		
		// urlPattern: "/servletPath"
		urlPattern = servletPath;
		if (isUrlPatternRegistered(servletRegistrations, urlPattern)) {
			return urlPattern;
		}
		
		// urlPattern: "*.extension"
		int lastDotIndex = urlPattern.lastIndexOf('.');
		if (lastDotIndex != -1) {
			String extension = urlPattern.substring(lastDotIndex + 1);
			urlPattern = "*." + extension;
			
			if (isUrlPatternRegistered(servletRegistrations, urlPattern)) {
				return urlPattern;
			}
		}
		
		// urlPattern not registered
		return "/";
	}
	
	public static boolean isTargetServlet(HttpServletRequest request) {
		String urlPattern = getUrlPattern(request);
		return !("/".equals(urlPattern));
	}
}
