package deviceet.common.operator;

import deviceet.common.configuration.profile.EnableForCI;
import org.springframework.stereotype.Component;

@Component
@EnableForCI
public class FakeCurrentOperatorProvider implements CurrentOperatorProvider {
    @Override
    public String currentOperator() {
        return null;
    }
}
