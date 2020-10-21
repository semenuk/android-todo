package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskActivity extends AppCompatActivity {

    private TodoDbHelper dbHelper;
    private Context context;
    private long subjectIdValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        context = getApplicationContext();
        dbHelper = TodoDbHelper.getInstance(context);
        subjectIdValue = getIntent().getLongExtra("subjectId", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        renderTasks();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LinearLayout taskList = findViewById(R.id.taskList);
        taskList.removeAllViews();
    }

    public void onButtonCreate(View view) {
        Intent intent = new Intent(context, CreateTaskActivity.class);
        intent.putExtra("subjectId", subjectIdValue);
        startActivity(intent);
    }

    private void renderTasks() {
        try {
            SQLiteDatabase dbReadable = dbHelper.getReadableDatabase();
            String subjectId = Long.toString(subjectIdValue);
            String[] selectionArgs = {subjectId};
            Cursor cursor = dbReadable.rawQuery("SELECT * FROM task WHERE subject_id = ?;", selectionArgs);
            LinearLayout taskList = findViewById(R.id.taskList);

            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int checkedIndex = cursor.getColumnIndex("checked");
            int length = cursor.getCount();
            for (int i = 0; i < length; i++) {
                boolean hasRow = cursor.moveToPosition(i);
                if (hasRow) {
                    long taskId = cursor.getLong(idIndex);
                    String name = cursor.getString(nameIndex);
                    boolean isChecked = cursor.getInt(checkedIndex) > 0;
                    LinearLayout listItem = createTask(name, isChecked, taskId);
                    taskList.addView(listItem, 0);
                }
            }
        } catch (Exception e) {
            Button input = findViewById(R.id.buttonCreate2);
            input.setText(e.getLocalizedMessage());
        }

    }

    private LinearLayout createTask(String name, boolean isChecked, final long taskId) {
        LinearLayout layout = new LinearLayout(context);
        layout.addView(createText(name));
        layout.addView(createCheckbox(taskId, isChecked));
        return layout;
    }

    private TextView createText(String text) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setPadding(0, 0, 12, 0);
        textView.setLayoutParams(getLayoutParams(1));
        return textView;
    }

    private CheckBox createCheckbox(final long taskId, boolean isChecked) {
        CheckBox checkBox = new CheckBox(context);
        checkBox.setLayoutParams(getLayoutParams(0));
        checkBox.setChecked(isChecked);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check(taskId, isChecked);
            }
        });
        return checkBox;
    }

    private void check(long taskId, boolean isChecked) {
        SQLiteDatabase dbWritable = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("checked", isChecked ? 1 : 0);
        String id = Long.toString(taskId);
        dbWritable.update("task", values, "id = ?", new String[] { id });
    }

    private LinearLayout.LayoutParams getLayoutParams(int weight) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.weight = weight;
        return layoutParams;
    }
}