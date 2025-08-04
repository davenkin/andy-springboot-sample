package deviceet.business.device.query;

import deviceet.business.device.domain.CpuArchitecture;
import deviceet.business.device.domain.OsType;

import java.time.Instant;

public record QListedDevice(
        String id,
        String orgId,
        Instant createdAt,
        String createdBy,
        String reportedName,
        String configuredName,
        CpuArchitecture cpuArchitecture,
        OsType osType) {
}
