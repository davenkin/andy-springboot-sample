package deviceet.common.notification;

import deviceet.common.configuration.profile.DisableForIT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@DisableForIT
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {
    @Override
    public void notifyOnDeviceCreated(String deviceId) {
        // todo: impl
    }
}
