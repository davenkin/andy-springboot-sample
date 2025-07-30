package deviceet.user.command;

import deviceet.user.domain.User;
import deviceet.user.domain.UserFactory;
import deviceet.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static deviceet.common.utils.Constants.PLATFORM_ORG_ID;

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
        log.info("Created user[{}].", user.getId());
        return user.getId();
    }

    @Transactional
    public void updateUserName(String userId, String name) {
        User user = this.userRepository.byId(userId, PLATFORM_ORG_ID);
        user.updateName(name);
        userRepository.save(user);
        log.info("Updated name for user[{}].", userId);
    }

}
