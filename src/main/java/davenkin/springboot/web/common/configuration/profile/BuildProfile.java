package davenkin.springboot.web.common.configuration.profile;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static davenkin.springboot.web.common.Constants.BUILD_PROFILE;

@Retention(RetentionPolicy.RUNTIME)
@Profile(BUILD_PROFILE)
public @interface BuildProfile {
}
