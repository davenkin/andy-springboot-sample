package davenkin.springboot.web.about;

import davenkin.springboot.web.BaseApiTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AboutControllerApiTest extends BaseApiTest {

    @Test
    public void should_display_about_info() {
        var result = this.webTestClient
                .get()
                .uri("/about")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(QAboutInfo.class)
                .returnResult()
                .getResponseBody();
        assertNotNull(result.getDeployTime());
    }
}
