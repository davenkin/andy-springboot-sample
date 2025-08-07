package deviceet.sample.equipment.domain;

import deviceet.common.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EquipmentFactory {
    public Equipment create(String name, Principal principal) {
        return new Equipment(name, principal);
    }
}
