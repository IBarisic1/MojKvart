package request;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RequestWrapper extends HttpServletRequestWrapper {
	private List<String> roles;
	private HttpServletRequest realRequest;
	
	public RequestWrapper(List<String> roles,
			HttpServletRequest realRequest) {
		super(realRequest);
		this.roles = roles;
		this.realRequest = realRequest;
	}
	
	@Override
	public boolean isUserInRole(String role) {
		if (roles == null) {
			return realRequest.isUserInRole(role);
		}
		
		return roles.contains(role);
	}
}
