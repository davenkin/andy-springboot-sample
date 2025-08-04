package deviceet.business.device.domain.event;

import deviceet.business.device.domain.Device;
import deviceet.common.event.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static deviceet.common.event.DomainEventType.DEVICE_NAME_CONFIGURED_EVENT;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("DEVICE_NAME_CONFIGURED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class DeviceNameConfiguredEvent extends DomainEvent {
    private String oldName;
    private String newName;

    public DeviceNameConfiguredEvent(String oldName, String newName, Device device) {
        super(DEVICE_NAME_CONFIGURED_EVENT, device);
        this.oldName = oldName;
        this.newName = newName;
    }
}
