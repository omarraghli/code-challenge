package tanger.med.codechallenge.domain.mappers;

import org.springframework.stereotype.Component;
import tanger.med.codechallenge.api.dtos.RegisterRequestDTO;
import tanger.med.codechallenge.domain.entities.User;

public class UserMapper {

    public static RegisterRequestDTO toDTO(User user) {
        return RegisterRequestDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .city(user.getCity())
                .country(user.getCountry())
                .avatar(user.getAvatar())
                .company(user.getCompany())
                .jobPosition(user.getJobPosition())
                .mobile(user.getMobile())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public static User toEntity(RegisterRequestDTO registerRequestDTO) {
        return User.builder()
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .birthDate(registerRequestDTO.getBirthDate())
                .city(registerRequestDTO.getCity())
                .country(registerRequestDTO.getCountry())
                .avatar(registerRequestDTO.getAvatar())
                .company(registerRequestDTO.getCompany())
                .jobPosition(registerRequestDTO.getJobPosition())
                .mobile(registerRequestDTO.getMobile())
                .username(registerRequestDTO.getUsername())
                .email(registerRequestDTO.getEmail())
                .password(registerRequestDTO.getPassword())
                .role(registerRequestDTO.getRole())
                .build();
    }
}
