package deviceet.device.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.device.domain.event.DeviceNameConfiguredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceNameConfiguredEventHandler extends AbstractEventHandler<DeviceNameConfiguredEvent> {

    @Override
    public void handle(DeviceNameConfiguredEvent event) {
        // todo: impl
    }
}
