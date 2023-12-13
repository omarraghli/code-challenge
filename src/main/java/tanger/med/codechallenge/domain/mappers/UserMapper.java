package tanger.med.codechallenge.domain.mappers;

import tanger.med.codechallenge.api.dtos.UserDTO;
import tanger.med.codechallenge.domain.entities.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
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

    public static User toEntity(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .birthDate(userDTO.getBirthDate())
                .city(userDTO.getCity())
                .country(userDTO.getCountry())
                .avatar(userDTO.getAvatar())
                .company(userDTO.getCompany())
                .jobPosition(userDTO.getJobPosition())
                .mobile(userDTO.getMobile())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .build();
    }
}
