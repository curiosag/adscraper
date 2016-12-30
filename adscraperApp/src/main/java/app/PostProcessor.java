package app;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;

public class PostProcessor implements BeanPostProcessor {

	private static final Log LOG = LogFactory.getLog(AdScraperApplication.class);

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		log("bean before: " + beanInfo(beanName, bean));
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		log("bean after : " + beanInfo(beanName, bean));
		return bean;
	}

	private String beanInfo(String name, Object bean) {
		return String.format("%s %s %s", bean.getClass().getSimpleName(), name, bean.toString());
	}

	private void log(String msg) {
		if (LOG.isDebugEnabled())
			LOG.debug(msg);
	}

}
