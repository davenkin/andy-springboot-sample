package deviceet.common.operator;

import deviceet.common.configuration.profile.DisableForIT;
import org.springframework.stereotype.Component;

@Component
@DisableForIT
public class DefaultCurrentOperator implements CurrentOperator {
    @Override
    public String id() {
        return null;//todo: get operator from JWT of the current thread, or return null
    }
}
