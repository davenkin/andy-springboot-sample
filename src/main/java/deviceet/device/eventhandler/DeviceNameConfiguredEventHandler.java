package deviceet.device.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.common.notification.NotificationService;
import deviceet.device.domain.event.DeviceNameConfiguredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceNameConfiguredEventHandler extends AbstractEventHandler<DeviceNameConfiguredEvent> {
    private final NotificationService notificationService;

    @Override
    public void handle(DeviceNameConfiguredEvent event) {
        notificationService.notifyOnDeviceCreated(event.getArId());
    }
}
