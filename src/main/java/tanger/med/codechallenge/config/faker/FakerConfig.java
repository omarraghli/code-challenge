package tanger.med.codechallenge.config.faker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.javafaker.Faker;

/**
 * Configuration class for providing a Faker bean for generating random data.
 */
@Configuration
public class FakerConfig {

    /**
     * Provides a Faker bean for generating random data.
     *
     * @return A Faker instance.
     */
    @Bean
    public Faker faker() {
        return new Faker();
    }
}
