package com.kshimauchi.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kshimauchi.myapplication.database.Task;
/**
 * Created by kshim on 6/13/2017.
 */

// takes care of opening the database if it exists, creating it if it does not,
// and upgrading it as necessary. Transactions are used to make sure the
// database is always in a sensible state.
public class TaskHelper extends SQLiteOpenHelper
{
    //Create a constructor with the "context, db_name and db-version
    public TaskHelper(Context context) {
        super(context, Task.DB_NAME, null, Task.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create attributes for the table
        //Create a helper object to create, open, and/or manage a database.
        String createTable = "CREATE TABLE " + Task.TaskEntry.TABLE +
                " ( " +

                Task.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                Task.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL) ; "
                ;
        //execute the sql statement
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop the table if it already exists
        db.execSQL("DROP TABLE IF EXISTS " + Task.TaskEntry.TABLE);
        //
        onCreate(db);
    }

}
