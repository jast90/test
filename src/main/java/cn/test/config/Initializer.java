package cn.test.config;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by zhiwen on 2017/5/25.
 */
public class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{Config.class};
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{MvcConfig.class};
    }


    protected String[] getServletMappings() {
        return new String[]{"/"};
    }


}
