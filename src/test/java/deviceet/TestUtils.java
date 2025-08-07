package deviceet;

import deviceet.common.model.Principal;
import deviceet.common.model.Role;
import org.apache.commons.lang3.RandomStringUtils;

import static deviceet.common.model.Role.ORG_ADMIN;
import static org.apache.commons.lang3.RandomUtils.secure;

public class TestUtils {
    public static final String TEST_USER_ID = "testUserId";
    public static final String TEST_USER_NAME = "testUserName";
    public static final String TEST_ORG_ID = "testOrgId";
    public static final Principal TEST_PRINCIPAL = new Principal(TEST_USER_ID, TEST_USER_NAME, ORG_ADMIN, TEST_ORG_ID);

    public static String randomEquipmentName() {
        return RandomStringUtils.secure().nextAlphanumeric(10);
    }

    public static String randomUserId() {
        return RandomStringUtils.secure().nextAlphanumeric(10);
    }

    public static String randomUserName() {
        return RandomStringUtils.secure().nextAlphanumeric(10);
    }

    public static String randomOrgId() {
        return RandomStringUtils.secure().nextAlphanumeric(10);
    }

    public static Role randomRole() {
        return randomEnum(Role.class);
    }

    public static Principal randomPrincipal() {
        return new Principal(randomUserId(), randomUserName(), randomRole(), randomOrgId());
    }

    public static <T extends Enum<T>> T randomEnum(Class<T> enumClass) {
        T[] constants = enumClass.getEnumConstants();
        return constants[secure().randomInt(0, constants.length)];
    }
}
