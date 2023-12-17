package tanger.med.codechallenge.domain.mappers;

import lombok.RequiredArgsConstructor;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Component;
import tanger.med.codechallenge.api.dto.UserDTO;
import tanger.med.codechallenge.domain.entities.User;

/**
 * Mapper class for converting between {@link User} entities and {@link UserDTO} data transfer objects.
 */
@RequiredArgsConstructor
@Component
public class UserMapper {
    private final DozerBeanMapper dozerBeanMapper;

    /**
     * Converts a {@link User} entity to a {@link UserDTO}.
     *
     * @param user The {@link User} entity to convert.
     * @return A {@link UserDTO} representing the converted user.
     */
    public UserDTO toDTO(User user) {
        return this.dozerBeanMapper.map(user, UserDTO.class);
    }

    /**
     * Converts a {@link UserDTO} to a {@link User} entity.
     *
     * @param userDTO The {@link UserDTO} to convert.
     * @return A {@link User} entity representing the converted user.
     */
    public User toEntity(UserDTO userDTO) {
        return this.dozerBeanMapper.map(userDTO, User.class);
    }

}
