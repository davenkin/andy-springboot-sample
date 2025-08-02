package deviceet.common.utils;

public class Constants {
    public static final String MONGO_ID = "_id";
    @Deprecated
    public static final String PLATFORM_ORG_ID = "PLATFORM_ORG_ID";

    public static final String DISABLE_PROFILE_PREFIX = "!";
    public static final String IT_PROFILE = "it";
    public static final String NON_IT_PROFILE = DISABLE_PROFILE_PREFIX + IT_PROFILE;

    //Event
    public static final String KAFKA_DOMAIN_EVENT_TOPIC = "domain-event-topic";
    public static final String PUBLISHING_EVENT_COLLECTION = "publishing-event";
    public static final String CONSUMING_EVENT_COLLECTION = "consuming-event";

    //Database
    public static final String SHEDLOCK_COLLECTION = "shedlock";
    public static final String DEVICE_COLLECTION = "device";

    //Cache
    public static final String ORG_DEVICES_CACHE = "ORG_DEVICES";

    //for testing
    public static final String KAFKA_EXTERNAL_DEVICE_REGISTRATION_TOPIC = "test-device-registration-topic";
    public static final String TEST_ORG_ID = "testOrgId";
    public static final String TEST_USER_ID = "testUserId";

}
