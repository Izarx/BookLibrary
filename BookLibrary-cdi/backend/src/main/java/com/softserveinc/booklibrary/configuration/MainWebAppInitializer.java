package com.softserveinc.booklibrary.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MainWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainWebAppInitializer.class);

	@Override
	protected Class<?>[] getRootConfigClasses() {
		LOGGER.trace("Method getRootConfigClasses ............");
		return new Class[]{
				HibernateConfiguration.class
		};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		LOGGER.trace("Method getServletConfigClasses ............");
		return new Class[]{
				MvcConfiguration.class
		};
	}

	@Override
	protected String[] getServletMappings() {
		LOGGER.trace("Method getServletMappings ............");
		return new String[]{
				"/"
		};
	}
}