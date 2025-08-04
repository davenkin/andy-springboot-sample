package deviceet.external;

import deviceet.business.device.domain.CpuArchitecture;
import deviceet.business.device.domain.OsType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Data
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
public class ExternalDeviceCreatedEvent extends ExternalEvent {
    String deviceId;
    String deviceName;
    CpuArchitecture cpuArchitecture;
    OsType osType;
}
