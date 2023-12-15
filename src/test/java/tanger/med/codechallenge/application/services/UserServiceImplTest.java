package tanger.med.codechallenge.application.services;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tanger.med.codechallenge.CodeChallengeApplication;
import tanger.med.codechallenge.domain.entities.User;
import tanger.med.codechallenge.domain.repositories.UserRepo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
@AllArgsConstructor
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

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
