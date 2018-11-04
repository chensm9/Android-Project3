package com.example.admin.personalproject3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileEditorActivity extends AppCompatActivity {
    private static String FILE_NAME = "myFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_editor);
    }

    public void SAVEButtonOnClick(View target) {
        try (FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE)) {
            String text = ((EditText)findViewById(R.id.editText)).getText().toString();
            fileOutputStream.write(text.getBytes());
            Toast.makeText(getApplication(),"Save successfully.",Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            Toast.makeText(getApplication(),"Fail to save file.",Toast.LENGTH_SHORT).show();
        }
    }

    public void LOADButtonOnClick(View target) {
        try (FileInputStream fileInputStream = openFileInput(FILE_NAME)) {
            byte[] text = new byte[fileInputStream.available()];
            fileInputStream.read(text);
            ((EditText)findViewById(R.id.editText)).setText(new String(text));
            Toast.makeText(getApplication(),"Load successfully.",Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            Toast.makeText(getApplication(),"Fail to load file.",Toast.LENGTH_SHORT).show();
        }
    }

    public void CLEARButtonOnClick(View target) {
        ((EditText)findViewById(R.id.editText)).setText("");
    }
}
