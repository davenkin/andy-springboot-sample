package deviceet.common.operator;

import deviceet.common.configuration.profile.DisableForIT;
import org.springframework.stereotype.Component;

@Component
@DisableForIT
public class DefaultCurrentOperatorProvider implements CurrentOperatorProvider {
    @Override
    public String currentOperator() {
        return null;//todo: get operator from JWT of the current thread, or return null
    }
}
