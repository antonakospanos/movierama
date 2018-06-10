package org.antonakospanos.movierama;

import com.google.common.collect.ImmutableMap;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration//(exclude={DataSourceAutoConfiguration.class})
@EnableScheduling
@ComponentScan(basePackages = {"org.antonakospanos.movierama"})
public class MovieRamaApplication extends SpringBootServletInitializer {

	private static final String CONFIG_NAME = "movierama-application";

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.properties(ImmutableMap.of("spring.config.name", CONFIG_NAME));
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(MovieRamaApplication.class).
				properties(ImmutableMap.of("spring.config.name", CONFIG_NAME))
				.build()
				.run(args);
	}
}
