package com.framgia.englishconversation.utils;

/**
 *
 */

public class Constant {

    public static final String END_POINT_URL = "https://api.github.com";
    public static final int MILLISECONDS_PER_MINUTE = 1000;
    public static final int ONE_HUNDRED_PERCENT = 100;
    public static final int ZERO_PERCENT = 0;
    public static final int SECOND_PER_MINUTE = 60;
    public static final String DEFAULT_FORMAT_AUDIO = ".3gp";
    public static final int FLAG_HIDE_KEYBOARD = 0;
    public static final int MINIMUM_CHARACTERS_PASSWORD = 6;
    public static final int MIN_CHARACTERS = 1;
    public static final String EMAIL_FORMAT = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public class Timeline {
        public static final int NO_IMAGE = 0;
        public static final int ONE_IMAGE = 1;
        public static final int TWO_IMAGE = 2;
        public static final int THREE_IMAGE = 3;
        public static final int FOUR_IMAGE = 4;
    }

    //Constant related to database's field name
    public class DatabaseTree {
        public static final String POST = "post";
        public static final String COMMENT = "comment";
        public static final String CREATED_AT = "createdAt";
    }

    public class RequestCode {
        public static final int RECORD_AUDIO = 135;
        public static final int RECORD_VIDEO = 136;
        public static final int SELECT_IMAGE = 137;
        public static final int POST_COMMENT = 138;
    }

    public static final String USER_AGENT = "user-agent";
    public static final String EXTRA_TIMELINE = "EXTRA_TIMELINE";
    public static final String EXTRA_MEDIA_MODEL = "EXTRA_MEDIA_MODEL";
    public static final String EXTRA_COMMENT = "EXTRA_COMMENT";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final int INDEX_FIRST_ITEM = 0;
}
