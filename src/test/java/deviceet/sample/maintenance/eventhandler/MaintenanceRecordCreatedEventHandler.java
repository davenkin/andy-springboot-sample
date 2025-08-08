package deviceet.sample.maintenance.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.sample.maintenance.domain.event.MaintenanceRecordCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MaintenanceRecordCreatedEventHandler extends AbstractEventHandler<MaintenanceRecordCreatedEvent> {
    @Override
    public void handle(MaintenanceRecordCreatedEvent event) {
// todo: count records under equipment
    }
}
