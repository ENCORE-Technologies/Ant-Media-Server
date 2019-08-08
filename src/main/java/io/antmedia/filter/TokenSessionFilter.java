package io.antmedia.filter;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import io.antmedia.security.ITokenService;

public class TokenSessionFilter implements HttpSessionListener {

	private ITokenService tokenService;
	protected static Logger logger = LoggerFactory.getLogger(TokenSessionFilter.class);

	ApplicationContext context;

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		logger.debug("session created:{}", se.getSession().getId());
		context = (ApplicationContext)se.getSession().getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		ITokenService tokenServiceTmp = getTokenService();
		if (tokenServiceTmp != null) {
			tokenServiceTmp.getAuthenticatedMap().remove(se.getSession().getId());
		}
	}


	public ITokenService getTokenService() {
		if (tokenService == null) 
		{
			if (context != null) {
				tokenService = (ITokenService)context.getBean(ITokenService.BeanName.TOKEN_SERVICE.toString());
			}
			else {
				logger.warn("Context is null");
			}
		}
		return tokenService;
	}
	
	public void setTokenService(ITokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	public void setContext(ApplicationContext context) {
		this.context = context;
	}

}
