package cn.org.awcp.common.swgger;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.spring.web.paths.Paths.removeAdjacentForwardSlashes;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .useDefaultResponseMessages(false).pathProvider(new CustRelativePathProvider()).select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any()).build();
        return docket;
    }

    public class CustRelativePathProvider extends AbstractPathProvider {
        public static final String ROOT = "/";

        @Override
        public String getOperationPath(String operationPath) {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
            String uri = removeAdjacentForwardSlashes(uriComponentsBuilder.path(operationPath).build().toString());
            return "/api"+uri;
        }

        @Override
        protected String applicationPath() {
            return  ROOT;
        }

        @Override
        protected String getDocumentationPath() {
            return ROOT;
        }
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("后端API文档")
                .description("API根地址：/swagger-ui.html")
                .termsOfServiceUrl("/swagger-ui.html")
                .contact(new Contact("venson", "https:www.awcp.org.cn", "744026144@qq.com")).version("v2").build();
    }

}