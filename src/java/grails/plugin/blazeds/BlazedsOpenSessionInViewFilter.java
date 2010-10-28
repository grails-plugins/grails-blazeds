package grails.plugin.blazeds;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.groovy.grails.orm.hibernate.support.GrailsOpenSessionInViewFilter;

/**
 * Subclass that ignores <code>IllegalStateException</code>s due to multiple calls to close the Hibernate session.
 *
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
public class BlazedsOpenSessionInViewFilter extends GrailsOpenSessionInViewFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			super.doFilterInternal(request, response, filterChain);
		}
		catch (IllegalStateException ignored) {
			// ok to ignore, HibernatePersistenceContextInterceptor.destroy() has already closed the session
		}
	}
}
