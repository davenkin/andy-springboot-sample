package deviceet.common.notification;

import deviceet.common.configuration.profile.EnableForIT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@EnableForIT
@RequiredArgsConstructor
public class FakeNotificationService implements NotificationService {
    public Set<String> deviceIds = new HashSet<>();

    @Override
    public void notifyOnDeviceCreated(String deviceId) {
        this.deviceIds.add(deviceId);
    }
}
