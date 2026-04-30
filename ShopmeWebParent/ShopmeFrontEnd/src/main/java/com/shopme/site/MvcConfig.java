package com.shopme.site;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	
		exposeDirectory("../category-images", registry);
		exposeDirectory("../brand-logos", registry);
		exposeDirectory("../product-images", registry);

	}
	
	private void exposeDirectory(String pathPattern,ResourceHandlerRegistry registry) {
		/**
		 * Ini Versi manual nya
		 * String brandImageDirName="../brand-logos";
		Path brandImageDir = Paths.get(brandImageDirName);
		String brandImagePath = brandImageDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/brand-logos/**")
		.addResourceLocations("file:/" + brandImagePath + "/");
		 */
		
		Path path = Paths.get(pathPattern);
		String absultePath = path.toFile().getAbsolutePath();
		
		String logicalPath = pathPattern.replace("../","")+"/**";
		
		registry.addResourceHandler(logicalPath)
		.addResourceLocations("file:/" + absultePath + "/");
	}

}
