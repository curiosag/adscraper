package app;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.BeansException;

public class PostProcessor implements BeanPostProcessor {
 
   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
      System.out.println("bean before: " + beanInfo(beanName, bean));
      return bean; 
   }

   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
      System.out.println("bean after : " + beanInfo(beanName, bean));
      return bean; 
   }
   
   private String beanInfo(String name, Object bean){
	   return String.format("%s %s %s", bean.getClass().getSimpleName(), name, bean.toString());
   }

}
