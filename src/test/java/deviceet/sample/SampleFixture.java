package deviceet.sample;

import deviceet.common.model.principal.Principal;
import deviceet.common.model.principal.UserPrincipal;

import static deviceet.common.model.principal.Role.ORG_ADMIN;

public class SampleFixture {
    public static final String TEST_USER_ID = "testUserId";
    public static final String TEST_USER_NAME = "testUserName";
    public static final String TEST_ORG_ID = "testOrgId";
    public static final Principal SAMPLE_USER_PRINCIPAL = UserPrincipal.of(TEST_USER_ID, TEST_USER_NAME, ORG_ADMIN, TEST_ORG_ID);
}
