package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TodoDbHelper dbHelper;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        dbHelper = TodoDbHelper.getInstance(context);
    }

    @Override
    protected void onStart() {
        super.onStart();
        renderSubjects();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LinearLayout subjectList = findViewById(R.id.subjectList);
        subjectList.removeAllViews();
    }

    public void onButtonCreate(View view) {
        Intent intent = new Intent(context, CreateActivity.class);
        startActivity(intent);
    }

    private void renderSubjects() {
        SQLiteDatabase dbReadable = dbHelper.getReadableDatabase();
        String[] selectionArgs = {};
        Cursor cursor = dbReadable.rawQuery("SELECT * FROM subject;", selectionArgs);
        LinearLayout subjectList = findViewById(R.id.subjectList);

        int idIndex = cursor.getColumnIndex("id");
        int nameIndex = cursor.getColumnIndex("name");
        int length = cursor.getCount();
        for (int i = 0; i < length; i++) {
            boolean hasRow = cursor.moveToPosition(i);
            if (hasRow) {
                long id = cursor.getLong(idIndex);
                String name = cursor.getString(nameIndex);
                LinearLayout listItem = createSubjectView(name, id);
                subjectList.addView(listItem, 0);
            }
        }
    }

    private LinearLayout createSubjectView(String name, final long subjectId) {
        LinearLayout layout = new LinearLayout(context);
        layout.addView(createText(name));
        layout.addView(createArrow(subjectId));
        return layout;
    }

    private TextView createText(String text) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setPadding(0, 6, 12, 0);
        textView.setLayoutParams(getLayoutParams(1));
        return textView;
    }

    private ImageButton createArrow(final long subjectId) {
        ImageButton button = new ImageButton(context);
        button.setImageResource(R.drawable.ic_arrow_right);
        button.setLayoutParams(getLayoutParams(0));
        button.setBackgroundColor(0);
        button.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
                intent.putExtra("subjectId", subjectId);
                startActivity(intent);
            }
        }));
        return button;
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