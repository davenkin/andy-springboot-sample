package deviceet.business.device.domain;

import deviceet.external.ExternalDeviceCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class DeviceFactory {

    public Device createDevice(ExternalDeviceCreatedEvent event) {
        return new Device(event.getDeviceId(),
                event.getOrgId(),
                event.getDeviceName(),
                event.getCpuArchitecture(),
                event.getOsType());
    }
}
