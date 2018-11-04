package com.example.admin.personalproject3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getSharedPreferences("MY_PREFERENCE", Context.MODE_PRIVATE);
        password = sharedPref.getString("password", "");
        if(!password.equals("")) {
            findViewById(R.id.newPassword).setVisibility(View.INVISIBLE);
            findViewById(R.id.confirmPassword).setVisibility(View.INVISIBLE);
            findViewById(R.id.Password).setVisibility(View.VISIBLE);
        }
    }

    public void OKButtonOnClick(View target) {
        if (password.equals("")) {
            // 为空说明不存在密码
            String newPassword = ((EditText)findViewById(R.id.newPassword)).getText().toString();
            String confirmPassword = ((EditText)findViewById(R.id.confirmPassword)).getText().toString();
            if (newPassword.length() == 0) {
                Toast.makeText(getApplication(),"Password cannot be empty.",Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(getApplication(),"Password Mismatch.",Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPref = this.getSharedPreferences("MY_PREFERENCE", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("password", newPassword);
                editor.commit();
                // 跳转到编辑页面
                Intent intent = new Intent(MainActivity.this, FileEditorActivity.class);
                startActivity(intent);
            }
        } else {
            // 已经有了密码的情况
            String userPassword = ((EditText)findViewById(R.id.Password)).getText().toString();
            if (!userPassword.equals(password)) {
                Toast.makeText(getApplication(),"Invalid Password.",Toast.LENGTH_SHORT).show();
            } else {
                // 跳转到编辑页面
                Intent intent = new Intent(MainActivity.this, FileEditorActivity.class);
                startActivity(intent);
            }
        }
    }

    public void CLEARButtonOnClick(View target) {
        ((EditText)findViewById(R.id.newPassword)).setText("");
        ((EditText)findViewById(R.id.confirmPassword)).setText("");
        ((EditText)findViewById(R.id.Password)).setText("");
    }
}
