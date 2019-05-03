package brainheap.config

import brainheap.item.rest.ItemController
import brainheap.user.rest.UserController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(ItemController::class.java.`package`.name))
                .paths(PathSelectors.any())
                .build()
    }

    private fun getApiInfo(): ApiInfo {
        val contact = Contact("brainheap-back", "https://github.com/annazarubina/brainheap-back", "")
        return ApiInfoBuilder()
                .title("BrainHeap API")
                .description("BrainHeap service")
                .version("1.0.0")
                .contact(contact)
                .build()
    }
}