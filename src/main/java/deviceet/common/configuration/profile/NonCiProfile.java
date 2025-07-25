package deviceet.common.configuration.profile;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static deviceet.common.Constants.NON_CI_PROFILE;

@Retention(RetentionPolicy.RUNTIME)
@Profile(NON_CI_PROFILE)
public @interface NonCiProfile {
}
