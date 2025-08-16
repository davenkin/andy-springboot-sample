package deviceet.common.configuration.profile;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static deviceet.common.util.Constants.IT_PROFILE;

//Enable for integration tests
@Retention(RetentionPolicy.RUNTIME)
@Profile(IT_PROFILE)
public @interface EnableForIT {
}
