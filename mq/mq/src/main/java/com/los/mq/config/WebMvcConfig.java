package com.los.mq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.los.mq.constants.ConfigConstants;
import com.los.mq.utilities.ConfigUtility;


@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

	protected static final Logger LOGGER = LoggerFactory.getLogger(WebMvcConfig.class);

	public static final String PROPERTY_PATH;

	static {
		PROPERTY_PATH = ConfigUtility.getPropertyPath(ConfigConstants.PATH_PROJ_CONFIG,
				ConfigConstants.FILE_SYS_RESOURCE, ConfigConstants.PROPERTY_FILENAME);
	}

	@Bean
	@Qualifier("messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames(ConfigConstants.FILE_PFX + PROPERTY_PATH);
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(1800);
		messageSource.setFallbackToSystemLocale(true);
		return messageSource;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		FileSystemResource[] resources = new FileSystemResource[] {
				new FileSystemResource(PROPERTY_PATH + ConfigConstants.PROPERTIES_EXT) };
		ppc.setLocations(resources);
		ppc.setIgnoreUnresolvablePlaceholders(false);
		return ppc;
	}

}
