package davenkin.springboot.web.user.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFactory {

    public User createUser(String name) {
        return new User(name);
    }
}
