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

import com.google.android.material.textfield.TextInputLayout;

public class CreateActivity extends AppCompatActivity {

    private TodoDbHelper dbHelper;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        context = getApplicationContext();
        dbHelper = TodoDbHelper.getInstance(context);
    }

    public void createSubject(View view) {
        EditText input = findViewById(R.id.inputSubject);
        String name = input.getText().toString().trim();
        if (name.length() > 0) {
            SQLiteDatabase dbWritable = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            long result = dbWritable.insert("subject", null, values);
            if (result != -1) {
                finish();
            }
        }
    }
}