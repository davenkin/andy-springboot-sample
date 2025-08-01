package deviceet.common.utils;

public class Constants {
    public static final String MONGO_ID = "_id";
    @Deprecated
    public static final String PLATFORM_ORG_ID = "PLATFORM_ORG_ID";

    public static final String DISABLE_PROFILE_PREFIX = "!";
    public static final String CI_PROFILE = "ci";
    public static final String NON_CI_PROFILE = DISABLE_PROFILE_PREFIX + CI_PROFILE;
    public static final String KAFKA_DOMAIN_EVENT_TOPIC = "domain-event-topic";

    public static final String PUBLISHING_EVENT_COLLECTION = "publishing-event";
    public static final String CONSUMING_EVENT_COLLECTION = "consuming-event";

    //Database
    public static final String SHEDLOCK_COLLECTION = "shedlock";
    public static final String USER_COLLECTION = "user";

    //Cache
    public static final String ORG_USERS_CACHE = "ORG_USERS";

}
