package deviceet.common.notification;

import deviceet.common.configuration.profile.EnableForIT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableForIT
@RequiredArgsConstructor
public class FakeNotificationService implements NotificationService {
    @Override
    public void notifyOnDeviceCreated(String deviceId) {
        // todo: impl
    }
}
