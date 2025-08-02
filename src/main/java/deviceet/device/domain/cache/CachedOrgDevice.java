package deviceet.device.domain.cache;

import deviceet.device.domain.CpuArchitecture;
import deviceet.device.domain.OsType;
import lombok.Builder;

@Builder
public record CachedOrgDevice(String id,
                              String orgId,
                              String configuredName,
                              CpuArchitecture cpuArchitecture,
                              OsType osType) {
}
