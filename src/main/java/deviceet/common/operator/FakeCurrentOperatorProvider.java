package deviceet.common.operator;

import deviceet.common.configuration.profile.EnableForIT;
import org.springframework.stereotype.Component;

@Component
@EnableForIT
public class FakeCurrentOperatorProvider implements CurrentOperatorProvider {
    @Override
    public String currentOperator() {
        return null;
    }
}
