package grails.plugin.blazeds

import org.apache.log4j.Logger
import org.springframework.flex.core.AbstractDestinationFactory
import org.springframework.flex.remoting.RemotingDestinationExporter
import org.springframework.util.Assert
import org.springframework.util.ClassUtils
import org.springframework.util.StringUtils

import flex.messaging.Destination
import flex.messaging.FactoryInstance
import flex.messaging.FlexFactory
import flex.messaging.MessageBroker
import flex.messaging.config.ConfigMap
import flex.messaging.services.RemotingService
import flex.messaging.services.Service
import flex.messaging.services.ServiceAdapter
import flex.messaging.services.remoting.RemotingDestination
import flex.messaging.services.remoting.adapters.JavaAdapter
import flex.messaging.services.remoting.adapters.RemotingMethod

/**
 * Reworked {@link RemotingDestinationExporter} that's more Grails-friendly.
 *
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class BlazeDsRemotingDestinationExporter extends AbstractDestinationFactory implements FlexFactory {

	private final Logger log = Logger.getLogger(getClass())

	private Object service
	private String[] includeMethods
	private String[] excludeMethods

	/** The Spring bean name of the exported service. */
	String serviceBeanName

	/**The class of the unproxied exported service. */
	Class<?> serviceClass

	/**
	 * {@inheritDoc}
	 * @see org.springframework.flex.core.AbstractDestinationFactory#createDestination(
	 * 	java.lang.String, flex.messaging.MessageBroker)
	 */
	@Override
	protected Destination createDestination(String destinationId, MessageBroker broker) {
		Assert.notNull serviceBeanName, "The 'serviceBeanName' property is required."
		Assert.notNull serviceClass, "The 'serviceClass' property is required."

		service = getBeanFactory().getBean(serviceBeanName)

		// Look up the remoting service
		RemotingService remotingService = broker.getServiceByType(RemotingService.name)
		Assert.notNull remotingService, "Could not find a proper RemotingService in the Flex MessageBroker."

		// Register and start the destination
		RemotingDestination destination = remotingService.createDestination(destinationId)

		destination.setFactory(this)
		destination.setSource(serviceClass.name)

		log.info "Created remoting destination with id '$destinationId'"

		return destination
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void destroyDestination(String destinationId, MessageBroker broker) {
		RemotingService remotingService = broker.getServiceByType(RemotingService.name)
		if (!remotingService) {
			return
		}

		log.info "Removing remoting destination '$destinationId'"

		remotingService.removeDestination destinationId
	}

	/**
	 * {@inheritDoc}
	 * @see flex.messaging.FlexFactory#createFactoryInstance(java.lang.String,
	 * 		flex.messaging.config.ConfigMap)
	 */
	FactoryInstance createFactoryInstance(String id, ConfigMap properties) {
		new ServiceFactoryInstance(this, id, properties)
	}

	/**
	 * {@inheritDoc}
	 * @see flex.messaging.FlexConfigurable#initialize(java.lang.String, flex.messaging.config.ConfigMap)
	 */
	void initialize(String id, ConfigMap configMap) {
		// No-op
	}

	/**
	 * Lookup will be handled directly by the created FactoryInstance.
	 */
	Object lookup(FactoryInstance instanceInfo) {
		throw new UnsupportedOperationException('FlexFactory.lookup')
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Service getTargetService(MessageBroker broker) {
		broker.getServiceByType RemotingService.name
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initializeDestination(Destination destination) {
		destination.start()

		Assert.isInstanceOf(ServiceAdapter, destination.getAdapter(),
				'Spring beans exported as a RemotingDestination require a ServiceAdapter.')

		configureIncludes destination
		configureExcludes destination

		log.info "Remoting destination '$destination.id' has been started started successfully."
	}

	private void configureExcludes(Destination destination) {
		if (!excludeMethods) {
			return
		}

		JavaAdapter adapter = destination.getAdapter()
		for (RemotingMethod method : getRemotingMethods(excludeMethods)) {
			adapter.addExcludeMethod method
		}
	}

	private void configureIncludes(Destination destination) {
		if (includeMethods == null) {
			return
		}

		JavaAdapter adapter = destination.getAdapter()
		for (RemotingMethod method : getRemotingMethods(includeMethods)) {
			adapter.addIncludeMethod method
		}
	}

	private List<RemotingMethod> getRemotingMethods(String[] methodNames) {
		List<RemotingMethod> remotingMethods = []
		for (String name : methodNames) {
			Assert.isTrue(ClassUtils.hasAtLeastOneMethodWithName(serviceClass, name),
					"Could not find method with name '" + name +
					"' on the exported service of type " + serviceClass)
			remotingMethods << new RemotingMethod(name: name)
		}
		remotingMethods
	}

	/**
	 * Sets the methods to be excluded from the bean being exported.
	 *
	 * @param methods the methods to exclude
	 */
	void setExcludeMethods(String[] methods) {
		excludeMethods = StringUtils.trimArrayElements(methods)
	}

	/**
	 * Sets the methods to included from the bean being exported.
	 *
	 * @param methods the methods to include
	 */
	void setIncludeMethods(String[] methods) {
		includeMethods = StringUtils.trimArrayElements(methods)
	}
}

class ServiceFactoryInstance extends FactoryInstance {
	private final FlexFactory factory

	ServiceFactoryInstance(FlexFactory factory, String id, ConfigMap properties) {
		super(factory, id, properties)
		this.factory = factory
	}

	@Override
	Object lookup() { factory.service }
}
