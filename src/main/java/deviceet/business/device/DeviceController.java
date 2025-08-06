package deviceet.business.device;

import deviceet.business.device.command.ConfigureDeviceNameCommand;
import deviceet.business.device.command.DeviceCommandService;
import deviceet.business.device.query.DeviceQueryService;
import deviceet.business.device.query.ListDeviceQuery;
import deviceet.business.device.query.QListedDevice;
import deviceet.common.model.Principal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static deviceet.common.model.Role.ORG_ADMIN;
import static deviceet.common.utils.Constants.TEST_ORG_ID;
import static deviceet.common.utils.Constants.TEST_USER_ID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/devices")
public class DeviceController {
    //In real case, the Principal is usually build from some security context such as Spring Security's SecurityContextHolder
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
