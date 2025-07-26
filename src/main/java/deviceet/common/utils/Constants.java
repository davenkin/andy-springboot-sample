package deviceet.common.utils;

public class Constants {
    public static final String MONGO_ID = "_id";
    public static final String CI_PROFILE = "ci";
    public static final String PROFILE_NEGATIVE_PREFIX = "!";
    public static final String NON_CI_PROFILE = PROFILE_NEGATIVE_PREFIX + CI_PROFILE;
    public static final String SHEDLOCK_COLLECTION = "shedlock";
    public static final String USER_COLLECTION = "user";
    public static final String PUBLISHING_EVENT_COLLECTION = "publishing_event";
    public static final String CONSUMING_EVENT_COLLECTION = "consuming_event";
}
