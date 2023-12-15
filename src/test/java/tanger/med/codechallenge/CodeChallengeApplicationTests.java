package tanger.med.codechallenge;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import tanger.med.codechallenge.api.dtos.UserDTO;
import tanger.med.codechallenge.application.services.UserServiceImpl;
import tanger.med.codechallenge.domain.entities.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import tanger.med.codechallenge.domain.mappers.UserMapper;


import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tanger.med.codechallenge.domain.repositories.UserRepo;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CodeChallengeApplicationTests {

    @Autowired
    private UserServiceImpl userService;



    @Mock
    private UserRepo userRepo;

    @Test
    public void testGetAllUsers() {
        // Arrange
        int page = 0;
        int size = 10;

        // Create a list of users for testing
        List<User> userList = Arrays.asList(new User(), new User(), new User());

        // Create a dummy Page<User> using PageImpl
        Page<User> userPage = new PageImpl<>(userList);

        // Mock behavior for userRepo.findAll
        when(userRepo.findAll(PageRequest.of(page, size))).thenReturn(userPage);

        // Act
        Page<UserDTO> result = userService.getAllUsers(page, size);

        // Assert
        assertEquals(userList.size(), result.getContent().size());
        // Add more specific assertions if needed
    }
    @Test
    public void testGenerateUsers() {
        // Arrange
        int count = 5;

        // Act
        List<User> users = userService.generateUsers(count);

        // Assert
        assertNotNull(users);
        assertEquals(count, users.size());

        for (User user : users) {
            assertNotNull(user.getFirstName());
            assertNotNull(user.getLastName());
            assertNotNull(user.getBirthDate());
            assertNotNull(user.getCity());
            assertNotNull(user.getCountry());
            assertNotNull(user.getAvatar());
            assertNotNull(user.getCompany());
            assertNotNull(user.getJobPosition());
            assertNotNull(user.getMobile());
            assertNotNull(user.getUsername());
            assertNotNull(user.getEmail());
            assertNotNull(user.getPassword());
            assertNotNull(user.getRole());
        }
    }

}
