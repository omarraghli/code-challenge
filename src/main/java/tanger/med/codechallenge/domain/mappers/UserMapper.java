package tanger.med.codechallenge.domain.mappers;

import tanger.med.codechallenge.api.dtos.UserDTO;
import tanger.med.codechallenge.domain.entities.User;

/**
 * Mapper class for converting between {@link User} entities and {@link UserDTO} data transfer objects.
 */
public class UserMapper {

    /**
     * Converts a {@link User} entity to a {@link UserDTO}.
     *
     * @param user The {@link User} entity to convert.
     * @return A {@link UserDTO} representing the converted user.
     */
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

    /**
     * Converts a {@link UserDTO} to a {@link User} entity.
     *
     * @param userDTO The {@link UserDTO} to convert.
     * @return A {@link User} entity representing the converted user.
     */
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
