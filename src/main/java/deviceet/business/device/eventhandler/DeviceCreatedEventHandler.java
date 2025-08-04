package deviceet.business.device.eventhandler;

import deviceet.business.device.domain.event.DeviceCreatedEvent;
import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.common.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceCreatedEventHandler extends AbstractEventHandler<DeviceCreatedEvent> {
    private final NotificationService notificationService;

    @Override
    public void handle(DeviceCreatedEvent event) {
        notificationService.notifyOnDeviceCreated(event.getArId());
    }
}
