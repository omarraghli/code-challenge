package tanger.med.codechallenge.domain.mappers;

import lombok.RequiredArgsConstructor;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Component;
import tanger.med.codechallenge.api.dto.UserDTO;
import tanger.med.codechallenge.domain.entity.User;

/**
 * Mapper class for converting between {@link User} entity and {@link UserDTO} data transfer objects.
 */
@Component
@RequiredArgsConstructor
public class UserMapper {

    /**
     * Converts a {@link User} entity to a {@link UserDTO}.
     *
     * @param user The {@link User} entity to convert.
     * @return A {@link UserDTO} representing the converted user.
     */
    private final DozerBeanMapper dozerBeanMapper;

    public UserDTO toDTO(User user) {
        return dozerBeanMapper.map(user, UserDTO.class);
    }

    /**
     * Converts a {@link UserDTO} to a {@link User} entity.
     *
     * @param userDTO The {@link UserDTO} to convert.
     * @return A {@link User} entity representing the converted user.
     */
    public User toEntity(UserDTO userDTO) {
        return dozerBeanMapper.map(userDTO, User.class);
    }
}
