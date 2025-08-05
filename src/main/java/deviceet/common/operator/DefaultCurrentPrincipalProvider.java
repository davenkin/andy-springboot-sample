package deviceet.common.operator;

import deviceet.common.configuration.profile.DisableForIT;
import org.springframework.stereotype.Component;

@Component
@DisableForIT
public class DefaultCurrentPrincipalProvider implements CurrentPrincipalProvider {
    @Override
    public String currentPrincipalUserId() {
        return null;//todo: get operator from JWT of the current thread, or return null
    }
}
