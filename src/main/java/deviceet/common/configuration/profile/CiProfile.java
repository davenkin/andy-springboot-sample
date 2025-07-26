package deviceet.common.configuration.profile;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static deviceet.common.utils.Constants.CI_PROFILE;

@Retention(RetentionPolicy.RUNTIME)
@Profile(CI_PROFILE)
public @interface CiProfile {
}
