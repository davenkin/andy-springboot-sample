package deviceet.testar.domain;

import deviceet.common.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestArFactory {

    public TestAr create(String name, Principal principal) {
        return new TestAr(name, principal);
    }
}
