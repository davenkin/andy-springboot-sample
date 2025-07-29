package deviceet.about;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AboutController {
    private final Instant deployTime = Instant.now();

    @GetMapping(value = "/about")
    public QAboutInfo about() {
        String deployTime = this.deployTime.toString();
        return QAboutInfo.builder()
                .deployTime(deployTime)
                .build();
    }

    @GetMapping(value = "/favicon.ico")
    public void favicon() {
    }
}
