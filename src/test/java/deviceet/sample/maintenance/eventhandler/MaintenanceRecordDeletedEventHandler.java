package deviceet.sample.maintenance.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.sample.maintenance.domain.event.MaintenanceRecordDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MaintenanceRecordDeletedEventHandler extends AbstractEventHandler<MaintenanceRecordDeletedEvent> {
    @Override
    public void handle(MaintenanceRecordDeletedEvent event) {
        // todo: count records under equipment
    }
}
