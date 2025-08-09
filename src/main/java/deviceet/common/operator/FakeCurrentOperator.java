package deviceet.common.operator;

import deviceet.common.configuration.profile.EnableForIT;
import org.springframework.stereotype.Component;

@Component
@EnableForIT
public class FakeCurrentOperator implements CurrentOperator {
    @Override
    public String id() {
        return "FakeOperator";
    }
}
