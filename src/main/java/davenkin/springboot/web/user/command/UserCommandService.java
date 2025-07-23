package davenkin.springboot.web.user.command;

import davenkin.springboot.web.user.domain.User;
import davenkin.springboot.web.user.domain.UserFactory;
import davenkin.springboot.web.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// Command here means the "C" of CQRS

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCommandService {
    private final UserFactory userFactory;
    private final UserRepository userRepository;

    @Transactional
    public String createUser(String name) {
        User user = userFactory.createUser(name);
        userRepository.save(user);
        log.info("Created user {}.", user.getId());
        return user.getId();
    }

    @Transactional
    public void updateUserName(String userId, String name) {
        User user = this.userRepository.byId(userId);
        user.updateName(name);
        userRepository.save(user);
        log.info("Updated name of user {}.", userId);
    }

}
