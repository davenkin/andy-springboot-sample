package deviceet;

import deviceet.business.device.domain.CpuArchitecture;
import deviceet.business.device.domain.OsType;
import deviceet.external.ExternalDeviceCreatedEvent;

import static deviceet.common.utils.RandomEnumUtils.randomEnum;
import static deviceet.common.utils.SnowflakeIdGenerator.newSnowflakeId;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.RandomStringUtils.secure;

public class TestRandomEventGenerator {
    public static ExternalDeviceCreatedEvent buildExternalDeviceCreateEvent() {
        return ExternalDeviceCreatedEvent.builder()
                .type("device_registered")
                .id(valueOf(newSnowflakeId()))
                .deviceId("DVC" + newSnowflakeId())
                .deviceName(secure().nextAlphabetic(8))
                .osType(randomEnum(OsType.class))
                .cpuArchitecture(randomEnum(CpuArchitecture.class))
                .orgId("ORG" + newSnowflakeId())
                .build();
    }

    public static ExternalDeviceCreatedEvent buildExternalDeviceCreateEvent(String orgId) {
        return ExternalDeviceCreatedEvent.builder()
                .type("device_registered")
                .id(valueOf(newSnowflakeId()))
                .deviceId("DVC" + newSnowflakeId())
                .deviceName(secure().nextAlphabetic(8))
                .osType(randomEnum(OsType.class))
                .cpuArchitecture(randomEnum(CpuArchitecture.class))
                .orgId(orgId)
                .build();
    }
}
