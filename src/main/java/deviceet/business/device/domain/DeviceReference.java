package deviceet.business.device.domain;

import lombok.Builder;

@Builder
public record DeviceReference(String id,
                              String orgId,
                              String configuredName,
                              CpuArchitecture cpuArchitecture,
                              OsType osType) {
}
