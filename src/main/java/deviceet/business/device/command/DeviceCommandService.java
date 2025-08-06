package deviceet.business.device.command;

import deviceet.business.device.domain.Device;
import deviceet.business.device.domain.DeviceRepository;
import deviceet.common.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static deviceet.common.model.Role.ORG_IT_ADMIN;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceCommandService {
    private final DeviceRepository deviceRepository;

    @Transactional
    public void configureDeviceName(String deviceId, ConfigureDeviceNameCommand command, Principal principal) {
        principal.checkRole(ORG_IT_ADMIN);
        Device device = deviceRepository.byId(deviceId, principal.getOrgId());
        device.configureName(command.name());
        deviceRepository.save(device);
        log.info("Device {} configured with name {}.", deviceId, command.name());
    }
}
