package tanger.med.codechallenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tanger.med.codechallenge.application.impl.UserServiceImpl;
import tanger.med.codechallenge.domain.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CodeChallengeApplicationTests {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void testGenerateUsers() {
        // Given
        int count = 5;

        // When
        List<User> generatedUsers = userService.generateUsers(count);

        // Then
        assertEquals(count, generatedUsers.size());
        for (User user : generatedUsers) {
            // Assert that each generated user has non-null values for essential fields
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
