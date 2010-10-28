package grails.plugin.blazeds;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.util.UrlPathHelper;

/**
 * Helper class to get Flex/BlazeDS and Grails requests to work in the same servlet. 
 *
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
public class BlazeDsUrlHandlerMapping extends SimpleUrlHandlerMapping {

	private UrlPathHelper urlPathHelper = new UrlPathHelper();

	@Override
	protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
		String lookupPath = urlPathHelper.getLookupPathForRequest(request);
		if (lookupPath.endsWith(".dispatch")) {
			return null; // let Grails handle it
		}
		return super.getHandlerInternal(request);
	}

	@Override
	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		super.setUrlPathHelper(urlPathHelper);
		this.urlPathHelper = urlPathHelper;
	}
}
