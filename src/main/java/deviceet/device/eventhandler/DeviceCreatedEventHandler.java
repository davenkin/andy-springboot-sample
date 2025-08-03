package deviceet.device.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.common.notification.NotificationService;
import deviceet.device.domain.DeviceRepository;
import deviceet.device.domain.event.DeviceCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceCreatedEventHandler extends AbstractEventHandler<DeviceCreatedEvent> {
    private final NotificationService notificationService;
    private final DeviceRepository deviceRepository;

    @Override
    public void handle(DeviceCreatedEvent event) {
        deviceRepository.cachedOrgDevices(event.getArOrgId());
        deviceRepository.cachedOrgDevices(event.getArOrgId());
        notificationService.notifyOnDeviceCreated(event.getArId());
    }
}
