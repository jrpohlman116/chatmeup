package com.jrpohlman.chatmeup.data;

import android.provider.BaseColumns;

/**
 * Created by jorda on 12/1/2017.
 */

public class UserContract {

    private UserContract() {}

    public static final class UserEntry implements BaseColumns {

        public final static String TABLE1_NAME = "swdChat";

        public final static String TABLE2_NAME = "groupChat";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_NAME ="username";

        public final static String COLUMN_MESSAGE = "message";

    }
}
