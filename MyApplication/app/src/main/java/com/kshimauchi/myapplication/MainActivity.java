package com.kshimauchi.myapplication;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kshimauchi.myapplication.database.TaskHelper;
import com.kshimauchi.myapplication.database.Task;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    /*****************************************/

    private static final String TAG = "MainActivity";

    private TaskHelper mHelper;

    private ListView mTaskListView;

    private ArrayAdapter<String> mAdapter;
    /*****************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mHelper = new TaskHelper(this);

        mTaskListView = (ListView) findViewById(R.id.list_todo);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //used to instantiate menu XML files into Menu objects.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle item selected, passes the MenuItem selected
        switch (item.getItemId()) {
            case R.id.add_task:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setMessage("What next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(Task.TaskEntry.COL_TASK_TITLE, task);
                                db.insertWithOnConflict(Task.TaskEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteTask(View view) {

        View parent = (View) view.getParent();

        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);

        String task = String.valueOf(taskTextView.getText());

        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.delete(Task.TaskEntry.TABLE,
                Task.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});

        db.close();

        updateUI();
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(Task.TaskEntry.TABLE,
                new String[]{Task.TaskEntry._ID, Task.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);

        while (cursor.moveToNext()) {

            int idx = cursor.getColumnIndex(Task.TaskEntry.COL_TASK_TITLE);

            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            // Returns a view for each object in a collection of data objects you provide, and can
            // be used with list-based user interface widgets such as ListView and Spinner
            mAdapter = new ArrayAdapter<>( this, R.layout.item_todo, R.id.task_title,  taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }
}
