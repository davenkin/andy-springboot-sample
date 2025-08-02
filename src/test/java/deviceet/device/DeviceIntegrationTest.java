package deviceet.device;

import deviceet.IntegrationTest;
import deviceet.common.event.DomainEvent;
import deviceet.common.event.consume.EventConsumer;
import deviceet.device.command.DeviceCommandService;
import org.springframework.beans.factory.annotation.Autowired;

public class DeviceIntegrationTest extends IntegrationTest {
    @Autowired
    private DeviceCommandService deviceCommandService;

    @Autowired
    private EventConsumer<DomainEvent> eventConsumer;

}