package deviceet.business.device.eventhandler;

import deviceet.business.device.domain.event.DeviceNameConfiguredEvent;
import deviceet.common.event.consume.AbstractEventHandler;
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
