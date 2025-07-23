package davenkin.springboot.web.common.configuration.profile;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static davenkin.springboot.web.common.Constants.NON_BUILD_PROFILE;

@Retention(RetentionPolicy.RUNTIME)
@Profile(NON_BUILD_PROFILE)
public @interface NonBuildProfile {
}
