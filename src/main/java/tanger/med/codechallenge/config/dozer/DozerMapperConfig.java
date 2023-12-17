package tanger.med.codechallenge.config.dozer;


import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tanger.med.codechallenge.api.dto.UserDTO;
import tanger.med.codechallenge.domain.entity.User;

@Configuration
public class DozerMapperConfig {
    @Bean
    public DozerBeanMapper dozerBeanMapper() {
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.addMapping(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                // Map fields from Entity to DTO
                mapping(User.class, UserDTO.class)
                        .fields("firstName", "firstName")
                        .fields("lastName", "lastName")
                        .fields("birthDate", "birthDate")
                        .fields("city", "city")
                        .fields("country", "country")
                        .fields("avatar", "avatar")
                        .fields("company", "company")
                        .fields("jobPosition", "jobPosition")
                        .fields("mobile", "mobile")
                        .fields("username", "username")
                        .fields("email", "email")
                        .fields("password", "password")
                        .fields("role", "role");
            }
        });

        return dozerBeanMapper;
    }
}
