package deviceet.device.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.device.domain.DeviceRepository;
import deviceet.device.domain.cache.CachedOrgDevice;
import deviceet.device.domain.event.DeviceCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceCreatedEventHandler extends AbstractEventHandler<DeviceCreatedEvent> {
    private final DeviceRepository deviceRepository;

    @Override
    public void handle(DeviceCreatedEvent event) {
        List<CachedOrgDevice> cachedOrgDevices = deviceRepository.cachedOrgDevices(event.getArOrgId());
        log.info("List cached devices: {}", cachedOrgDevices);
    }
}
