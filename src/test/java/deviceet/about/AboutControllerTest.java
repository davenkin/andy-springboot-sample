package deviceet.about;

import deviceet.BaseTest;
import deviceet.user.command.UserCommandService;
import deviceet.user.domain.User;
import deviceet.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AboutControllerTest extends BaseTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCommandService userCommandService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void should_create_user() {
        String userId = userCommandService.createUser("Davenkin");
        User dbUser = userRepository.byId(userId);
        assertEquals("Davenkin", dbUser.getName());

        long count = mongoTemplate.count(Query.query(new Criteria()), User.class);
        System.out.println(count);
    }
}