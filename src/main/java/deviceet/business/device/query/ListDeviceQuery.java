package deviceet.business.device.query;

import deviceet.business.device.domain.CpuArchitecture;
import deviceet.business.device.domain.OsType;
import lombok.Builder;

@Builder
public record ListDeviceQuery(CpuArchitecture cpuArchitecture, OsType osType) {
}

