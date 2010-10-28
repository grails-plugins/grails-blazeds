package grails.plugin.blazeds;

import groovy.lang.Closure;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.flex.core.io.SpringPropertyProxy;

/**
 * Filters out the MetaClass property, Closures, etc.
 *
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
public class BlazeDsPropertyProxy extends SpringPropertyProxy {

	private static final long serialVersionUID = 1;

	private ConversionService _conversionService;

	private final List<String> ignoreNames;

	/**
	 * Constructor that takes a class.
	 * @param beanType  the class
	 * @param ignoreNames  optional override of default names to ignore
	 */
	public BlazeDsPropertyProxy(Class<?> beanType, final List<String> ignoreNames) {
		super(beanType);
		if (ignoreNames == null) {
			this.ignoreNames = Collections.emptyList();
		}
		else {
			this.ignoreNames = ignoreNames;
		}
	}

	/**
	 * Constructor that takes an instance.
	 * @param defaultInstance and instance to get class from
	 * @param ignoreNames  optional override of default names to ignore
	 */
	public BlazeDsPropertyProxy(Object defaultInstance, final List<String> ignoreNames) {
		super(defaultInstance);
		if (ignoreNames == null) {
			this.ignoreNames = Collections.emptyList();
		}
		else {
			this.ignoreNames = ignoreNames;
		}
	}

	@Override
	public List<String> getPropertyNames(Object instance) {
		List<String> names = new ArrayList<String>();
		for (PropertyDescriptor pd : getBeanPropertyAccessor(instance).getPropertyDescriptors()) {
			if (ignoreNames.contains(pd.getName())) {
				continue;
			}
			if (pd.getPropertyType().isAssignableFrom(Closure.class)) {
				continue;
			}
			names.add(pd.getName());
		}
		return names;
	}

//	@Override
//	public Object getValue(Object instance, String propertyName) {
//		BeanWrapper wrapper = getBeanPropertyAccessor(instance);
//		Object value = getBeanPropertyAccessor(instance).getPropertyValue(propertyName);
//		if (value == null) {
//			return null;
//		}
//		TypeDescriptor targetType = wrapper.getPropertyTypeDescriptor(propertyName);
//		TypeDescriptor sourceType = TypeDescriptor.valueOf(value.getClass());
//		if (!sourceType.getType().equals(targetType.getType()) && _conversionService.canConvert(sourceType, targetType)) {
//			value = _conversionService.convert(value, sourceType, targetType);
//		}
//		return value;
//	}

	private BeanWrapper getBeanPropertyAccessor(Object instance) {
		BeanWrapper accessor = PropertyAccessorFactory.forBeanPropertyAccess(instance);
		accessor.setConversionService(_conversionService);
		return accessor;
	}

	@Override
	public void setConversionService(ConversionService conversionService) {
		super.setConversionService(conversionService);
		_conversionService = conversionService;
	}
}
