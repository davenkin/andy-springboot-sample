package deviceet.device.query;

import deviceet.device.domain.CpuArchitecture;
import deviceet.device.domain.OsType;

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
