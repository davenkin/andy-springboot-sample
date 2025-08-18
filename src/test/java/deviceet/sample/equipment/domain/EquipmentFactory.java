package deviceet.sample.equipment.domain;

import deviceet.common.model.operator.Operator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EquipmentFactory {

    public Equipment create(String name, Operator operator) {
        return new Equipment(name, operator);
    }
}
