package deviceet.device;

import deviceet.IntegrationTest;
import deviceet.device.command.DeviceCommandService;
import deviceet.device.eventhandler.ExternalDeviceRegisteredEventHandler;
import deviceet.external.ExternalDeviceCreatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DeviceIntegrationTest extends IntegrationTest {
    @Autowired
    private DeviceCommandService deviceCommandService;

    @Autowired
    private ExternalDeviceRegisteredEventHandler externalDeviceRegisteredEventHandler;

    @Test
    public void aa() {
        ExternalDeviceCreatedEvent event = ExternalDeviceCreatedEvent.builder()
                .build();
        externalDeviceRegisteredEventHandler.handle(event);
    }

}