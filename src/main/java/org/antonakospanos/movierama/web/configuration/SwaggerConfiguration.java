package org.antonakospanos.movierama.web.configuration;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

//	@Bean(name = "Admin API")
//	public Docket adminApi() {
//		return new Docket(DocumentationType.SWAGGER_2)
//				.groupName("Admin API")
//				.apiInfo(adminInfo())
//				.select()
//				.paths(regex(".*/(admin).*"))
//				.build();
//	}
//
//	private ApiInfo adminInfo() {
//		return new ApiInfoBuilder()
//				.title("Admin API")
//				.contact("movierama")
//				.description("MovieRama Administration Interface ")
//				.build();
//	}

	@Bean(name = "MovieRama API")
	public Docket movieramaAPIv1() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("MovieRama API")
				.apiInfo(movieramaInfo())
				.select()
				.paths(apiV1Paths())
				.build();
	}

	@Bean(name = "MovieRama API v2")
	public Docket movieramaAPIv2() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("MovieRama API v2")
				.apiInfo(movieramaInfo())
				.select()
				.paths(apiV2Paths())
				.build();
	}

	private ApiInfo movieramaInfo() {
		return new ApiInfoBuilder()
				.title("MovieRama API")
				.contact("workable.com")
				.description("The Movie Voting Platform")
				.build();
	}

	private Predicate<String> apiV1Paths() {
		return or(regex("/(users|movies|votes).*"));
	}

	private Predicate<String> apiV2Paths() {
		return or(regex("/v2/.*"));
	}
}