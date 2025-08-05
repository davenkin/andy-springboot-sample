package deviceet.common.operator;

import deviceet.common.configuration.profile.EnableForIT;
import org.springframework.stereotype.Component;

@Component
@EnableForIT
public class FakeCurrentPrincipalProvider implements CurrentPrincipalProvider {
    @Override
    public String currentPrincipalUserId() {
        return null;
    }
}
