package deviceet.sample;

import deviceet.common.model.principal.Principal;
import deviceet.common.model.principal.UserPrincipal;

import static deviceet.common.model.principal.Role.ORG_ADMIN;

public class SampleFixture {
    public static final String SAMPLE_USER_ID = "sampleUserId";
    public static final String SAMPLE_USER_NAME = "sampleUserName";
    public static final String SAMPLE_ORG_ID = "sampleOrgId";
    public static final Principal SAMPLE_USER_PRINCIPAL = UserPrincipal.of(SAMPLE_USER_ID, SAMPLE_USER_NAME, ORG_ADMIN, SAMPLE_ORG_ID);
}
