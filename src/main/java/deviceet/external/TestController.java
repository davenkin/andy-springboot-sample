package deviceet.external;

import deviceet.common.configuration.profile.DisableForIT;
import deviceet.common.utils.ResponseId;
import deviceet.device.domain.CpuArchitecture;
import deviceet.device.domain.OsType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static deviceet.common.utils.Constants.KAFKA_EXTERNAL_DEVICE_REGISTRATION_TOPIC;
import static deviceet.common.utils.Constants.TEST_ORG_ID;
import static deviceet.common.utils.RandomEnumUtils.randomEnum;
import static deviceet.common.utils.SnowflakeIdGenerator.newSnowflakeId;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.RandomStringUtils.secure;

@Validated
@DisableForIT
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/testing")
public class TestController {
    private final KafkaTemplate<String, ExternalDeviceCreatedEvent> kafkaTemplate;

    @GetMapping("/register-new-device")
    public ResponseId registerDevice() {
        return new ResponseId(this.publishRandomDeviceRegistrationEvent());
    }

    public String publishRandomDeviceRegistrationEvent() {
        ExternalDeviceCreatedEvent event = ExternalDeviceCreatedEvent.builder()
                .type("device_registered")
                .id(valueOf(newSnowflakeId()))
                .deviceId(valueOf(newSnowflakeId()))
                .deviceName(secure().nextAlphabetic(8))
                .osType(randomEnum(OsType.class))
                .cpuArchitecture(randomEnum(CpuArchitecture.class))
                .orgId(TEST_ORG_ID)
                .build();
        kafkaTemplate.send(KAFKA_EXTERNAL_DEVICE_REGISTRATION_TOPIC, event.getDeviceId(), event);
        return event.getId();
    }
}
