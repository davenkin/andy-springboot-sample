package deviceet.common.configuration.profile;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static deviceet.common.util.Constants.NON_IT_PROFILE;

//Disable for integration test
@Retention(RetentionPolicy.RUNTIME)
@Profile(NON_IT_PROFILE)
public @interface DisableForIT {
}
