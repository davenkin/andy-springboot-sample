package deviceet.device.query;

import deviceet.device.domain.CpuArchitecture;
import deviceet.device.domain.OsType;
import lombok.Builder;

@Builder
public record ListDeviceQuery(CpuArchitecture cpuArchitecture, OsType osType) {
}

