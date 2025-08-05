package deviceet.business.device.domain;

import deviceet.common.security.Principal;
import deviceet.external.ExternalDeviceCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class DeviceFactory {

    public Device createDevice(ExternalDeviceCreatedEvent event, Principal principal) {
        return new Device(event.getDeviceId(),
                event.getOrgId(),
                event.getDeviceName(),
                event.getCpuArchitecture(),
                event.getOsType(),
                principal);
    }
}
