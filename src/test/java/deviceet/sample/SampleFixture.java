package deviceet.sample;

import deviceet.common.model.principal.Operator;
import deviceet.common.model.principal.UserOperator;

import static deviceet.common.model.Role.ORG_ADMIN;

public class SampleFixture {
    public static final String SAMPLE_USER_ID = "sampleUserId";
    public static final String SAMPLE_USER_NAME = "sampleUserName";
    public static final String SAMPLE_ORG_ID = "sampleOrgId";
    public static final Operator SAMPLE_USER_OPERATOR = UserOperator.of(SAMPLE_USER_ID, SAMPLE_USER_NAME, ORG_ADMIN, SAMPLE_ORG_ID);
}
