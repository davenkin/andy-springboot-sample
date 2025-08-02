package deviceet.device.command;

import deviceet.common.security.Principal;
import deviceet.device.domain.Device;
import deviceet.device.domain.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceCommandService {
    private final DeviceRepository deviceRepository;

    @Transactional
    public void configureDeviceName(String deviceId, ConfigureDeviceNameCommand command, Principal principal) {
        Device device = deviceRepository.byId(deviceId, principal.getOrgId());
        device.configureName(command.name());
        deviceRepository.save(device);
        log.info("Device {} configured with name {}.", deviceId, command.name());
    }
}
