package deviceet.device;

import deviceet.common.security.Principal;
import deviceet.common.utils.ResponseId;
import deviceet.device.command.ConfigureDeviceNameCommand;
import deviceet.device.command.CreateDeviceCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static deviceet.common.security.Role.ORG_ADMIN;
import static org.springframework.http.HttpStatus.CREATED;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/devices")
public class DeviceController {
    public static final Principal TEST_PRINCIPAL = new Principal("testAdminUserId", "testAdminUser", ORG_ADMIN, "testOrgId");

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseId createDevice(@RequestBody @Valid CreateDeviceCommand createDeviceCommand) {
        return new ResponseId(null);
    }

    @PutMapping(value = "/{deviceId}/name")
    public void configureDeviceName(@PathVariable("deviceId") @NotBlank String deviceId,
                                    @RequestBody @Valid ConfigureDeviceNameCommand configureDeviceNameCommand) {
    }

}
