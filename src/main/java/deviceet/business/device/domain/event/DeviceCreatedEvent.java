package deviceet.business.device.domain.event;

import deviceet.business.device.domain.Device;
import deviceet.common.event.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static deviceet.common.event.DomainEventType.DEVICE_CREATED_EVENT;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("DEVICE_CREATED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class DeviceCreatedEvent extends DomainEvent {
    private String deviceId;

    public DeviceCreatedEvent(Device device) {
        super(DEVICE_CREATED_EVENT, device);
        this.deviceId = device.getId();
    }
}
