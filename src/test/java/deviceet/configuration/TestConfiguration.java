package deviceet.configuration;

import de.flapdoodle.embed.mongo.commands.MongodArguments;
import de.flapdoodle.embed.mongo.config.Storage;
import deviceet.common.configuration.profile.EnableForCI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableForCI
@Configuration
public class TestConfiguration {
    @Bean
    MongodArguments mongodArguments() {
        return MongodArguments.builder()
                .replication(Storage.of("test", 10))
                .build();
    }
}
