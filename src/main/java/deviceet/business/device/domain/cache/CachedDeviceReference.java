package deviceet.business.device.domain.cache;

import deviceet.business.device.domain.CpuArchitecture;
import deviceet.business.device.domain.OsType;
import lombok.Builder;

@Builder
public record CachedDeviceReference(String id,
                                    String orgId,
                                    String configuredName,
                                    CpuArchitecture cpuArchitecture,
                                    OsType osType) {
}
