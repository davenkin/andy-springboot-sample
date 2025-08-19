package deviceet.common.configuration.profile;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.Retention;

import static deviceet.common.util.Constants.IT_PROFILE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

// Enable for integration tests
@Retention(RUNTIME)
@Profile(IT_PROFILE)
public @interface EnableForIT {
}
