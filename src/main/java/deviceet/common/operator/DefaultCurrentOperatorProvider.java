package deviceet.common.operator;

import deviceet.common.configuration.profile.DisableForCI;
import org.springframework.stereotype.Component;

@Component
@DisableForCI
public class DefaultCurrentOperatorProvider implements CurrentOperatorProvider {
    @Override
    public String currentOperator() {
        return "";//todo: get operator from JWT of the current thread, or return null
    }
}
