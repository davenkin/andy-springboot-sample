package deviceet.business.animal.domain;

import deviceet.common.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestArFactory {

    public TestAr create(String name, Principal principal) {
        return new TestAr(name, principal);
    }
}
