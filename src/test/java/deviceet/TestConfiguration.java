package deviceet;

import de.flapdoodle.embed.mongo.commands.MongodArguments;
import de.flapdoodle.embed.mongo.config.Storage;
import deviceet.common.model.Principal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static deviceet.common.model.Role.ORG_ADMIN;

@Configuration(proxyBeanMethods = false)
public class TestConfiguration {
    public static final String TEST_USER_ID = "testUserId";
    public static final String TEST_USER_NAME = "testUserName";
    public static final String TEST_ORG_ID = "testOrgId";
    public static final Principal TEST_PRINCIPAL = new Principal(TEST_USER_ID, TEST_USER_NAME, ORG_ADMIN, TEST_ORG_ID);

    @Bean
    MongodArguments mongodArguments() {
        return MongodArguments.builder()
                .replication(Storage.of("integration-test", 10))
                .build();
    }
}
