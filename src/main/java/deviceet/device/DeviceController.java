package deviceet.device;

import deviceet.common.security.Principal;
import deviceet.device.command.ConfigureDeviceNameCommand;
import deviceet.device.command.DeviceCommandService;
import deviceet.device.query.DeviceQueryService;
import deviceet.device.query.ListDeviceQuery;
import deviceet.device.query.QListedDevice;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static deviceet.common.security.Role.ORG_ADMIN;
import static deviceet.common.utils.Constants.TEST_ORG_ID;
import static deviceet.common.utils.Constants.TEST_USER_ID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/devices")
public class DeviceController {
    public static final Principal TEST_PRINCIPAL = new Principal(TEST_USER_ID, "testAdminUser", ORG_ADMIN, TEST_ORG_ID);
    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;

    @PutMapping(value = "/{deviceId}/name")
    public void configureDeviceName(@PathVariable("deviceId") @NotBlank String deviceId,
                                    @RequestBody @Valid ConfigureDeviceNameCommand configureDeviceNameCommand) {
        this.deviceCommandService.configureDeviceName(deviceId, configureDeviceNameCommand, TEST_PRINCIPAL);
    }

    @PostMapping("/list")
    public Page<QListedDevice> listDevices(@RequestBody @Valid ListDeviceQuery query,
                                           @PageableDefault Pageable pageable) {
        return this.deviceQueryService.listDevices(query, pageable, TEST_PRINCIPAL);
    }

}
