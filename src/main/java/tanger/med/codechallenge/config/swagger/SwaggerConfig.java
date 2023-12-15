package tanger.med.codechallenge.config.swagger;

import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> operation.addParametersItem(
                new Parameter()
                        .in("header")
                        .description("Authorization")
                        .name("Authorization token"));
    }
}



