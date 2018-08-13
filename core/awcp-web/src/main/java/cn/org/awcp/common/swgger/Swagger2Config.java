package cn.org.awcp.common.swgger;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.spring.web.paths.Paths.removeAdjacentForwardSlashes;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        //添加head参数start
        ParameterBuilder parameter = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        parameter.defaultValue("APP").name("X-Requested-With").description("客户端类型").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(parameter.build());

        Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .useDefaultResponseMessages(false).pathProvider(new CustRelativePathProvider()).globalOperationParameters(pars).select()
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