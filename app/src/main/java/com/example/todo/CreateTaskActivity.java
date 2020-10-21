package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateTaskActivity extends AppCompatActivity {

    private TodoDbHelper dbHelper;
    private Context context;
    private long subjectId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        context = getApplicationContext();
        dbHelper = TodoDbHelper.getInstance(context);
        subjectId = getIntent().getLongExtra("subjectId", 0);
    }


    public void createTask(View view) {
        EditText input = findViewById(R.id.inputTask);
        String name = input.getText().toString().trim();
        if (name.length() > 0) {
            SQLiteDatabase dbWritable = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("checked", false);
            values.put("subject_id", subjectId);
            long result = dbWritable.insert("task", null, values);
            if (result != -1) {
                finish();
            }
        }
    }
}