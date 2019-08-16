package com.gionee.gnservice.sdk.member.database;

import android.provider.BaseColumns;

/**
 * Created by caocong on 11/25/16.
 */
public class Columns {
    public static final String CATE_TABLE = "category";
    public static final String VERSION_TABLE = "version";
    public static final String CONTENT_TABLE = "content";
    public static final String UPDATE_TIME_TABLE = "updatetime";

    Columns() {
        throw new RuntimeException("Stub!");
    }

    protected static final class CateColumns implements BaseColumns {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String DESC = "desc";
        public static final String ICON = "icon";
        public static final String ICON_2 = "icon2";
        public static final String IMG = "img";
        public static final String MEMBER_LEVEL = "level";
    }

    protected static final class ContentColumns implements BaseColumns {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String CONTENT = "con";
        public static final String CID = "cid";
    }

    protected static final class VersionColumns implements BaseColumns {
        public static final String VERSION = "version";
    }

    protected static final class UpdateTimeColumns implements BaseColumns {
        public static final String UPDATE_TIME = "time";
    }
}
