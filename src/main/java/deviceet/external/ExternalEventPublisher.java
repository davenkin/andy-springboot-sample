package deviceet.external;

import deviceet.device.domain.CpuArchitecture;
import deviceet.device.domain.OsType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static deviceet.common.utils.Constants.KAFKA_EXTERNAL_DEVICE_REGISTRATION_TOPIC;
import static deviceet.common.utils.RandomEnumUtils.randomEnum;
import static deviceet.common.utils.SnowflakeIdGenerator.newSnowflakeId;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.RandomStringUtils.secure;

@Component
@RequiredArgsConstructor
public class ExternalEventPublisher {
    private final KafkaTemplate<String, ExternalDeviceCreatedEvent> kafkaTemplate;
    private final String orgId = valueOf(newSnowflakeId());

    public String publishRandomDeviceRegistrationEvent() {
        ExternalDeviceCreatedEvent event = ExternalDeviceCreatedEvent.builder()
                .type("device_registered")
                .id(valueOf(newSnowflakeId()))
                .deviceId(valueOf(newSnowflakeId()))
                .deviceName(secure().nextAlphabetic(8))
                .osType(randomEnum(OsType.class))
                .cpuArchitecture(randomEnum(CpuArchitecture.class))
                .orgId(orgId)
                .build();
        kafkaTemplate.send(KAFKA_EXTERNAL_DEVICE_REGISTRATION_TOPIC, event.getDeviceId(), event);
        return event.getId();
    }
}
