package com.example.sqlitedemoapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText etStdntId, etStdntName, etStdntProgram;
    Button btAdd, btView, btSearch, btDelete;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etStdntId = findViewById(R.id.editTextText);
        etStdntName = findViewById(R.id.editTextText2);
        etStdntProgram = findViewById(R.id.editTextText3);
        btAdd = findViewById(R.id.btnAdd);
        btView = findViewById(R.id.btnViewAll);
        btSearch = findViewById(R.id.btnSearch);
        btDelete = findViewById(R.id.btnDelete);
        db = openOrCreateDatabase("StudentDB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(stdntId VARCHAR, stdntName VARCHAR, stdntProgram VARCHAR);");

    }

    public void onClick(View view) {
        if (view == btAdd) {
            db.execSQL("INSERT INTO student VALUES('" + etStdntId.getText() + "','" + etStdntName.getText() + "','" + etStdntProgram.getText() + "');");
            showMessage("Success", "Record added");
            clearText();
        }
        else if (view == btDelete) {
            db.execSQL("DELETE FROM student WHERE stdntId='" + etStdntId.getText() + "';");
            showMessage("Success", "Record deleted");
            clearText();
        }
        else if (view == btSearch) {
            Cursor c = db.rawQuery("SELECT * FROM student WHERE stdntId='" + etStdntId.getText() + "'", null);
            StringBuilder buffer = new StringBuilder();
            if (c.moveToFirst()) {
                buffer.append("Student Name: ").append(c.getString(1)).append("\n");
                buffer.append("Student Program: ").append(c.getString(2)).append("\n\n");
            }
            showMessage("Student Details", buffer.toString());
            c.close();
            clearText();
        } else if (view == btView) {
            Cursor c = db.rawQuery("SELECT * FROM student", null);

            if(c.getCount() == 0) {
                showMessage("Error", "No records found.");

            }

            StringBuilder buffer = new StringBuilder();
            while (c.moveToNext()) {
                buffer.append("Student ID: ").append(c.getString(0)).append("\n");
                buffer.append("Student Name: ").append(c.getString(1)).append("\n");
                buffer.append("Student Program: ").append(c.getString(2)).append("\n\n");
            }
            showMessage("Student Details", buffer.toString());
            c.close();
        }

    }

    public void showMessage(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.baseline_school_24);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }

    public void clearText() {
        etStdntId.setText("");
        etStdntName.setText("");
        etStdntProgram.setText("");
        etStdntId.requestFocus();
    }
}
