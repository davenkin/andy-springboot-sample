package deviceet.business.device.domain;

import deviceet.business.device.domain.event.DeviceCreatedEvent;
import deviceet.business.device.domain.event.DeviceNameConfiguredEvent;
import deviceet.common.model.AggregateRoot;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

import static deviceet.common.utils.Constants.DEVICE_COLLECTION;
import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldNameConstants
@TypeAlias(DEVICE_COLLECTION)
@Document(DEVICE_COLLECTION)
@NoArgsConstructor(access = PRIVATE)
public class Device extends AggregateRoot {
    private String reportedName;
    private String configuredName;
    private CpuArchitecture cpuArchitecture;
    private OsType osType;

    public Device(String id,
                  String orgId,
                  String reportedName,
                  CpuArchitecture cpuArchitecture,
                  OsType osType) {
        super(id, orgId);
        this.reportedName = reportedName;
        this.configuredName = reportedName;
        this.cpuArchitecture = cpuArchitecture;
        this.osType = osType;
        raiseEvent(new DeviceCreatedEvent(this));
    }

    public void configureName(String name) {
        if (Objects.equals(this.configuredName, name)) {
            return;
        }
        String oldName = this.configuredName;
        this.configuredName = name;
        raiseEvent(new DeviceNameConfiguredEvent(oldName, this.configuredName, this));
    }
}
