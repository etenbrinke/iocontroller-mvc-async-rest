package etenbrinke.iocontroller.mvc.async.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by etenbrinke on 08/05/16.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("springboot")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "IoController Async REST API",
                "REST API to control the Elektuur IO-Controller.",
                "https://github.com/etenbrinke/iocontroller-mvc-async-rest",
                "Terms of service",
                "ep@tenbrinke.net",
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.html");
        return apiInfo;
    }
}
