package com.kshimauchi.myapplication.database;

import android.provider.BaseColumns;

/**
 * Created by kshim on 6/13/2017.
 */
public class Task {
    public static final String DB_NAME= "com.kshimauchi.myapplication.db";

    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        //Create a table  in SQLite
        public static final String TABLE = "tasks";
        //Create a column in that the SQLite Table
        public static final String COL_TASK_TITLE = "title";
    }
}
