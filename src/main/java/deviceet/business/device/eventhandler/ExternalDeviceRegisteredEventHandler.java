package deviceet.business.device.eventhandler;

import deviceet.business.device.domain.Device;
import deviceet.business.device.domain.DeviceFactory;
import deviceet.business.device.domain.DeviceRepository;
import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.external.ExternalDeviceCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static deviceet.common.security.Principal.ROBOT;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalDeviceRegisteredEventHandler extends AbstractEventHandler<ExternalDeviceCreatedEvent> {
    private final DeviceFactory deviceFactory;
    private final DeviceRepository deviceRepository;

    @Override
    public void handle(ExternalDeviceCreatedEvent event) {
        Device device = deviceFactory.createDevice(event, ROBOT);
        deviceRepository.save(device);
        log.info("Device {} created.", device.getId());
    }
}
