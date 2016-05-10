package grails.plugin.blazeds;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.groovy.grails.orm.hibernate.support.GrailsOpenSessionInViewFilter;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Overrides <code>doFilterInternal()</code> to just flush the session but not close it, since that will
 * be done in <code>HibernatePersistenceContextInterceptor.destroy()</code>.
 *
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
public class BlazedsOpenSessionInViewFilter extends GrailsOpenSessionInViewFilter {

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException {

		SessionFactory sessionFactory = lookupSessionFactory(request);
		boolean participate = false;

		if (isSingleSession()) {
			// single session mode
			if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
				// Do not modify the Session: just set the participate flag.
				participate = true;
			}
			else {
				logger.debug("Opening single Hibernate Session in OpenSessionInViewFilter");
				Session session = getSession(sessionFactory);
				TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
			}
		}
		else {
			// deferred close mode
			if (SessionFactoryUtils.isDeferredCloseActive(sessionFactory)) {
				// Do not modify deferred close: just set the participate flag.
				participate = true;
			}
			else {
				SessionFactoryUtils.initDeferredClose(sessionFactory);
			}
		}

		try {
			filterChain.doFilter(request, response);
		}
		finally {
			if (!participate) {
				if (isSingleSession()) {
					// single session mode
					SessionHolder sessionHolder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
					// flush the session - it will get closed in HibernatePersistenceContextInterceptor.destroy()
					if (null != sessionHolder && !FlushMode.MANUAL.equals(sessionHolder.getSession().getFlushMode())) {
						sessionHolder.getSession().flush();
					}
				}
				else {
					// deferred close mode
					SessionFactoryUtils.processDeferredClose(sessionFactory);
				}
			}
		}
	}
}
