package deviceet.device.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.device.domain.Device;
import deviceet.device.domain.DeviceFactory;
import deviceet.device.domain.DeviceRepository;
import deviceet.external.ExternalDeviceCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalDeviceRegisteredEventHandler extends AbstractEventHandler<ExternalDeviceCreatedEvent> {
    private final DeviceFactory deviceFactory;
    private final DeviceRepository deviceRepository;

    @Override
    public void handle(ExternalDeviceCreatedEvent event) {
        Device device = deviceFactory.createDevice(event);
        deviceRepository.save(device);
        log.info("Device {} created.", device.getId());
    }
}
