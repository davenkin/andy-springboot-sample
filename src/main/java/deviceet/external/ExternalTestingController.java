package deviceet.external;

import deviceet.common.utils.ResponseId;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/testing")
public class ExternalTestingController {
    private final ExternalEventPublisher externalEventPublisher;

    @GetMapping("/register-new-device")
    public ResponseId registerDevice() {
        return new ResponseId(this.externalEventPublisher.publishRandomDeviceRegistrationEvent());
    }
}
