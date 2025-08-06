package deviceet;

import deviceet.common.security.Principal;
import deviceet.common.security.Role;
import deviceet.common.utils.RandomEnumUtils;
import org.apache.commons.lang3.RandomStringUtils;

import static deviceet.common.security.Role.ROOT;

public class TestRandomUtils {
    public static String randomTestArName() {
        return RandomStringUtils.secure().nextAlphanumeric(6);
    }

    public static String randomUserId() {
        return RandomStringUtils.secure().nextAlphanumeric(6);
    }

    public static String randomUserName() {
        return RandomStringUtils.secure().nextAlphanumeric(6);
    }

    public static String randomOrgId() {
        return RandomStringUtils.secure().nextAlphanumeric(6);
    }

    public static Role randomRole() {
        return RandomEnumUtils.randomEnum(Role.class);
    }

    public static Principal randomPrincipal() {
        return new Principal(randomUserId(), randomUserName(), ROOT, randomOrgId());
    }
}
